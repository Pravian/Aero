/*
 * Copyright 2015 Jerom van der Sar.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.pravian.aero.command.handler;

import com.google.common.collect.Maps;
import lombok.Setter;
import net.pravian.aero.command.AeroCommandBase;
import net.pravian.aero.command.CommandReflection;
import net.pravian.aero.command.executor.AeroCommandExecutor;
import net.pravian.aero.command.executor.AeroCommandExecutorFactory;
import net.pravian.aero.command.executor.SimpleCommandExecutor;
import net.pravian.aero.plugin.AeroLogger;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;

import java.io.IOException;
import java.security.CodeSource;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Represents a handler for executing commands.
 *
 * @param <T extends Plugin> Optional Type-safety (Plugin instance) that the command can use to get the original Plugin instance.
 */
public class SimpleCommandHandler<T extends AeroPlugin<T>> extends AbstractCommandHandler<T>
{

    private final Map<String, AeroCommandExecutor<?>> commands = Maps.newHashMap();
    private final Map<String, PluginCommand> registeredCommands = Maps.newHashMap();
    @Setter
    private AeroCommandExecutorFactory executorFactory;

    public SimpleCommandHandler(T plugin)
    {
        this(plugin, plugin.getPluginLogger());
    }

    public SimpleCommandHandler(T plugin, AeroLogger logger)
    {
        super(plugin, logger);
    }

    public AeroCommandExecutorFactory getExecutorFactory()
    {
        if (executorFactory == null)
        {
            executorFactory = new SimpleCommandExecutor.SimpleCommandExecutorFactory();
        }
        return executorFactory;
    }

    @Override
    public void clearCommands()
    {
        commands.clear();
    }

    private List<Class<?>> loadClasses(Package pack)
    {
        List<Class<?>> classes = new ArrayList<>();

        String prefix = pack.getName().replace('.', '/');

        CodeSource codeSource = plugin.getClass().getProtectionDomain().getCodeSource();
        if (codeSource == null)
        {
            logger.severe("Could not load commands from package: " + pack.getName() + "! Could not get CodeSource.");
            return null;
        }

        ZipInputStream zip;
        try
        {
            zip = new ZipInputStream(codeSource.getLocation().openStream());
        }
        catch (IOException ex)
        {
            logger.severe("Could not load commands from package: " + pack.getName() + "! Could not open CodeSource stream.");
            return null;
        }

        // No failure (null return) beyond this point
        while (true)
        {
            ZipEntry zipEntry;
            try
            {
                zipEntry = zip.getNextEntry();
            }
            catch (IOException ex)
            {
                logger.severe("Could not load commands from package: " + pack.getName() + "! Could not get ZIP entry");
                logger.severe(ex);
                break;
            }

            if (zipEntry == null)
            {
                break;
            }

            String name = zipEntry.getName();

            if (!name.startsWith(prefix))
            {
                continue;
            }

            if (!name.endsWith(".class"))
            {
                continue;
            }

            String loadName = name.substring(0, name.length() - 6).replace('/', '.');

            Class<?> loadClass;
            try
            {
                loadClass = Class.forName(loadName, true, plugin.getClass().getClassLoader());
            }
            catch (ClassNotFoundException ex)
            {
                logger.severe("Could not load command class: " + loadName + "! Class not found.");
                logger.severe(ex);
                continue;
            }

            classes.add(loadClass);

        }

        return classes;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int loadFrom(Package pack)
    {
        List<Class<?>> classes = loadClasses(pack);

        int loaded = 0;
        for (Class<?> clazz : classes)
        {
            String className = clazz.getSimpleName();
            if (!className.startsWith(commandClassPrefix))
            {
                logger.warning("Skipping command class: " + className + ". Class does not have required prefix!");
                continue;
            }

            final String commandName = className.substring(commandClassPrefix.length()).toLowerCase();

            if (commands.containsKey(commandName))
            {
                logger.warning("Skipping command class: " + className + ". Command name conflict!");
                continue;
            }

            AeroCommandBase<T> command;
            try
            {
                command = (AeroCommandBase<T>) clazz.newInstance();
            }
            catch (Exception ex)
            {
                plugin.handleException("Could not instantiate command class: " + className, ex);
                continue;
            }

            try
            {
                command.register(this);
            }
            catch (Exception ex)
            {
                plugin.handleException("Could not register command: " + commandName, ex);
                continue;
            }

            commands.put(commandName, getExecutorFactory().newExecutor(this, commandName, command));
            loaded++;
        }

        return loaded;
    }

    @Override
    public void add(AeroCommandBase<T> command, String name)
    {
        if (name == null)
        {
            name = command.getClass().getSimpleName();
            if (!name.startsWith(commandClassPrefix))
            {
                logger.warning("Skipping class in command package: " + name + ". Class does not have required prefix.");
                return;
            }

            name = name.substring(commandClassPrefix.length());
        }

        try
        {
            command.register(this);
        }
        catch (Exception ex)
        {
            plugin.handleException("Could not register command: " + name, ex);
            return;
        }

        commands.put(name, getExecutorFactory().newExecutor(this, name, command));
    }

    @Override
    public void add(AeroCommandExecutor<? extends AeroCommandBase<T>> executor)
    {
        try
        {
            executor.getCommandBase().register(this);
        }
        catch (Exception ex)
        {
            plugin.handleException("Could not register command: " + executor.getName(), ex);
            return;
        }

        commands.put(executor.getName(), executor);
    }

    @Override
    public boolean registerAll(String fallbackPrefix, boolean force)
    {
        registeredCommands.clear();

        // Get command map
        CommandMap map = CommandReflection.getCommandMap();
        if (map == null)
        {
            logger.severe("Could not register commands to command map. Could not find command map!");
            return false;
        }

        // Used to unregister old commands when force-registering
        Map<String, Command> mapKnownCommands = null;

        for (String name : commands.keySet())
        {
            final PluginCommand command = CommandReflection.newPluginCommand(name, plugin);

            if (command == null)
            {
                logger.severe("Could not register command: " + name + ". Could not instantiate PluginCommand instance!");
                continue;
            }

            final AeroCommandExecutor<?> executor = commands.get(name);

            // Allow the executor to set command options: aliases, usage, description
            executor.setupCommand(command);

            // Setup command
            command.setExecutor(executor);
            command.setTabCompleter(executor);

            // If we're force-registering the command, remove the previous command
            final Command prevCommand = map.getCommand(name);
            if (force && prevCommand != null)
            {
                if (mapKnownCommands == null && (mapKnownCommands = CommandReflection.getKnownCommands()) == null)
                {
                    logger.warning("Could not remove old command registration: " + prevCommand.getName() + ". Could not determine known commands!");
                }
                else
                {
                    // Unregister the old command
                    prevCommand.unregister(map);

                    // Remove any references to the old command
                    for (String label : new HashSet<>(mapKnownCommands.keySet()))
                    {
                        Command loopCommand = mapKnownCommands.get(label);
                        if (prevCommand.equals(loopCommand))
                        {
                            mapKnownCommands.remove(label);
                        }
                    }
                }
            }

            // Register command
            if (!map.register(fallbackPrefix, command))
            {
                logger.warning("Could not register command: " + command.getName() + ". (is this command already registered?)");
                continue;
            }

            registeredCommands.put(name, command);
        }
        return true;
    }

    @Override
    public Map<String, AeroCommandExecutor<?>> getExecutorMap()
    {
        return Collections.unmodifiableMap(commands);
    }

    @Override
    public Collection<AeroCommandExecutor<?>> getExecutors()
    {
        return commands.values();
    }

    @Override
    public Map<String, PluginCommand> getRegisteredCommandsMap()
    {
        return Collections.unmodifiableMap(registeredCommands);
    }

    @Override
    public Collection<PluginCommand> getRegisteredCommands()
    {
        return registeredCommands.values();
    }
}

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
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import net.pravian.aero.command.AeroCommandBase;
import net.pravian.aero.command.CommandOptions;
import net.pravian.aero.command.CommandReflection;
import net.pravian.aero.command.executor.AeroCommandExecutor;
import net.pravian.aero.command.executor.SimpleCommandExecutor;
import net.pravian.aero.plugin.AeroLogger;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;

/**
 * Represents a handler for executing commands.
 *
 * @param <T extends Plugin> Optional Type-safety (Plugin instance) that the command can use to get the original Plugin instance.
 */
public class SimpleCommandHandler<T extends AeroPlugin<T>> extends AbstractCommandHandler<T> {

    private final Map<String, AeroCommandExecutor<T>> commands = Maps.newHashMap();
    private final Map<String, PluginCommand> registeredCommands = Maps.newHashMap();

    public SimpleCommandHandler(T plugin) {
        this(plugin, plugin.getPluginLogger());
    }

    public SimpleCommandHandler(T plugin, AeroLogger logger) {
        super(plugin, logger);
    }

    @Override
    public void clearCommands() {
        commands.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadFrom(Package pack) {
        ClassPath classPath;
        try {
            classPath = ClassPath.from(plugin.getClass().getClassLoader());
        } catch (Exception ex) {
            plugin.logger.severe("Could not load commands from package: " + pack.getName());
            plugin.logger.severe(ex);
            return;
        }

        for (ClassInfo info : classPath.getTopLevelClasses(pack.getName())) {

            if (!info.getSimpleName().startsWith(commandClassPrefix)) {
                logger.debug("Skipping class in command package: " + info.getSimpleName() + ". Class does not have required prefix.");
                continue;
            }

            final String name = info.getSimpleName().substring(commandClassPrefix.length()).toLowerCase();

            if (commands.containsKey(name)) {
                logger.warning("Skipping class in command package: " + info.getSimpleName() + ". Command name conflict!");
                continue;
            }

            final Class<?> clazz = info.load();

            if (!AeroCommandBase.class.isAssignableFrom(clazz)) {
                logger.debug("Skipping class in command package: " + info.getSimpleName() + ". Class does can not be assigned to CommandBase.");
                continue;
            }

            AeroCommandBase<T> command;
            try {
                command = (AeroCommandBase<T>) clazz.newInstance();
            } catch (Exception ex) {
                plugin.handleException("Could not instantiate command class: " + info.getSimpleName(), ex);
                continue;
            }

            final SimpleCommandExecutor<T> executor = new SimpleCommandExecutor<T>(this, name, command);
            commands.put(name, executor);
        }
    }

    @Override
    public boolean registerAll() {
        return registerAll(plugin.getName());
    }

    @Override
    public boolean registerAll(String fallbackPrefix) {
        registeredCommands.clear();

        // Get command map
        CommandMap map = CommandReflection.getCommandMap();
        if (map == null) {
            logger.severe("Could not register commands to command map. Could not find command map!");
            return false;
        }

        for (String name : commands.keySet()) {
            final PluginCommand command = CommandReflection.newPluginCommand(name, plugin);

            if (command == null) {
                logger.severe("Could not register command: " + name + ". Could not instantiate PluginCommand instance!");
                continue;
            }

            final AeroCommandExecutor<T> executor = commands.get(name);

            // Copy data from executor
            if (executor instanceof SimpleCommandExecutor) {
                final SimpleCommandExecutor<T> sce = (SimpleCommandExecutor<T>) executor;

                final CommandOptions options = sce.getOptions();

                if (options != null) {
                    command.setUsage(options.usage());
                    command.setDescription(options.description());
                    command.setAliases(Arrays.asList(options.aliases().split(",")));
                }
            }

            // Setup command
            command.setExecutor(executor);
            command.setTabCompleter(executor);

            // Register command
            if (!map.register(fallbackPrefix, command)) {
                logger.warning("Could not register command: " + command.getName() + ". (is this command already registered?)");
                continue;
            }

            registeredCommands.put(name, command);
        }
        return true;
    }

    @Override
    public Map<String, AeroCommandExecutor<T>> getExecutorMap() {
        return Collections.unmodifiableMap(commands);
    }

    @Override
    public Collection<AeroCommandExecutor<T>> getExecutors() {
        return commands.values();
    }

    @Override
    public Map<String, PluginCommand> getRegisteredCommandsMap() {
        return Collections.unmodifiableMap(registeredCommands);
    }

    @Override
    public Collection<PluginCommand> getRegisteredCommands() {
        return registeredCommands.values();
    }
}

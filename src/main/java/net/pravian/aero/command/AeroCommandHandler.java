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
package net.pravian.aero.command;

import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import net.pravian.aero.command.permission.PermissionHandler;
import net.pravian.aero.component.PluginComponent;
import net.pravian.aero.plugin.AeroLogger;
import net.pravian.aero.plugin.AeroPlugin;
import net.pravian.aero.util.Reflection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Represents a handler for executing commands.
 *
 * @param <T extends Plugin> Optional Type-safety (Plugin instance) that the command can use to get the original Plugin instance.
 */
public class AeroCommandHandler<T extends AeroPlugin<T>> extends PluginComponent<T> {

    private final Map<String, AeroCommandExecutor<T>> commands = Maps.newHashMap();
    //
    private String superPermission = plugin.getName().toLowerCase() + ".*";
    private String onlyFromPlayerMessage = ChatColor.YELLOW + "Only players may execute that command.";
    private String onlyFromConsoleMessage = ChatColor.YELLOW + "That command can only be executed from console.";
    private String invalidArgumentLengthMessage = ChatColor.RED + "Invalid argument length!";
    private String invalidArgumentMessage = ChatColor.RED + "Invalid argument: <arg>";
    private String permissionMessage = ChatColor.RED + "You don't have permission to use that command.";
    private String commandClassPrefix = "Command";
    private PermissionHandler permissionHandler = null;

    /**
     * Creates a new instance of BukkitCommandHandler with the specified plugin.
     *
     * @param plugin The plugin instance.
     */
    public AeroCommandHandler(T plugin) {
        this(plugin, plugin.getPluginLogger());
    }

    /**
     * Creates a new instance of BukkitCommandHandler with the specified plugin and logger.
     *
     * @param plugin The plugin instance.
     * @param logger The logger to send error messages to.
     */
    public AeroCommandHandler(T plugin, AeroLogger logger) {
        super(plugin, logger);
    }

    public void clearCommands() {
        commands.clear();
    }

    @SuppressWarnings("unchecked")
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

            if (!CommandBase.class.isAssignableFrom(clazz)) {
                logger.debug("Skipping class in command package: " + info.getSimpleName() + ". Class does can not be assigned to CommandBase.");
                continue;
            }

            CommandBase<T> command;
            try {
                command = (CommandBase<T>) clazz.newInstance();
            } catch (Exception ex) {
                plugin.handleException("Could not instantiate command class: " + info.getSimpleName(), ex);
                continue;
            }

            final AeroCommandExecutor<T> executor = new AeroCommandExecutor<T>(this, name, command);
            commands.put(name, executor);
        }
    }

    public boolean registerAll() {
        return registerAll(plugin.getName());
    }

    public boolean registerAll(String fallbackPrefix) {
        CommandMap map = getCommandMap();
        if (map == null) {
            logger.severe("Could not register commands to command map. Could not find command map!");
            return false;
        }

        Constructor<PluginCommand> cons;
        try {
            cons = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            cons.setAccessible(true);
        } catch (Exception ex) {
            logger.severe("Could not register commands. Could not obtain constructor for PluginCommand!");
            logger.severe(ex);
            return false;
        }

        for (String name : commands.keySet()) {
            final AeroCommandExecutor<T> command = commands.get(name);
            final CommandOptions options = command.getOptions();

            PluginCommand pluginCommand;
            try {
                // Instantiate command
                pluginCommand = cons.newInstance(name, plugin);
            } catch (Exception ex) {
                logger.severe("Could not register command: " + name + ". Could not instantiate PluginCommand instance!");
                logger.severe(ex);
                continue;
            }

            pluginCommand.setUsage(options.usage());
            pluginCommand.setDescription(options.description());
            pluginCommand.setAliases(Arrays.asList(options.aliases().split(",")));
            pluginCommand.setExecutor(command);
            pluginCommand.setTabCompleter(command);

            if (!map.register(fallbackPrefix, pluginCommand)) {
                logger.warning("Could not register command: " + pluginCommand.getName() + ". (is this command already registered?)");
            }
        }
        return true;
    }

    /**
     * Sets the super permission for this command handler.
     *
     * <p>
     * Optional: Defaults to <b>[lowercase pluginname].*<b></p>
     *
     * <p>
     * The super permission indicates an override for players. Any sender who has this permission will be able to run all commands executed by this PluginCommandHandler provided the source type is
     * correct.</p>
     *
     * @param superPermission The super permission to set.
     */
    public void setSuperPermission(String superPermission) {
        this.superPermission = superPermission;
    }

    /**
     * Returns the super permission set for this command handler.
     *
     * @return The super permission set.
     * @see #setSuperPermission(java.lang.String)
     */
    public String getSuperPermission() {
        return superPermission;
    }

    /**
     * Returns the current prefix for all commands.
     *
     * @return The current prefix.
     */
    public String getCommandClassPrefix() {
        return commandClassPrefix;
    }

    /**
     * Sets the prefix every command has.
     *
     * <p>
     * Optional: Defaults to <b>Command_<b></p>
     *
     * <p>
     * Example: Command_testing.java</p>
     *
     * @param commandClassPrefix The new prefix.
     * @see #getCommandPrefix()
     */
    public void setCommandClassPrefix(String commandClassPrefix) {
        this.commandClassPrefix = commandClassPrefix;
    }

    /**
     * Sets a new no-permission message.
     *
     * <p>
     * Optional: Defaults to <b>ChatColor.RED + "You don't have permission to use that command."<b></p>
     *
     * @param message The new no-permission message.
     */
    public void setPermissionMessage(String message) {
        this.permissionMessage = message;
    }

    /**
     * Returns the current no-permission message.
     *
     * @return The no-permission message.
     * @see #setPermissionMessage(String)
     */
    public String getPermissionMessage() {
        return permissionMessage;
    }

    /**
     * Returns the current console-only message.
     *
     * @return The console-only message.
     * @see #setOnlyConsoleMessage(String)
     */
    public String getOnlyConsoleMessage() {
        return onlyFromConsoleMessage;
    }

    /**
     * Sets the new console-only message.
     *
     * <p>
     * Optional: Defaults to <b>ChatColor.YELLOW + "That command can only be executed from console."<b></p>
     *
     * @param message The new console-only message.
     */
    public void setOnlyConsoleMessage(String message) {
        this.onlyFromConsoleMessage = message;
    }

    /**
     * Returns the current game-only message.
     *
     * @return The game-only message.
     * @see #setOnlyGameMessage(String)
     */
    public String getOnlyPlayerMessage() {
        return onlyFromPlayerMessage;
    }

    /**
     * Sets the game-only message.
     *
     * <p>
     * Optional: Defaults to <b>ChatColor.YELLOW + "Only players may execute that command."<b></p>
     *
     * @param message The new game-only message.
     */
    public void setOnlyGameMessage(String message) {
        this.onlyFromPlayerMessage = message;
    }

    /**
     * Returns the current set permission holder.
     *
     * @return The PermissionHolder or null if none has been set.
     */
    public PermissionHandler getPermissionHandler() {
        return permissionHandler;
    }

    public void setPermissionHandler(PermissionHandler handler) {
        this.permissionHandler = handler;

        if (handler != null) {
            handler.initPermissions();
        }
    }

    // TODO: Docs docs docs
    public String getInvalidArgumentLengthMessage() {
        return invalidArgumentLengthMessage;
    }

    public void setInvalidArgumentLengthMessage(String invalidArgumentLengthMessage) {
        this.invalidArgumentLengthMessage = invalidArgumentLengthMessage;
    }

    public String getInvalidArgumentMessage() {
        return invalidArgumentMessage;
    }

    public void setInvalidArgumentMessage(String invalidArgumentMessage) {
        this.invalidArgumentMessage = invalidArgumentMessage;
    }

    @SuppressWarnings("unchecked")
    private CommandMap getCommandMap() {
        PluginManager manager = plugin.getServer().getPluginManager();

        try {
            Object commandMap = Reflection.getField(manager, "commandMap");
            return commandMap instanceof CommandMap ? (CommandMap) commandMap : null;
        } catch (Exception ignored) {
        }

        return null;
    }
}

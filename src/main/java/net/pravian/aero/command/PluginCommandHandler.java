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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import net.pravian.aero.component.PluginComponent;
import net.pravian.aero.plugin.AeroLogger;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Represents a handler for executing commands.
 *
 * @param <T extends Plugin> Optional Type-safety (Plugin instance) that the command can use to get the original Plugin instance.
 */
public class PluginCommandHandler<T extends AeroPlugin<T>> extends PluginComponent<T> {

    private final Map<String, CommandEntry<T>> executors;
    private String superPermission;
    private String onlyFromPlayerMessage;
    private String onlyFromConsoleMessage;
    private String invalidArgumentLengthMessage;
    private String invalidArgumentMessage;
    private String permissionMessage;
    private String commandPrefix;
    private PluginPermissionContainer permissionContainer;
    private String commandPath;

    /**
     * Creates a new instance of BukkitCommandHandler with the specified plugin.
     *
     * @param plugin The plugin instance.
     */
    public PluginCommandHandler(T plugin) {
        this(plugin, plugin.getPluginLogger());
    }

    /**
     * Creates a new instance of BukkitCommandHandler with the specified plugin and logger.
     *
     * @param plugin The plugin instance.
     * @param logger The logger to send error messages to.
     */
    public PluginCommandHandler(T plugin, AeroLogger logger) {
        super(plugin);
        this.executors = new HashMap<String, CommandEntry<T>>();
        this.superPermission = plugin.getName().toLowerCase() + ".*";
        this.onlyFromPlayerMessage = ChatColor.YELLOW + "Only players may execute that command.";
        this.onlyFromConsoleMessage = ChatColor.YELLOW + "That command can only be executed from console.";
        this.permissionMessage = ChatColor.RED + "You don't have permission to use that command.";
        this.invalidArgumentLengthMessage = ChatColor.RED + "Invalid argument length!";
        this.invalidArgumentMessage = ChatColor.RED + "Invalid argument: <arg>";
        this.commandPrefix = "Command";
        this.permissionContainer = null;
        this.commandPath = null;
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
     * Sets the location of the commands this PluginCommandHandler is handling.
     *
     * <p>
     * <b>Required</b>!</p>
     *
     * <p>
     * This method <b>must</b> be called before calling {@link #handleCommand(CommandSender, Command, String, String[])}
     *
     * @param commandLocation The location of this command.
     */
    public void setCommandLocation(Package commandLocation) {
        this.commandPath = commandLocation.getName();
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
     * @param prefix The new prefix.
     * @see #getCommandPrefix()
     */
    public void setCommandPrefix(String prefix) {
        this.commandPrefix = prefix;
    }

    /**
     * Returns the current prefix for all commands.
     *
     * @return The current prefix.
     */
    public String getCommandPrefix() {
        return commandPrefix;
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
     * Returns the current console-only message.
     *
     * @return The console-only message.
     * @see #setOnlyConsoleMessage(String)
     */
    public String getOnlyConsoleMessage() {
        return onlyFromConsoleMessage;
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
     * Returns the current game-only message.
     *
     * @return The game-only message.
     * @see #setOnlyGameMessage(String)
     */
    public String getOnlyPlayerMessage() {
        return onlyFromPlayerMessage;
    }

    public void setPermissionHolder(PluginPermissionContainer holder) {
        this.permissionContainer = holder;

        if (holder != null) {
            holder.initPermissions();
        }
    }

    /**
     * Returns the current set permission holder.
     *
     * @return The PermissionHolder or null if none has been set.
     */
    public PluginPermissionContainer getPermissionContainer() {
        return permissionContainer;
    }

    // TODO: Docs docs docs
    public String getInvalidArgumentLengthMessage() {
        return invalidArgumentLengthMessage;
    }

    public String getInvalidArgumentMessage() {
        return invalidArgumentMessage;
    }

    public void setInvalidArgumentLengthMessage(String invalidArgumentLengthMessage) {
        this.invalidArgumentLengthMessage = invalidArgumentLengthMessage;
    }

    public void setInvalidArgumentMessage(String invalidArgumentMessage) {
        this.invalidArgumentMessage = invalidArgumentMessage;
    }

    /**
     * Returns the BukkitLogger associated with this handler.
     *
     * @return The Logger.
     */
    public AeroLogger getLogger() {
        return logger;
    }

    /**
     * Handles the execution of a command.
     *
     * @param sender The CommandSender executing the command.
     * @param command The command being executed.
     * @param commandLabel The exact command being used.
     * @param args The arguments to the command.
     * @return true/false depending if the command executed successfully.
     * @see BukkitCommand#run(CommandSender, Command, String, String[])
     */
    @SuppressWarnings("unchecked")
    public boolean handleCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        CommandEntry<T> container = executors.get(command.getName().toLowerCase());

        if (container == null) {

            // Instantiate command
            PluginCommand<T> inst;
            try {
                inst = (PluginCommand<T>) PluginCommandHandler.class.getClassLoader().loadClass(
                        String.format("%s.%s%s", commandPath, commandPrefix, command.getName().toLowerCase())).newInstance();
            } catch (Exception ex) {
                plugin.handleException("Could not load command: " + command.getName(), ex);
                sender.sendMessage(ChatColor.RED + "Command Error: Command  " + command.getName() + " not loaded!");
                return true;
            }

            // Setup
            inst.setup(this);

            // Find command method
            // TODO: multiple command receivers?
            CommandOptions annotation = null;
            Method method = null;
            for (Method loopMethod : inst.getClass().getMethods()) {
                try {
                    annotation = loopMethod.getAnnotation(CommandOptions.class);
                    method = loopMethod;
                    break;
                } catch (NullPointerException ignored) {
                }
            }

            if (annotation == null) {
                plugin.handleException("Malformed command: Command " + command.getName() + " contains no viable command methods!");
                sender.sendMessage(ChatColor.RED + "Command Error: Command  " + command.getName() + " is malformed!");
                return true;
            }

            // Wrap command
            container = new CommandEntry<T>(this, annotation, inst, method);

            // Store wrapped command
            executors.put(command.getName().toLowerCase(), container);
        }

        try {
            return container.onCommand(sender, command, commandLabel, args);
        } catch (Exception ex) {
            // If this is ever ran, Aero failed :C
            plugin.handleException("Unhandled command exception: " + command.getName(), ex);
            sender.sendMessage(ChatColor.RED + "Unhandled Command Error: " + command.getName());
            return true;
        }
    }

}

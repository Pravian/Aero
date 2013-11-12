package net.pravian.bukkitlib.command;

import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Represents a handler for executing commands.
 * 
 * @param <T extends Plugin> Optional Type-safety (Plugin instance) that the command can use to get the original Plugin instance.
 */
public class BukkitCommandHandler<T extends Plugin> {

    private T plugin;
    private String commandPath;
    private BukkitPermissionHandler permissionHandler = null;
    private String commandPrefix = "Command_";
    private String permissionMessage = ChatColor.RED + "You don't have permission to use that command.";
    private String onlyFromConsoleMessage = ChatColor.YELLOW + "That command can only be executed from console.";
    private String onlyFromGameMessage = ChatColor.YELLOW + "Only players may execute that command.";

    /**
     * Creates a new BukkitCommandHandler with the specified plugin.
     * 
     * @param plugin The plugin instance.
     */
    public BukkitCommandHandler(T plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Sets the location of the commands this CommandHandler is handling.
     * 
     * <p><b>Required</b>!</p>
     * <p>This method <b>must</b> be called before calling {@link #handleCommand(CommandSender, Command, String, String[])}
     * 
     * @param commandLocation The location of this command.
     */
    public void setCommandLocation(Package commandLocation) {
        this.commandPath = commandLocation.getName();
    }

    /**
     * Sets the prefix every command has.
     * 
     * <p>Optional: Defaults to <b>Command_<b></p>
     * 
     * <p>Example: Command_testing.java</p>
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
     * <p>Optional: Defaults to <b>ChatColor.RED + "You don't have permission to use that command."<b></p>
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
     * <p>Optional: Defaults to <b>ChatColor.YELLOW + "That command can only be executed from console."<b></p>
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
     * <p>Optional: Defaults to <b>ChatColor.YELLOW + "Only players may execute that command."<b></p>
     * 
     * @param message The new game-only message.
     */
    public void setOnlyGameMessage(String message) {
        this.onlyFromGameMessage = message;
    }
    
    /**
     * Returns the current game-only message.
     * 
     * @return The game-only message.
     * @see #setOnlyGameMessage(String) 
     */
    public String getOnlyGameMessage() {
        return onlyFromGameMessage;
    }
    
    /**
     * Sets a new PermissionHandler to handle command permissions.
     * 
     * <p>Optionally, set to null to return to annotation-based permission handling.</p>
     * 
     * @param handler The new PermissionHandler / null
     * @see BukkitPermissionHandler
     */
    public void setPermissionHandler(BukkitPermissionHandler handler) {
        this.permissionHandler = handler;
    }
    
    /**
     * Returns the BukkitPermissionHandler which handles command permissions.
     * 
     * <p>Might return null if no BukkitPermissionHandler has been set.
     * 
     * @return BukkitPermissionHandler instance / null 
     * @see #getPermissionHandler()
     */
    public BukkitPermissionHandler getPermissionHandler() {
        return permissionHandler;
        
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
    public boolean handleCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        BukkitCommand<T> dispatcher;

        try {
            dispatcher = (BukkitCommand) BukkitCommandHandler.class.getClassLoader().loadClass(
                    String.format("%s.%s%s", commandPath, commandPrefix, command.getName().toLowerCase())).newInstance();
            dispatcher.setup(this, plugin, sender, command, dispatcher.getClass());
        } catch (Throwable ex) {
            LoggerUtils.severe(plugin, "Command not loaded: " + command.getName());
            LoggerUtils.severe(plugin, ex);
            sender.sendMessage(ChatColor.RED + "Command Error: Command not loaded: " + command.getName());
            return true;
        }

        try {
            if (dispatcher.checkPermissions()) {
                return dispatcher.run(sender, command, commandLabel, args);
            }
        } catch (Throwable ex) {
            LoggerUtils.severe(plugin, "Command Error: " + commandLabel);
            LoggerUtils.severe(plugin, ex);
            sender.sendMessage(ChatColor.RED + "Command Error:  " + command.getName());
        }
        return true;
    }
}

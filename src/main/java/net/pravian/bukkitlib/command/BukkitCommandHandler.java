package net.pravian.bukkitlib.command;

import net.pravian.bukkitlib.InternalExceptionHandler;
import net.pravian.bukkitlib.implementation.BukkitLogger;
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

    private final T plugin;
    private final BukkitLogger logger;
    private String commandPath;
    private String superPermission;
    private BukkitPermissionHolder permissionHolder;
    private String commandPrefix;
    private String permissionMessage;
    private String onlyFromConsoleMessage;
    private String onlyFromGameMessage;

    /**
     * Creates a new instance of BukkitCommandHandler with the specified plugin.
     *
     * @param plugin The plugin instance.
     */
    public BukkitCommandHandler(T plugin) {
        this(plugin, new BukkitLogger(plugin));
    }

    /**
     * Creates a new instance of BukkitCommandHandler with the specified plugin and logger.
     *
     * @param plugin The plugin instance.
     * @param logger The logger to send error messages to.
     */
    public BukkitCommandHandler(T plugin, BukkitLogger logger) {
        this.onlyFromGameMessage = ChatColor.YELLOW + "Only players may execute that command.";
        this.onlyFromConsoleMessage = ChatColor.YELLOW + "That command can only be executed from console.";
        this.permissionMessage = ChatColor.RED + "You don't have permission to use that command.";
        this.commandPrefix = "Command_";
        this.permissionHolder = null;
        this.plugin = plugin;
        this.logger = logger;
        this.superPermission = plugin.getName().toLowerCase() + ".*";
    }

    /**
     * Sets the super permission for this command handler.
     *
     * <p>Optional: Defaults to <b>[lowercase pluginname].*<b></p>
     *
     * <p>The super permission indicates an override for players. Any sender who has this permission will be able to run all commands executed by this CommandHandler provided the source type
     * is correct.</p>
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
     * Sets the location of the commands this CommandHandler is handling.
     *
     * <p><b>Required</b>!</p>
     *
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

    public void setPermissionHolder(BukkitPermissionHolder holder) {
        this.permissionHolder = holder;

        if (holder != null) {
            holder.initPermissions();
        }
    }

    public BukkitPermissionHolder getPermissionHolder() {
        return permissionHolder;
    }

    /**
     * Returns the plugin associated with this handler.
     *
     * @return The Plugin
     */
    public T getPlugin() {
        return plugin;
    }

    /**
     * Returns the BukkitLogger associated with this handler.
     *
     * @return The Logger
     */
    public BukkitLogger getLogger() {
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
    @SuppressWarnings({"unchecked"})
    public boolean handleCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        final BukkitCommand<T> dispatcher;

        try {
            dispatcher = (BukkitCommand) BukkitCommandHandler.class.getClassLoader().loadClass(
                    String.format("%s.%s%s", commandPath, commandPrefix, command.getName().toLowerCase())).newInstance();

            dispatcher.setup(this, plugin, logger, sender, command, commandLabel, args, (Class<? extends BukkitCommand<T>>) dispatcher.getClass());

        } catch (Exception ex) {
            InternalExceptionHandler.handle(plugin, "Command not loaded: " + command.getName());
            InternalExceptionHandler.handle(plugin, ex);
            sender.sendMessage(ChatColor.RED + "Command Error: Command  " + command.getName() + " not loaded!");
            return true;
        }

        try {
            if (dispatcher.checkPermissions()) {
                return dispatcher.execute();
            }
        } catch (RuntimeException ex) {
            InternalExceptionHandler.handle(plugin, "Command Error: " + commandLabel);
            InternalExceptionHandler.handle(plugin, ex);
            sender.sendMessage(ChatColor.RED + "Command Error:  " + command.getName());
        }
        return true;
    }
}

package net.pravian.bukkitlib.command;

import net.pravian.bukkitlib.implementation.PluginLogger;
import net.pravian.bukkitlib.util.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Represents a BukkitLib command.
 * 
 * @param <T> Optional: Type safety for {@link #plugin}
 */
public abstract class BukkitCommand<T extends Plugin> {

    /**
     * Represents the plugin handling the command.
     */
    protected T plugin;
    /**
     * Represents the CraftBukkit server.
     */
    protected Server server;
    /**
     * Represents the player sending the command.
     * 
     * <p><b>Warning</b>: Might be null if the console is sending the command.</p>
     * @deprecated Getting rid of duplicate values.
     */
    @Deprecated
    protected Player commandSenderPlayer;
    /**
     * Represents the PLuginLogger used.
     */
    protected PluginLogger logger;
    
    private BukkitCommandHandler handler;
    private CommandSender commandSender;
    private Command command;
    private String commandLabel;
    private String[] args;
    private Class<?> commandClass;
    private String usage;

    /**
     * Sets up a BukkitCommand environment.
     * 
     * <p>Every instance should call this before executing any other method.</p>
     * 
     * @param handler The BukkitCommandHander from which the command is being executed.
     * @param plugin The plugin handling the command.
     * @param logger The logger used to log data.
     * @param sender The CommandSender executing the command.
     * @param command The command being executed.
     * @param commandLabel The commandLabel for this command.
     * @param args The arguments for this command.
     * @param commandClass The class representing the command.
     */
    public void setup(
            final BukkitCommandHandler handler,
            final T plugin,
            final PluginLogger logger,
            final CommandSender sender,
            final Command command,
            final String commandLabel,
            final String[] args,
            final Class<? extends BukkitCommand> commandClass) {
        this.handler = handler;
        this.plugin = plugin;
        this.logger = logger;
        this.server = plugin.getServer();
        this.commandSender = sender;
        this.command = command;
        this.commandLabel = commandLabel;
        this.args = args;
        this.commandClass = commandClass;

        if (sender instanceof Player) {
            this.commandSenderPlayer = (Player) sender;
        }
    }

    /**
     * Executed when the command is being ran.
     * 
     * <p><b>In normal conditions, this should never be ran.</b></p>
     * 
     * @param sender The CommandSender who is executing the command.
     * @param command The command which is being executed.
     * @param commandLabel The exact command being used.
     * @param args The arguments to the command.
     * @return true/false depending if the command executed successfully.
     */
    abstract public boolean run(final CommandSender sender, final Command command, final String commandLabel, final String[] args);

    /**
     * Executes the command.
     * 
     * <p><b>In normal conditions, this should never be ran.</b></p>
     * 
     * @return true/false depending if the command executed successfully.
     */
    protected boolean execute() {
        return run(commandSender, command, commandLabel, args);
    }
    
    /**
     * Checks if the CommandSender has permissions to run this command.
     * 
     * <p>Validates if the player has the "permission" value from the CommandPermission annotation.
     * If a custom BukkitPermissionHandler has been registered, it will use that instead.
     * Regardless of which permission handler is used, the SourceType will always be checked.
     * If the CommandSender does not have permission to use the command, a no-permission message will be sent.</p>
     * 
     * @return true if the CommandSender has permission to use this command.
     */
    public boolean checkPermissions() {
        final CommandPermissions permissions = commandClass.getAnnotation(CommandPermissions.class);
        
        if (permissions == null) {
            logger.warning(commandClass.getName() + " is missing permissions annotation.");
            this.usage = "";
            return true;
        }
        
        this.usage = permissions.usage();
        
        final String permission = permissions.permission();
        final SourceType source = permissions.source();
        
        if (source == SourceType.PLAYER && isConsole()) {
            commandSender.sendMessage(handler.getOnlyGameMessage());
            return false;
        }
        
        if (source == SourceType.CONSOLE && !isConsole()) {
            commandSender.sendMessage(handler.getOnlyConsoleMessage());
            return false;
        }
        
        if (isConsole() || "".equals(permission)) {
            return true;
        }
        
        if (handler.getPermissionHandler() != null) {
            final boolean result = handler.getPermissionHandler().hasPermission(commandSender, command, args);
            
            if (!result) {
                commandSender.sendMessage(handler.getPermissionMessage());
            }
            return result;
        }
        
        if (permission == null || permission.equals("")) {
            return true;
        }
        
        if (!((Player) commandSender).hasPermission(permission)) {
            commandSender.sendMessage(handler.getPermissionMessage());
            return false;
        }
        
        return true;
        
    }

    /**
     * Validates if the sender of the command is not a player.
     * 
     * @return true if the CommandSender is not a Player.
     */
    public boolean isConsole() {
        return !(commandSender instanceof Player);
    }
    
    /**
     * Sends the sender of the command a no-permissions message.
     * 
     * <p><b>This method uses the quick-return syntax for ease of use.</b></p>
     * <p>Example:
     * <pre>
     * if (target.hasPermission("plugin.kickexempt")) {
     *     return noPerms();
     * }
     * </pre></p>
     * 
     * @return true
     */
    public boolean noPerms() {
        msg(handler.getPermissionMessage());
        return true;
    }
    
    /**
     * Sends the sender the usage of this command.
     * 
     * <p><b>This method uses the quick-return syntax for ease of use.</b></p>
     * 
     * @see #noPerms()
     * @return true
     */
    public boolean showUsage() {
        if (usage.equals("")) {
            return false;
        }
        msg(usage.replaceAll("<command>", command.getLabel()));
        return true;
    }
    
    /**
     * Sends the sender of this command a message (in Gray).
     * 
     * @param message The message to send.
     * @see #msg(CommandSender, String, ChatColor)
     */
    public void msg(final String message) {
        msg(commandSender, message);
    }

    /**
     * Sends a message to a sender (in Gray).
     * 
     * @param receiver The CommandSender to whom the message must be sent.
     * @param message The message to send.
     * @see #msg(CommandSender, String, ChatColor)
     */
    public void msg(final CommandSender receiver, final String message) {
        msg(receiver, message, ChatColor.GRAY);
    }

    /**
     * Sends the sender of this command a message.
     * 
     * @param message The message to send.
     * @param color The color in which the message must be sent.
     * @see #msg(CommandSender, String, ChatColor)
     */
    public void msg(final String message, final ChatColor color) {
        msg(commandSender, message, color);
    }

     /**
     * Sends a message to a CommandSender.
     * 
     * @param receiver The CommandSender to which the message must be sent.
     * @param message The message to send.
     * @param color The color in which the message must be sent.
     */
    public void msg(final CommandSender receiver, final String message, final ChatColor color) {
        if (receiver == null) {
            return;
        }
        receiver.sendMessage(color + message);
    }
    
    /**
     * Searches and returns an online player by (partial)name.
     * 
     * <p>Uses {@link PlayerUtils#getPlayer(String)}.</p>
     * 
     * @param name
     * @return The player that has been found (<b>Or null if the player could not be found!</b>)
     * @see PlayerUtils#getPlayer(String)
     */
    public Player getPlayer(final String name) {
        return PlayerUtils.getPlayer(name);
    }
    
    /**
     * Searches and returns an offline or online player by (partial)name.
     * 
     * <p>Uses {@link net.pravian.bukkitlib.util.PlayerUtils#getOfflinePlayer(String)}.</p>
     * 
     * @param name
     * @return The OfflinePlayer that has been found (<b>Or null if the player could not be found!</b>)
     * @see PlayerUtils#getOfflinePlayer(String)
     */
    public OfflinePlayer getOfflinePlayer(final String name) {
        return PlayerUtils.getOfflinePlayer(name);
    }
}

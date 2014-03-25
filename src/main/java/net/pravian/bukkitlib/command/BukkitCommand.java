package net.pravian.bukkitlib.command;

import java.util.Set;
import net.pravian.bukkitlib.implementation.BukkitLogger;
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
     */
    protected Player playerSender;
    /**
     * Represents the PLuginLogger used.
     */
    protected BukkitLogger logger;
    //
    private BukkitCommandHandler<?> handler;
    private CommandSender commandSender;
    private Command command;
    private String commandLabel;
    private String[] args;
    private Class<? extends BukkitCommand<T>> commandClass;
    private String usage;

    /**
     * Creates a new BukkitCommand instance.
     *
     * <p><b>In normal conditions, you should never create BukkitCommand instances</b></p>
     */
    protected BukkitCommand() {
    }

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
    protected void setup(
            final BukkitCommandHandler<?> handler,
            final T plugin,
            final BukkitLogger logger,
            final CommandSender sender,
            final Command command,
            final String commandLabel,
            final String[] args,
            final Class<? extends BukkitCommand<T>> commandClass) {
        this.handler = handler;
        this.plugin = plugin;
        this.logger = logger;
        this.server = plugin.getServer();
        this.commandSender = sender;
        this.command = command;
        this.usage = command.getUsage();
        this.commandLabel = commandLabel;
        this.args = args;
        this.commandClass = commandClass;

        if (sender instanceof Player) {
            this.playerSender = (Player) sender;
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
    abstract protected boolean run(final CommandSender sender, final Command command, final String commandLabel, final String[] args);

    /**
     * Executed when the command finishes execution.
     *
     * <p><b>In normal conditions, this should never be ran.</b></p>
     */
    protected void reset() {
    }

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
     * <p>Validates if the player has the "permission" value from the CommandPermission annotation. If a custom BukkitPermissionHandler has been registered, it will use that instead.
     * Regardless of which permission handler is used, the SourceType will always be checked. If the CommandSender does not have permission to use the command, a no-permission message will
     * be sent.</p>
     *
     * @return true if the CommandSender has permission to use this command.
     */
    @SuppressWarnings("deprecation")
    protected boolean checkPermissions() {
        final CommandPermissions permissions = commandClass.getAnnotation(CommandPermissions.class);

        if (permissions == null) {
            logger.warning(commandClass.getName() + " is missing permissions annotation!");
            this.usage = "";
            return true;
        }

        final String permission = permissions.permission();
        final SourceType source = permissions.source();

        // Validate source
        if (source == SourceType.PLAYER && isConsole()) {
            commandSender.sendMessage(handler.getOnlyGameMessage());
            return false;
        }

        if (source == SourceType.CONSOLE && !isConsole()) {
            commandSender.sendMessage(handler.getOnlyConsoleMessage());
            return false;
        }

        // If Console, always allow
        if (isConsole()) {
            return true;
        }

        // super permission?
        if (handler.getSuperPermission() != null && !handler.getSuperPermission().isEmpty()) {
            if (commandSender.hasPermission(handler.getSuperPermission())) {
                return true;
            }
        }

        // PermissionHolder?
        if (handler.getPermissionHolder() != null) {
            final Set<String> handlerPermissions = handler.getPermissionHolder().getPermissions(commandClass);

            if (handlerPermissions != null) {
                for (String handlerPermission : handlerPermissions) {
                    if (commandSender.hasPermission(handlerPermission)) {
                        return true;
                    }
                }

                commandSender.sendMessage(handler.getPermissionMessage());
                return false;
            }
        }

        // Annotations?
        if (permission != null && !permission.isEmpty()) {
            final boolean result = ((Player) commandSender).hasPermission(permission);

            if (!result) {
                commandSender.sendMessage(handler.getPermissionMessage());
            }
            return result;
        }

        // Default to true
        return true;
    }

    protected Class<? extends BukkitCommand<T>> getCommandClass() {
        return commandClass;
    }

    /**
     * Validates if the sender of the command is not a player.
     *
     * @return true if the CommandSender is not a Player.
     */
    protected boolean isConsole() {
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
    protected boolean noPerms() {
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
    protected boolean showUsage() {
        if (usage.isEmpty()) {
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
    protected void msg(final BukkitMessage message) {
        msg(message.getMessage());
    }

    /**
     * Sends the sender of this command a message (in Gray).
     *
     * @param message The message to send.
     * @see #msg(CommandSender, String, ChatColor)
     */
    protected void msg(final String message) {
        msg(commandSender, message);
    }

    /**
     * Sends a message to a sender (in Gray).
     *
     * @param receiver The CommandSender to whom the message must be sent.
     * @param message The message to send.
     * @see #msg(CommandSender, String, ChatColor)
     */
    protected void msg(final CommandSender receiver, final String message) {
        msg(receiver, message, ChatColor.GRAY);
    }

    /**
     * Sends the sender of this command a message.
     *
     * @param message The message to send.
     * @param color The color in which the message must be sent.
     * @see #msg(CommandSender, String, ChatColor)
     */
    protected void msg(final String message, final ChatColor color) {
        msg(commandSender, message, color);
    }

    /**
     * Sends a message to a CommandSender.
     *
     * @param receiver The CommandSender to which the message must be sent.
     * @param message The message to send.
     * @param color The color in which the message must be sent.
     */
    protected void msg(final CommandSender receiver, final String message, final ChatColor color) {
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
    protected Player getPlayer(final String name) {
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
    protected OfflinePlayer getOfflinePlayer(final String name) {
        return PlayerUtils.getOfflinePlayer(name);
    }
}

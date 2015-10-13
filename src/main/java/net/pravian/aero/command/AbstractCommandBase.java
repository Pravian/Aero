package net.pravian.aero.command;

import java.util.List;
import net.pravian.aero.base.PluginMessage;
import net.pravian.aero.command.handler.AeroCommandHandler;
import net.pravian.aero.command.handler.SimpleCommandHandler;
import net.pravian.aero.component.PluginComponent;
import net.pravian.aero.plugin.AeroPlugin;
import net.pravian.aero.util.Players;
import net.pravian.aero.util.Plugins;
import net.pravian.aero.util.Worlds;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class AbstractCommandBase<T extends AeroPlugin<T>> extends PluginComponent<T> implements AeroCommandBase<T> {

    // Default arguments
    protected CommandSender sender;
    protected Command command;
    protected String label;
    protected String[] args;
    /**
     * Represents the player sending the command.
     *
     * <p>
     * <b>Note</b>: Might be null if the console is sending the command.</p>
     */
    protected Player playerSender;
    //
    private AeroCommandHandler<T> handler = null;

    protected AbstractCommandBase() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends AeroCommandBase<T>> getCommandClass() {
        return (Class<? extends AeroCommandBase<T>>) getClass();
    }

    @Override
    public void register(SimpleCommandHandler<T> handler) throws CommandRegistrationException {
        if (this.handler != null) {
            throw new CommandRegistrationException("Command already registered to a handler!");
        }

        this.handler = handler;
    }

    @Override
    public void unregister() {
        handler = null;
    }

    @Override
    public AeroCommandHandler<T> getHandler() {
        return handler;
    }

    protected void setVariables(final CommandSender sender, final Command command, final String label, final String[] args) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;

        if (sender instanceof Player) {
            this.playerSender = (Player) sender;
        } else {
            this.playerSender = null;
        }
    }

    @Override
    public void onInit() { // Called when the command is initialised
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    /**
     * Validates if the sender of the command is not a player.
     *
     * @return true if the CommandSender is not a Player.
     */
    protected boolean isConsole() {
        return !(sender instanceof Player);
    }

    /**
     * Sends the sender of the command a no-permissions message.
     *
     * <p>
     * <b>This method uses the quick-return syntax for ease of use.</b></p>
     * <p>
     * Example:
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
     * <p>
     * <b>This method uses the quick-return syntax for ease of use.</b></p>
     *
     * @see #noPerms()
     * @return true
     */
    protected boolean showUsage() {
        msg(command.getUsage().replaceAll("<command>", command.getLabel()));
        return true;
    }

    /**
     * Sends the sender of this command a message (in Gray).
     *
     * @param message The message to send.
     * @see #msg(CommandSender, String, ChatColor)
     */
    protected void msg(final PluginMessage message) {
        msg(message.getMessage());
    }

    /**
     * Sends the sender of this command a message (in Gray).
     *
     * @param message The message to send.
     * @see #msg(CommandSender, String, ChatColor)
     */
    protected void msg(final String message) {
        msg(sender, message);
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
        msg(sender, message, color);
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
     * <p>
     * Uses {@link PlayerUtils#getPlayer(String)}.</p>
     *
     * @param name
     * @return The player that has been found (<b>Or null if the player could not be found!</b>)
     * @see PlayerUtils#getPlayer(String)
     */
    protected Player getPlayer(final String name) {
        return Players.getPlayer(name);
    }

    /**
     * Searches and returns an offline or online player by (partial)name.
     *
     * <p>
     * Uses {@link net.pravian.bukkitlib.util.PlayerUtils#getOfflinePlayer(String)}.</p>
     *
     * @param name
     * @return The OfflinePlayer that has been found (<b>Or null if the player could not be found!</b>)
     * @see PlayerUtils#getOfflinePlayer(String)
     */
    protected OfflinePlayer getOfflinePlayer(final String name) {
        return Players.getOfflinePlayer(name);
    }

    protected World getWorld(final String world) {
        return Worlds.getWorld(world);
    }

    protected Plugin getPlugin(final String plugin) {
        return Plugins.getPlugin(plugin);
    }

}

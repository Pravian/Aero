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

import net.pravian.aero.command.base.PluginMessage;
import net.pravian.aero.component.PluginComponent;
import net.pravian.aero.plugin.AeroPlugin;
import net.pravian.aero.util.Players;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Represents a plugin command.
 *
 * @param <T> Optional: Type safety for {@link #plugin}
 */
public abstract class PluginCommand<T extends AeroPlugin<T>> extends PluginComponent<T> implements CommandExecutor {

    // Default arguments
    protected CommandSender sender;
    protected Command command;
    protected String label;
    protected String[] args;
    //
    /**
     * Represents the player sending the command.
     *
     * <p>
     * <b>Note</b>: Might be null if the console is sending the command.</p>
     */
    protected Player playerSender;
    //
    private PluginCommandHandler<T> handler;

    // TODO: avoid using this.
    @Deprecated
    protected PluginCommand() {
    }

    protected PluginCommand(T plugin) {
        super(plugin);
    }

    protected void setup(PluginCommandHandler<T> handler) {
        this.handler = handler;
    }

    /**
     * Executed when the command is being ran.
     *
     * <p>
     * <b>In normal conditions, this should never be ran.</b></p>
     *
     * @param sender The CommandSender who is executing the command.
     * @param command The command which is being executed.
     * @param label The exact command being used.
     * @param args The arguments to the command.
     * @return true/false depending if the command executed successfully.
     */
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        this.playerSender = null;

        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;

        if (sender instanceof Player) {
            this.playerSender = (Player) sender;
        }

        throw new CommandUnimplementedException();
    }

    @SuppressWarnings("unchecked")
    public Class<? extends PluginCommand<T>> getCommandClass() {
        return (Class<? extends PluginCommand<T>>) this.getClass();
    }

    /**
     * Executed when the command finishes execution.
     *
     * <p>
     * <b>In normal conditions, this should never be ran.</b></p>
     */
    protected void reset() {
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
        //msg(usage.replaceAll("<command>", command.getLabel()));
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
}

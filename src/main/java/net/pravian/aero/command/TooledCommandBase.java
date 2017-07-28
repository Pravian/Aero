/*
 * Copyright 2015 Pravian Systems.
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

import net.pravian.aero.base.PluginMessage;
import net.pravian.aero.plugin.AeroPlugin;
import net.pravian.aero.util.Players;
import net.pravian.aero.util.Plugins;
import net.pravian.aero.util.Worlds;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class TooledCommandBase<T extends AeroPlugin<T>> extends AbstractCommandBase<T> {

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
        msg(getHandler().getPermissionMessage());
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

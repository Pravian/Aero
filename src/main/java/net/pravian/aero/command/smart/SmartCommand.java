package net.pravian.aero.command.smart;

import net.pravian.aero.command.AbstractCommandBase;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class SmartCommand<T extends AeroPlugin<T>> extends AbstractCommandBase<T> implements CommandExecutor {

    protected SmartCommand() {
    }

    @Override
    public final boolean runCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        setVariables(sender, command, label, args);

        try {
            return onCommand(sender, command, label, args);
        } catch (ParseException ex) {
            String message = ex.getMessage();
            if (message == null) {
                return false;
            }

            sender.sendMessage(ChatColor.RED + message);
            return true;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return false; // Accessing args[index]
        } catch (Exception ex) {
            plugin.handleException("Uncaught exception executing command: " + command.getName(), ex);
            sender.sendMessage(ChatColor.RED + "Command error: " + (ex.getMessage() == null ? "Unknown cause" : ex.getMessage()));
            return true;
        }
    }

    @Override
    protected Player getPlayer(final String name) {
        Player player = super.getPlayer(name);
        if (player == null) {
            throw new ParseException("Could not find player: " + name);
        }
        return player;
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
    @Override
    protected OfflinePlayer getOfflinePlayer(final String name) {
        OfflinePlayer player = super.getOfflinePlayer(name);
        if (player == null) {
            throw new ParseException("Could not find offline player: " + name);
        }
        return player;
    }

    @Override
    protected World getWorld(final String name) {
        World world = super.getWorld(name);
        if (world == null) {
            throw new ParseException("Could not world: " + name);
        }
        return world;
    }

    @Override
    protected Plugin getPlugin(final String name) {
        Plugin findPlugin = super.getPlugin(name);
        if (findPlugin == null) {
            throw new ParseException("Could not find offline player: " + name);
        }
        return findPlugin;
    }

}

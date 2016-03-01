package net.pravian.aero.command.smart;

import net.pravian.aero.command.TooledCommandBase;
import net.pravian.aero.plugin.AeroPlugin;
import net.pravian.aero.util.Players;
import net.pravian.aero.util.Plugins;
import net.pravian.aero.util.Worlds;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class SmartCommand<T extends AeroPlugin<T>> extends TooledCommandBase<T> implements CommandExecutor {

    protected SmartCommand() {
    }

    @Override
    public final boolean runCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        setVariables(sender, command, label, args);

        try {
            return onCommand(sender, command, label, args);
        } catch (ArgumentException ex) {
            boolean value = true;
            if (ex instanceof ReturnException) {
                value = ((ReturnException) ex).isReturnValue();
            }

            String message = ex.getMessage();
            if (message != null) {
                sender.sendMessage(ChatColor.RED + message);
            }

            return value;
        } catch (Exception ex) {
            plugin.handleException("Uncaught exception executing command: " + command.getName(), ex);
            sender.sendMessage(ChatColor.RED + "Command error: " + (ex.getMessage() == null ? "Unknown cause" : ex.getMessage()));
            return true;
        }
    }

    protected Player toPlayer(final String name) {
        Player player = Players.getPlayer(name);
        if (player == null) {
            throw new ArgumentException("Could not find player: " + name);
        }
        return player;
    }

    /**
     * Searches and returns an offline or online toPlayer by (partial)name.
     *
     * <p>
     * Uses {@link net.pravian.bukkitlib.util.PlayerUtils#getOfflinePlayer(String)}.</p>
     *
     * @param name
     * @return The OfflinePlayer that has been found (<b>Or null if the toPlayer could not be found!</b>)
     * @see PlayerUtils#getOfflinePlayer(String)
     */
    protected OfflinePlayer toOfflinePlayer(final String name) {
        OfflinePlayer player = Players.getOfflinePlayer(name);
        if (player == null) {
            throw new ArgumentException("Could not find offline player: " + name);
        }
        return player;
    }

    protected World toWorld(final String name) {
        World world = Worlds.getWorld(name);
        if (world == null) {
            throw new ArgumentException("Could not world: " + name);
        }
        return world;
    }

    protected Plugin toPlugin(final String name) {
        Plugin findPlugin = Plugins.getPlugin(name);
        if (findPlugin == null) {
            throw new ArgumentException("Could not find plugin: " + name);
        }
        return findPlugin;
    }

    protected int toInt(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException ex) {
            throw new ArgumentException("Invalid number: " + arg);
        }
    }

    protected double toDouble(String arg) {
        try {
            return Double.parseDouble(arg);
        } catch (NumberFormatException ex) {
            throw new ArgumentException("Invalid number: " + arg);
        }
    }

    protected void length(int length) {
        if (length != args.length) {
            throw new ReturnException(false);
        }
    }

    protected void minLength(int length) {
        if (length > args.length) {
            throw new ReturnException(false);
        }
    }

    protected void maxLength(int length) {
        if (args.length > length) {
            throw new ReturnException(false);
        }
    }

    protected String concat(String[] params) {
        return StringUtils.join(args, " ");
    }

    protected String concat(String[] params, int begin) {
        return StringUtils.join(args, " ", begin, params.length);
    }

    protected String concat(String[] params, int begin, int end) {
        return StringUtils.join(args, " ", begin, end);
    }

}

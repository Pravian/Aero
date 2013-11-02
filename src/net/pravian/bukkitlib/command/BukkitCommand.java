package net.pravian.bukkitlib.command;

import net.pravian.bukkitlib.BukkitLib;
import net.pravian.bukkitlib.utils.LoggerUtils;
import net.pravian.bukkitlib.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class BukkitCommand {

    protected Plugin plugin;
    protected Server server;
    protected Player commandSenderPlayer;
    private CommandSender commandSender;
    private Class<?> commandClass;
    private String usage;

    public void setup(Plugin plugin, final CommandSender commandSender, final Class<?> commandClass) {
        this.plugin = plugin;
        this.server = plugin.getServer();
        this.commandSender = commandSender;
        this.commandClass = commandClass;

        if (commandSender instanceof Player) {
            this.commandSenderPlayer = (Player) commandSender;
        }
    }

    abstract public boolean run(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args);

    public boolean checkPermissions() {
        CommandPermissions permissions = commandClass.getAnnotation(CommandPermissions.class);

        if (permissions == null) {
            LoggerUtils.warning(plugin, commandClass.getName() + " is missing permissions annotation.");
            return true;
        }
        
        String permission = permissions.permission();
        SourceType source = permissions.source();
        this.usage = permissions.usage();
        
        if (source == SourceType.GAME && isConsole()) {
            commandSender.sendMessage(BukkitCommandHandler.getOnlyGameMessage());
            return false;
        }
        
        if (source == SourceType.CONSOLE && !isConsole()) {
            commandSender.sendMessage(BukkitCommandHandler.getOnlyConsoleMessage());
            return false;
        }
        
        if (isConsole() || "".equals(permission)) {
            return true;
        }
        
        if (!commandSenderPlayer.hasPermission(permission)) {
            commandSender.sendMessage(BukkitCommandHandler.getPermissionMessage());
            return false;
        }
        
        return true;
        
    }

    // Util
    public boolean isConsole() {
        return !(commandSender instanceof Player);
    }
    
    public boolean noPerms() {
        msg(BukkitCommandHandler.getPermissionMessage());
        return true;
    }
    
    public boolean showUsage() {
        if (usage.equals("")) {
            return false;
        }
        msg(usage);
        return true;
    }

    public void msg(final String message) {
        msg(commandSender, message);
    }

    public void msg(final CommandSender receiver, final String message) {
        msg(receiver, message, ChatColor.GRAY);
    }

    public void msg(final String message, final ChatColor color) {
        msg(commandSender, message, color);
    }

    public void msg(final CommandSender receiver, final String message, final ChatColor color) {
        if (receiver == null) {
            return;
        }
        receiver.sendMessage(color + message);
    }
    
    public Player getPlayer(String name) {
        return PlayerUtils.getPlayer(name);
    }
    
    public OfflinePlayer getOfflinePlayer(String name) {
        return PlayerUtils.getOfflinePlayer(name);
    }
}

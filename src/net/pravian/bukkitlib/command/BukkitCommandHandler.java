package net.pravian.bukkitlib.command;

import net.pravian.bukkitlib.BukkitLib;
import net.pravian.bukkitlib.utils.LoggerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class BukkitCommandHandler {

    private static ClassLoader classLoader;
    private static String commandPath;
    //
    private static String commandPrefix = "Command_";
    private static String permissionMessage = ChatColor.RED + "You don't have permission for that command.";
    private static String onlyFromConsoleMessage = ChatColor.YELLOW + "That command can only be executed from console.";
    private static String onlyFromGameMessage = ChatColor.YELLOW + "Only players can execute that command.";

    public static void setup(Class loader, Package commandLocation) {
        classLoader = loader.getClassLoader();
        commandPath = commandLocation.getName();
    }

    public static void setCommandPrefix(String prefix) {
        commandPrefix = prefix;
    }

    public static String getCommandPrefix() {
        return commandPrefix;
    }

    public static void setPermissionMessage(String message) {
        permissionMessage = message;
    }

    public static String getPermissionMessage() {
        return permissionMessage;
    }
    
    public static void setOnlyConsoleMessage(String message) {
        onlyFromConsoleMessage = message;
    }
    
    public static String getOnlyConsoleMessage() {
        return onlyFromConsoleMessage;
    }
    
    public static void setOnlyGameMessage(String message) {
        onlyFromGameMessage = message;
    }
    
    public static String getOnlyGameMessage() {
        return onlyFromGameMessage;
    } 

    public static boolean handleCommand(Plugin plugin, CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (classLoader == null || commandPath == null) {
            return false;
        }

        BukkitCommand dispatcher;

        try {
            dispatcher = (BukkitCommand) classLoader.loadClass(String.format("%s.%s%s", commandPath, commandPrefix, cmd.getName().toLowerCase())).newInstance();
            dispatcher.setup(plugin, sender, dispatcher.getClass());
        } catch (Throwable ex) {
            LoggerUtils.severe(plugin, "Command not loaded: " + cmd.getName());
            LoggerUtils.severe(plugin, ex);
            sender.sendMessage(ChatColor.RED + "Command Error: Command not loaded: " + cmd.getName());
            return true;
        }

        try {
            if (dispatcher.checkPermissions()) {
                return dispatcher.run(sender, cmd, commandLabel, args);
            }
        } catch (Throwable ex) {
            LoggerUtils.severe(plugin, "Command Error: " + commandLabel);
            LoggerUtils.severe(plugin, ex);
            sender.sendMessage(ChatColor.RED + "Command Error:  " + cmd.getName());
        }
        return true;
    }
}

package net.pravian.bukkitlib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Represents the interface which permission handles must inherit.
 */
public interface BukkitPermissionHandler {
    
    /**
     * Validates if a CommandSender has permission to use a command.
     * 
     * @param sender The sender executing the command.
     * @param command The command being executed.
     * @param args The arguments the command has.
     * @return true/false depending on if the sender has permission to use the command.
     */
    public boolean hasPermission(CommandSender sender, Command command, String[] args);
}

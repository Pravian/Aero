package net.pravian.bukkitlib.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

public class CommandUtils {

    protected static <T> T getField(Object from, String name) {
        Class<?> checkClass = from.getClass();
        do {
            try {
                Field field = checkClass.getDeclaredField(name);
                field.setAccessible(true);
                return (T) field.get(from);
            } catch (NoSuchFieldException ex) {
            } catch (IllegalAccessException ex) {
            }
        } while (checkClass.getSuperclass() != Object.class && ((checkClass = checkClass.getSuperclass()) != null));
        return null;
    }

    public HashMap<String, Command> getKnownCommands(CommandMap commandMap) {
        Object knownCommands = getField(commandMap, "knownCommands");
        if (knownCommands != null) {
            if (knownCommands instanceof HashMap) {
                return (HashMap<String, Command>) knownCommands;
            }
        }
        return null;
    }

    public CommandMap getCommandMap() {
        Object commandMap = getField(Bukkit.getServer().getPluginManager(), "commandMap");
        if (commandMap == null) {
            return null;
        }

        if (commandMap instanceof CommandMap) {
            return (CommandMap) commandMap;
        }
        return null;
    }

    public void unregisterCommand(String commandName) {
        CommandMap commandMap = getCommandMap();
        if (commandMap == null) {
            return;
        }

        Command command = commandMap.getCommand(commandName.toLowerCase());
        if (command != null) {
            unregisterCommand(command, commandMap);
        }
    }

    public void unregisterCommand(Command command, CommandMap commandMap) {
        command.unregister(commandMap);
        HashMap<String, Command> knownCommands = getKnownCommands(commandMap);
        
        if (knownCommands == null) {
            return;
        }
        
        knownCommands.remove(command.getName());
        
        for (String alias : command.getAliases()) {
            knownCommands.remove(alias);
        }
    }
}

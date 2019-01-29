package net.pravian.aero.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

/** Represents all Command-related utilities. */
public class Commands {

  private Commands() {}

  /**
   * Returns a Map of all commands currently loaded into the server.
   *
   * @return A HashMap with all loaded commands.
   * @see #getKnownCommands(CommandMap)
   */
  public static HashMap<String, Command> getKnownCommands() {
    return getKnownCommands(getCommandMap());
  }

  /**
   * Returns a Map of all commands currently loaded into the server.
   *
   * @param commandMap The CommandMap from where to retrieve the commands from.
   * @return A HashMap with all loaded commands.
   */
  @SuppressWarnings("unchecked")
  public static HashMap<String, Command> getKnownCommands(CommandMap commandMap) {
    Object knownCommands = getField(commandMap, "knownCommands");
    if (knownCommands != null) {
      if (knownCommands instanceof HashMap) {
        return (HashMap<String, Command>) knownCommands;
      }
    }
    return null;
  }

  /**
   * Returns the current CommandMap loaded into the server.
   *
   * @return the CommandMap.
   */
  public static CommandMap getCommandMap() {
    Object commandMap = getField(Bukkit.getServer().getPluginManager(), "commandMap");
    if (commandMap == null) {
      return null;
    }

    if (commandMap instanceof CommandMap) {
      return (CommandMap) commandMap;
    }
    return null;
  }

  /**
   * Unregisters a command from the server.
   *
   * <p>After a command has been unregistered, it will no longer be able to be executed.
   *
   * @param commandName The name of the command to unregister.
   * @see #unregisterCommand(Command, CommandMap)
   */
  public static void unregisterCommand(String commandName) {
    CommandMap commandMap = getCommandMap();
    if (commandMap == null) {
      return;
    }

    Command command = commandMap.getCommand(commandName.toLowerCase());
    if (command != null) {
      unregisterCommand(command, commandMap);
    }
  }

  /**
   * Unregisters a command from the server.
   *
   * <p>After a command has been unregistered, it will no longer be able to be executed.
   *
   * @param command The command to unregister.
   * @see #unregisterCommand(Command, CommandMap)
   */
  public static void unregisterCommand(Command command) {
    CommandMap commandMap = getCommandMap();
    if (commandMap == null) {
      return;
    }

    if (command != null) {
      unregisterCommand(command, commandMap);
    }
  }

  /**
   * Unregisters a command from the server.
   *
   * <p>After a command has been unregistered, it will no longer be able to be executed.
   *
   * @param command The command to unregister.
   * @param commandMap The commandMap to unregister the command from.
   */
  public static void unregisterCommand(Command command, CommandMap commandMap) {
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

  /**
   * Returns the Command-name of a command-message.
   *
   * <p>Command-messages are formated as such: <i>/commandName arg1 arg2</i>. These are found in
   * events such as {@link org.bukkit.event.player.PlayerCommandPreprocessEvent}.
   *
   * @param commandMessage The Command-message to parse.
   * @return The command name.
   * @see org.bukkit.event.player.PlayerCommandPreprocessEvent
   */
  public static String parseCommandName(String commandMessage) {
    return commandMessage.split(" ")[0].substring(1).trim();
  }

  /**
   * Returns the Command-arguments of a command-message.
   *
   * <p>Command-messages are formated as such: <i>/commandName arg1 arg2</i>. These are found in
   * events such as {@link org.bukkit.event.player.PlayerCommandPreprocessEvent}.
   *
   * @param commandMessage The Command-message to parse.
   * @return The command arguments.
   * @see org.bukkit.event.player.PlayerCommandPreprocessEvent
   */
  public static String[] parseCommandArgs(String commandMessage) {
    return (String[]) ArrayUtils.remove(commandMessage.split(" "), 0);
  }

  /** @author sk89q */
  @SuppressWarnings("unchecked")
  private static <T> T getField(Object from, String name) {
    Class<?> checkClass = from.getClass();
    do {
      try {
        Field field = checkClass.getDeclaredField(name);
        field.setAccessible(true);
        return (T) field.get(from);
      } catch (NoSuchFieldException ex) {
        return null;
      } catch (IllegalAccessException ex) {
        return null;
      }
    } while (checkClass.getSuperclass() != Object.class
        && ((checkClass = checkClass.getSuperclass()) != null));
  }
}

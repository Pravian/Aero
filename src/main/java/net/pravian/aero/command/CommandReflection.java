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

import java.lang.reflect.Constructor;
import java.util.Map;
import net.pravian.aero.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

public class CommandReflection {

  private static final Constructor<PluginCommand> PLUGINCOMMAND_CONSTRUCTOR;

  static {
    Constructor<PluginCommand> temp = null;
    try {
      temp = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
      temp.setAccessible(true);
    } catch (Exception ignored) {
    }
    PLUGINCOMMAND_CONSTRUCTOR = temp;
  }

  private CommandReflection() {
    throw new AssertionError();
  }

  @SuppressWarnings("unchecked")
  public static CommandMap getCommandMap() {
    try {
      final Object commandMap = Reflection.getField(Bukkit.getPluginManager(), "commandMap");
      return commandMap instanceof CommandMap ? (CommandMap) commandMap : null;
    } catch (Exception ignored) {
      return null;
    }
  }

  public static Map<String, Command> getKnownCommands() {
    return getKnownCommands(getCommandMap());
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Command> getKnownCommands(CommandMap map) {
    if (map == null) {
      return null;
    }

    final Object commands = Reflection.getField(map, "knownCommands");
    return (Map<String, Command>) (commands instanceof Map ? commands : null);
  }

  public static PluginCommand newPluginCommand(String name, Plugin plugin) {
    try {
      return PLUGINCOMMAND_CONSTRUCTOR.newInstance(name, plugin);
    } catch (Exception ignored) {
      return null;
    }
  }
}

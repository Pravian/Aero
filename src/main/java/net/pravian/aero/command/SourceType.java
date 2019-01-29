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

import org.apache.commons.lang.Validate;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

/** Represents the different sources a command execution might originate from. */
public enum SourceType {

  // Generic source types
  ANY,
  NON_PLAYER,
  //
  // Non generic source types
  PLAYER,
  CONSOLE,
  BLOCK,
  RCON;

  public static SourceType fromSender(CommandSender sender) {
    if (sender instanceof Player) {
      return PLAYER;
    }

    if (sender instanceof RemoteConsoleCommandSender) {
      return RCON;
    }

    if (sender instanceof BlockCommandSender) {
      return BLOCK;
    }

    if (sender instanceof ConsoleCommandSender) {
      return CONSOLE;
    }

    return null;
  }

  public boolean isPlayer() {
    return this == PLAYER;
  }

  public boolean isPrivileged() {
    return this != PLAYER;
  }

  public boolean isGeneric() {
    return this == NON_PLAYER || this == ANY;
  }

  public boolean matches(CommandSender sender) {
    Validate.notNull(sender, "Sender may not be null");

    if (this == ANY) {
      return true;
    }

    // Filter out players first
    if (sender instanceof Player) {
      return this == PLAYER;
    }

    // All non-player below here
    if (this == NON_PLAYER) {
      return true;
    }

    if (sender instanceof RemoteConsoleCommandSender) {
      return this == RCON;
    }

    if (sender instanceof BlockCommandSender) {
      return this == BLOCK;
    }

    if (sender instanceof ConsoleCommandSender) {
      return this == CONSOLE;
    }

    // Unknown sender type
    return true;
  }
}

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
package net.pravian.aero.command.executor;

import java.util.List;
import lombok.Getter;
import net.pravian.aero.command.AeroCommandBase;
import net.pravian.aero.command.handler.AeroCommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public abstract class AbstractCommandExecutor<C extends AeroCommandBase<?>>
    implements AeroCommandExecutor<C> {

  @Getter protected final C commandBase;
  @Getter protected final String name;
  @Getter protected final AeroCommandHandler<?> handler;

  public AbstractCommandExecutor(AeroCommandHandler<?> handler, String name, C command) {
    this.handler = handler;
    this.name = name;
    this.commandBase = command;
  }

  @Override
  public final boolean hasPermission(CommandSender sender) {
    return hasPermission(sender, false);
  }

  @Override
  public boolean hasPermission(CommandSender sender, boolean sendMessage) {
    return true;
  }

  @Override
  public List<String> onTabComplete(
      CommandSender cs, Command cmnd, String string, String[] strings) {
    return commandBase.tabComplete(cs, cmnd, string, strings);
  }

  @Override
  public void setupCommand(PluginCommand command) {}
}

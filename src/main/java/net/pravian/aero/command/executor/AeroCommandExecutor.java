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

import net.pravian.aero.command.AeroCommandBase;
import net.pravian.aero.command.handler.AeroCommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

public interface AeroCommandExecutor<C extends AeroCommandBase<?>> extends TabExecutor
{

    public String getName();

    public C getCommandBase();

    public AeroCommandHandler<?> getHandler();

    public boolean hasPermission(CommandSender sender);

    public boolean hasPermission(CommandSender sender, boolean sendMessage);

    public void setupCommand(PluginCommand command);
}

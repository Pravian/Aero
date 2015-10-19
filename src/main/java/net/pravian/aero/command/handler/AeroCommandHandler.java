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
package net.pravian.aero.command.handler;

import java.util.Collection;
import java.util.Map;
import net.pravian.aero.command.AeroCommandBase;
import net.pravian.aero.command.executor.AeroCommandExecutor;
import net.pravian.aero.command.permission.AeroPermissionHandler;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.command.PluginCommand;

public interface AeroCommandHandler<T extends AeroPlugin<T>> {

    public T getPlugin();

    public void clearCommands();

    public void loadFrom(Package pack);

    public void addAll(Iterable<? extends AeroCommandBase<T>> commands);

    public void add(AeroCommandBase<T> command);

    public void add(AeroCommandBase<T> command, String name);

    public void add(AeroCommandExecutor<? extends AeroCommandBase<T>> executor);

    public boolean registerAll();

    public boolean registerAll(String fallbackPrefix);

    public Map<String, AeroCommandExecutor<?>> getExecutorMap();

    public Collection<AeroCommandExecutor<?>> getExecutors();

    public Map<String, PluginCommand> getRegisteredCommandsMap();

    public Collection<PluginCommand> getRegisteredCommands();

    public AeroPermissionHandler getPermissionHandler();

    public void setPermissionHandler(AeroPermissionHandler permissionHandler);

    public String getSuperPermission();

    public void setSuperPermission(String permission);

    public String getOnlyPlayerMessage();

    public void setOnlyPlayerMessage(String message);

    public String getOnlyConsoleMessage();

    public void setOnlyConsoleMessage(String message);

    public String getInvalidArgumentLengthMessage();

    public void setInvalidArgumentLengthMessage(String message);

    public String getInvalidArgumentMessage();

    public void setInvalidArgumentMessage(String message);

    public String getPermissionMessage();

    public void setPermissionMessage(String message);

    public String getCommandClassPrefix();

    public void setCommandClassPrefix(String prefix);
}

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

import lombok.Getter;
import lombok.Setter;
import net.pravian.aero.command.AeroCommandBase;
import net.pravian.aero.command.permission.AeroPermissionHandler;
import net.pravian.aero.component.PluginComponent;
import net.pravian.aero.plugin.AeroLogger;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.ChatColor;

public abstract class AbstractCommandHandler<T extends AeroPlugin<T>> extends PluginComponent<T> implements AeroCommandHandler<T> {

    @Getter
    @Setter
    protected String superPermission = plugin.getName().toLowerCase() + ".*";
    @Getter
    @Setter
    protected String onlyPlayerMessage = ChatColor.YELLOW + "Only players may execute that command.";
    @Getter
    @Setter
    protected String onlyConsoleMessage = ChatColor.YELLOW + "That command can only be executed from console.";
    @Getter
    @Setter
    protected String invalidArgumentLengthMessage = ChatColor.RED + "Invalid argument length!";
    @Getter
    @Setter
    protected String invalidArgumentMessage = ChatColor.RED + "Invalid argument: <arg>";
    @Getter
    @Setter
    protected String permissionMessage = ChatColor.RED + "You don't have permission to use that command.";
    @Getter
    @Setter
    protected String commandClassPrefix = "Command";
    @Getter
    @Setter
    private AeroPermissionHandler permissionHandler = null;

    public AbstractCommandHandler(T plugin) {
        this(plugin, plugin.getPluginLogger());
    }

    public AbstractCommandHandler(T plugin, AeroLogger logger) {
        super(plugin, logger);
    }

    @Override
    public void addAll(Iterable<? extends AeroCommandBase<T>> commands) {
        for (AeroCommandBase<T> command : commands) {
            add(command, null);
        }
    }

    @Override
    public void add(AeroCommandBase<T> command) {
        add(command, null);
    }

}

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

import java.util.List;
import net.pravian.aero.command.permission.PermissionHandler;
import net.pravian.aero.plugin.AeroPlugin;
import net.pravian.aero.util.Players;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class AeroCommandExecutor<T extends AeroPlugin<T>> implements TabExecutor {

    private final T plugin;
    private final AeroCommandHandler<T> handler;
    private final CommandBase<T> commandBase;
    private final CommandOptions options;

    public AeroCommandExecutor(AeroCommandHandler<T> handler, String name, CommandBase<T> commandBase) {
        this.plugin = handler.getPlugin();
        this.handler = handler;
        this.commandBase = commandBase;
        this.options = commandBase.getClass().getAnnotation(CommandOptions.class);
    }

    public CommandBase<T> getCommand() {
        return commandBase;
    }

    public CommandOptions getOptions() {
        return options;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.isEnabled()) {
            return false;
        }

        if (!hasPermission(sender, true)) {
            return true;
        }

        try {
            return commandBase.runCommand(sender, command, label, args);
        } catch (Exception ex) {
            // If this is ever ran, Aero failed :C
            handler.getPlugin().handleException("Unhandled command exception: " + command.getName(), ex);
            sender.sendMessage(ChatColor.RED + "Unhandled Command Error: " + command.getName());
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return commandBase.tabComplete(sender, command, alias, args);
    }

    public boolean hasPermission(CommandSender sender) {
        return hasPermission(sender, false);
    }

    public boolean hasPermission(CommandSender sender, boolean sendMsg) {
        if (options == null) {
            return true;
        }

        if (sendMsg) {
            if (hasPermission(sender, false)) {
                return true;
            }
            sender.sendMessage(handler.getPermissionMessage());
            return false;
        }

        // Match source type
        final SourceType[] sources = options.sources().length == 0 ? new SourceType[]{options.source()} : options.sources();
        boolean matches = false;
        for (SourceType type : sources) {
            if (type.matches(sender)) {
                matches = true;
                break;
            }
        }

        if (!matches) {
            if (sendMsg) {
                sender.sendMessage(Players.is(sender) ? handler.getOnlyConsoleMessage() : handler.getOnlyPlayerMessage()); // TODO: Better messages
            }
            return false;
        }

        // Superpermission?
        final String superPermission = handler.getSuperPermission();
        if (superPermission != null && !superPermission.isEmpty()) {
            if (sender.hasPermission(superPermission)) {
                return true;
            }
        }

        // Annotation?
        final String permission = options.permission().isEmpty() ? handler.getPlugin().getName().toLowerCase() + "." + options.subPermission() : options.permission();
        if (!permission.isEmpty()) {
            return sender.hasPermission(permission);
        }

        // PermissionHandler?
        final PermissionHandler permHandler = handler.getPermissionHandler();
        if (permHandler != null && permHandler.containsPermissions(commandBase.getCommandClass())) {
            for (String handlerPermission : permHandler.getPermissions(commandBase.getCommandClass())) {
                if (sender.hasPermission(handlerPermission)) {
                    return true;
                }
            }
            return false;
        }

        // Default to true
        return true;
    }

}

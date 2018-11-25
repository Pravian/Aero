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
import net.pravian.aero.command.CommandOptions;
import net.pravian.aero.command.SourceType;
import net.pravian.aero.command.handler.AeroCommandHandler;
import net.pravian.aero.command.permission.AeroPermissionHandler;
import net.pravian.aero.util.Players;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.Arrays;
import java.util.List;

public class SimpleCommandExecutor<C extends AeroCommandBase<?>> extends AbstractCommandExecutor<C>
{

    protected final CommandOptions options;

    public SimpleCommandExecutor(AeroCommandHandler<?> handler, String name, C command)
    {
        super(handler, name, command);
        this.options = command.getClass().getAnnotation(CommandOptions.class);
    }

    public CommandOptions getOptions()
    {
        return options;
    }

    @Override
    public void setupCommand(PluginCommand command)
    {
        if (options == null)
        {
            return;
        }

        command.setUsage(options.usage());
        command.setDescription(options.description());
        command.setAliases(Arrays.asList(options.aliases().split(",")));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!hasPermission(sender, true))
        {
            return true;
        }

        try
        {
            return commandBase.runCommand(sender, command, label, args);
        }
        catch (Exception ex)
        {
            // If this is ever ran, Aero failed :C
            handler.getPlugin().handleException("Unhandled command exception: " + command.getName(), ex);
            sender.sendMessage(ChatColor.RED + "Unhandled Command Error: " + command.getName());
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        return commandBase.tabComplete(sender, command, alias, args);
    }

    @Override
    public boolean hasPermission(CommandSender sender, boolean sendMsg)
    {
        if (sendMsg)
        {
            if (hasPermission(sender, false))
            {
                return true;
            }
            sender.sendMessage(handler.getPermissionMessage());
            return false;
        }

        // Match source type
        if (options != null)
        {
            final SourceType[] sources = options.sources().length == 0 ? new SourceType[]{options.source()} : options.sources();
            boolean matches = false;
            for (SourceType type : sources)
            {
                if (type.matches(sender))
                {
                    matches = true;
                    break;
                }
            }
            if (!matches)
            {
                if (sendMsg)
                {
                    sender.sendMessage(Players.is(sender) ? handler.getOnlyConsoleMessage() : handler.getOnlyPlayerMessage()); // TODO: Better messages
                }
                return false;
            }
        }

        // Superpermission?
        final String superPermission = handler.getSuperPermission();
        if (superPermission != null && !superPermission.isEmpty())
        {
            if (sender.hasPermission(superPermission))
            {
                return true;
            }
        }

        // Annotation?
        if (options != null)
        {
            final String permission = options.permission().isEmpty() ? handler.getPlugin().getName().toLowerCase() + "." + options.subPermission() : options.permission();
            if (!permission.isEmpty())
            {
                return sender.hasPermission(permission);
            }
        }

        // SimplePermissionHandler?
        final AeroPermissionHandler permHandler = handler.getPermissionHandler();
        if (permHandler != null && permHandler.containsPermissions(commandBase.getCommandClass()))
        {
            for (String handlerPermission : permHandler.getPermissions(commandBase.getCommandClass()))
            {
                if (sender.hasPermission(handlerPermission))
                {
                    return true;
                }
            }
            return false;
        }

        // Default to true
        return true;
    }

    public static class SimpleCommandExecutorFactory implements AeroCommandExecutorFactory
    {

        @Override
        public AeroCommandExecutor<? extends AeroCommandBase<?>> newExecutor(AeroCommandHandler<?> handler, String name, AeroCommandBase<?> command)
        {
            return new SimpleCommandExecutor<>(handler, name, command);
        }

    }
}

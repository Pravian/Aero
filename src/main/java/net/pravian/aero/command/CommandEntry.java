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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.pravian.aero.command.parser.CustomParser;
import net.pravian.aero.command.parser.Parser;
import net.pravian.aero.command.parser.Parsers;
import net.pravian.aero.command.parser.StringJoiner;
import net.pravian.aero.plugin.AeroPlugin;
import net.pravian.aero.util.Players;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

// Package-private: only for internal use
// TODO: docs
class CommandEntry<T extends AeroPlugin<T>> implements CommandExecutor {

    private final PluginCommandHandler<T> handler;
    private final CommandOptions options;
    private final PluginCommand<T> commandHolder;
    private final Method commandMethod;
    private final boolean mayJoinStrings; // TODO: Describe what happens here
    private final List<Parser<?>> parsers;

    CommandEntry(PluginCommandHandler<T> handler, CommandOptions options, PluginCommand<T> inst, Method method) throws IllegalArgumentException {
        this.handler = handler;
        this.options = options;
        this.commandHolder = inst;
        this.commandMethod = method;
        this.parsers = new ArrayList<Parser<?>>();

        // Determine parsers
        boolean joinStrings = false;
        for (Class<?> type : method.getParameterTypes()) {

            // Custom parser
            final Class<? extends Parser<?>> parserClass = type.getAnnotation(CustomParser.class).value();
            if (parserClass != null) {

                try {
                    final Parser<?> parser = parserClass.newInstance();
                    parsers.add(parser);
                    continue;
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Could not instantiate custom parser class with zero-argument constructor: " + parserClass.getName(), ex);
                }

            }

            // Infer parser
            final Parser parser = Parsers.forClass(type);
            if (parser == null) {
                throw new IllegalArgumentException("Command expects unknown argument type: " + type.toString());
            }
            parsers.add(parser);

            // Joinstrings for args.length > parsers.size()
            if (parser instanceof StringJoiner) {
                joinStrings = true;
            }
        }
        this.mayJoinStrings = joinStrings;
    }

    public PluginCommandHandler<T> getHandler() {
        return handler;
    }

    public CommandOptions getOptions() {
        return options;
    }

    public List<Parser<?>> getParsers() {
        return Collections.unmodifiableList(parsers);
    }

    public boolean hasPermission(CommandSender sender) {
        return hasPermission(sender, false);
    }

    /**
     * Checks if the CommandSender has permissions to run this command.
     *
     * <p>
     * Validates if the player has the "permission" value from the CommandOptions annotation. If a custom PluginCommandContainer has been registered, it will use that instead.
     * Regardless of which permission handling method is used, the SourceType will always be checked.</p>
     *
     * <p>
     * If true is passed as the second argument, the sender will be sent the appropriate error message set in PluginCommandHandler if the sender cannot execute the command.
     * For example, if the sender is a player and this method is ran on a console-only command, the sender will be sent the "console-only" message, as defined in
     * the PluginCommandHandler.</p>
     *
     * @param sender The sender for which to check the permissions.
     * @param sendMsg If the sender should be sent error messages.
     * @return true if the CommandSender has permission to use this command.
     */
    public boolean hasPermission(CommandSender sender, boolean sendMsg) {
        final SourceType source = options.source();
        final boolean isPlayer = Players.is(sender);

        // Validate source
        if (source == SourceType.PLAYER && !isPlayer) {
            if (sendMsg) {
                sender.sendMessage(handler.getOnlyPlayerMessage());
            }

            return false;
        }

        if (source == SourceType.CONSOLE && isPlayer) {
            if (sendMsg) {
                sender.sendMessage(handler.getOnlyConsoleMessage());
            }

            return false;
        }

        // If Console, always allow
        if (!Players.is(sender)) {
            return true;
        }

        // Superpermission?
        if (handler.getSuperPermission() != null && !handler.getSuperPermission().isEmpty()) {
            if (sender.hasPermission(handler.getSuperPermission())) {
                return true;
            }
        }

        // PermissionContainer?
        if (handler.getPermissionContainer() != null) {
            final Set<String> handlerPermissions = handler.getPermissionContainer().getPermissions(commandHolder.getCommandClass());

            if (handlerPermissions != null) {
                for (String handlerPermission : handlerPermissions) {
                    if (sender.hasPermission(handlerPermission)) {
                        return true;
                    }
                }

                if (sendMsg) {
                    sender.sendMessage(handler.getPermissionMessage());
                }
                return false;
            }
        }

        // Annotation?
        if (options.subPermission() != null && !options.subPermission().isEmpty()) {
            final boolean result = sender.hasPermission(options.subPermission());

            if (!result && sendMsg) {
                sender.sendMessage(handler.getPermissionMessage());
            }

            return result;
        }

        // Default to true
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Check permission
        if (!hasPermission(sender, true)) {
            return true;
        }

        // Check argument lenghts
        if (args.length < parsers.size() || !mayJoinStrings && args.length != parsers.size()) {
            sender.sendMessage(handler.getInvalidArgumentLengthMessage());
            return true;
        }

        // Parse arguments
        final List<Object> pArgs = new ArrayList<Object>();
        int offset = 0;
        for (Parser parser : parsers) {
            final Object parsed = parser.parse(args, offset++); // TODO: Allow parsers to increment the offset
            if (parsed == null) {
                sender.sendMessage(handler.getInvalidArgumentMessage().replace("<arg>", args[offset - 1]));
                return true;
            }
            pArgs.add(parsed);
        }

        // Setup command
        try {
            commandHolder.onCommand(sender, command, label, args);
        } catch (CommandUnimplementedException ignored) {
            // TODO: Implement if the command _is_ implemented.
        }

        boolean result = true;
        try {
            commandMethod.invoke(commandHolder, pArgs.toArray());
        } catch (Exception ex) {
            handler.getPlugin().handleException("Exception executing command: " + command.getName(), ex);
            sender.sendMessage(ChatColor.RED + "Command Error:  " + command.getName());
            return true;
        }

        try {
            commandHolder.reset();
        } catch (Exception ex) {
            handler.getPlugin().handleException("Exception resetting command: " + command.getName(), ex);
        }
        return result;
    }

}

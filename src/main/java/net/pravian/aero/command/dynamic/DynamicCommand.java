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
package net.pravian.aero.command.dynamic;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.pravian.aero.command.handler.SimpleCommandHandler;
import net.pravian.aero.command.CommandRegistrationException;
import net.pravian.aero.command.CommandOptions;
import net.pravian.aero.command.dynamic.parser.CustomParser;
import net.pravian.aero.command.dynamic.parser.DefaultParser;
import net.pravian.aero.command.dynamic.parser.ParseException;
import net.pravian.aero.command.dynamic.parser.Parser;
import net.pravian.aero.command.SimpleCommand;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Represents a plugin command.
 *
 * @param <T> Optional: Type safety for {@link #plugin}
 */
@Deprecated
public abstract class DynamicCommand<T extends AeroPlugin<T>> extends SimpleCommand<T> implements CommandExecutor {

    private final List<ParsingMethodDelegate<?>> delegates = Lists.newArrayList();
    private final Map<Class<?>, Parser<?>> argumentParsers = Maps.newHashMap();
    //

    protected DynamicCommand() {
    }

    /**
     * Executed when the command is being ran.
     *
     * <p>
     * <b>In normal conditions, you should never call this manually.</b></p>
     *
     * @param sender The CommandSender who is executing the command.
     * @param command The command which is being executed.
     * @param label The exact command being used.
     * @param args The arguments to the command.
     * @return true/false depending if the command executed successfully.
     */
    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        // Tricky loops and exceptions 8D
        for (ParsingMethodDelegate<?> delegate : delegates) {
            try {
                return delegate.onCommand(sender, command, label, args);
            } catch (ParseException ignored) { // TODO: Something something
                continue;
            } catch (ExecutionException ex) {
                throw ex.getCause(); //
            } catch (Exception ex) {
                plugin.handleException("Uncaught DynamicCommand exception whilst executing command: " + command.getName(), ex);
                sender.sendMessage(ChatColor.RED + "Command Error: " + command.getName());
            }

            return true;
        }

        // All CommandDelegate's threw ParseExceptions
        return false;
    }

    @Override
    public void unregister() {
        super.unregister();
        delegates.clear();
    }

    // Should load command stuff
    @Override
    public void register(SimpleCommandHandler<T> handler) throws CommandRegistrationException {
        super.register(handler);

        // Find command methods
        delegates.clear();
        for (Method method : getClass().getMethods()) {
            CommandOptions options = method.getAnnotation(CommandOptions.class);
            if (options == null) {
                continue;
            }

            if (method.getReturnType() != boolean.class && method.getReturnType() != Boolean.class) {
                logger.severe("Could not register command method: " + method.getName() + ". Method does not return valid type (boolean)!");
                continue;
            }

            try {
                registerDelegate(method, options);
            } catch (Exception ex) {
                logger.severe("Could not register command method: " + method.getName());
                logger.severe(ex);
            }
        }

    }

    public void registerArgumentParser(Class<?> superType, Parser<?> parser) {
        argumentParsers.put(superType, parser);
    }

    public Map<Class<?>, Parser<?>> getArgumentParsers() {
        return argumentParsers;
    }

    // TODO: Docs docs docs
    protected void registerDelegate(Method method, CommandOptions options) {
        final List<Parser<?>> parsers = new ArrayList<Parser<?>>();

        // Determine parsers
        for (Class<?> type : method.getParameterTypes()) {

            Parser parser = null;

            // Custom parser
            final CustomParser parserAnn = type.getAnnotation(CustomParser.class);
            if (parserAnn != null) {
                try {
                    parser = parserAnn.value().newInstance();
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Could not instantiate custom parser class with zero-argument constructor: " + parserAnn.value().getName(), ex);
                }
            }

            // Registered parsers
            if (parser == null) {
                for (Class<?> parserType : argumentParsers.keySet()) {
                    if (parserType.isAssignableFrom(type)) {
                        parser = argumentParsers.get(parserType);
                    }
                }
            }

            // Builtin Parser
            if (parser == null) {
                parser = DefaultParser.forClass(type);
            }

            // Parser could not be found
            if (parser == null) {
                throw new IllegalArgumentException("Command expects unknown argument type: " + type.toString());
            }

            parsers.add(parser);
        }

        final ParsingMethodDelegate<T> delegate = new ParsingMethodDelegate<T>(this, options, method, parsers);
        delegates.add(delegate);
    }

}

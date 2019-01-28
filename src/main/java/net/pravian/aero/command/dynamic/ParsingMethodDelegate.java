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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.pravian.aero.command.AeroCommandBase;
import net.pravian.aero.command.CommandOptions;
import net.pravian.aero.command.SimpleCommand;
import net.pravian.aero.command.dynamic.parser.ParseException;
import net.pravian.aero.command.dynamic.parser.Parser;
import net.pravian.aero.command.handler.AeroCommandHandler;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

// Package-private: only for internal use
// TODO: docs
class ParsingMethodDelegate<T extends AeroPlugin<T>> implements CommandExecutor {

  private final AeroCommandBase<T> aeroCommand;
  private final AeroCommandHandler<T> handler;
  private final CommandOptions options;
  private final Method method;
  private final List<Parser<?>> parsers = new ArrayList<Parser<?>>();

  ParsingMethodDelegate(SimpleCommand<T> command, CommandOptions options, Method method,
      List<Parser<?>> parsers) {
    this.aeroCommand = command;
    this.handler = command.getHandler();
    this.options = options;
    this.method = method;
    this.parsers.addAll(parsers);
  }

  public CommandOptions getOptions() {
    return options;
  }

  public List<Parser<?>> getParsers() {
    return Collections.unmodifiableList(parsers);
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
      throws ParseException, ExecutionException {

    // Parse arguments
    final List<Object> pArgs = new ArrayList<Object>();
    int offset = 0;
    for (Parser<?> parser : parsers) {
      if (pArgs.size() >= parsers.size() // Too many arguments parsed
          || offset >= args.length) { // Too many parsers for arguments
        return false; // TODO: Allow parsers to parse multiple arguments?
      }

      try {
        offset = parser.parse(pArgs, args, offset);
      } catch (ParseException pex) {
        throw pex;
      } catch (Exception ex) {
        throw new ParseException(
            ex.getMessage() != null ? ex.getMessage() : "Could not parse argument: " + args[offset],
            ex);
      }
    }

    // Debug
    assert method.getParameterTypes().length == pArgs.size(); // All parameters are satisfied

    try {
      return (Boolean) method.invoke(aeroCommand, pArgs.toArray());
    } catch (Exception ex) {
      throw new ExecutionException(ex);
    }
  }
}

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
package net.pravian.aero.command.simple;

import net.pravian.aero.command.AbstractCommandBase;
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
public abstract class AeroCommand<T extends AeroPlugin<T>> extends AbstractCommandBase<T> implements CommandExecutor {

    protected AeroCommand() {
    }

    @Override
    public final boolean runCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        setVariables(sender, command, label, args);

        try {
            return onCommand(sender, command, label, args);
        } catch (Exception ex) {
            plugin.handleException("Uncaught exception executing command: " + command.getName(), ex);
            sender.sendMessage(ChatColor.RED + "Command error: " + (ex.getMessage() == null ? "Unknown cause" : ex.getMessage()));
            return true;
        }
    }

}

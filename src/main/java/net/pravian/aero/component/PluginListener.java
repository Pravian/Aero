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
package net.pravian.aero.component;

import net.pravian.aero.command.base.Registrable;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class PluginListener<T extends AeroPlugin<T>> extends PluginComponent<T> implements Listener, Registrable {

    protected boolean registered = false;

    public PluginListener() {
    }

    public PluginListener(T plugin) {
        super(plugin);
    }

    @Override
    public final void register() {
        unregister();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        registered = true;
    }

    @Override
    public final void unregister() {
        HandlerList.unregisterAll(this);
        registered = false;
    }

    @Override
    public boolean isRegistered() {
        return registered;
    }

}

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

import com.google.common.base.Preconditions;
import java.lang.reflect.ParameterizedType;
import net.pravian.aero.Aero;
import net.pravian.aero.AeroException;
import net.pravian.aero.RegisteredPlugin;
import net.pravian.aero.command.base.PluginContainer;
import net.pravian.aero.plugin.AeroLogger;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.Server;

public abstract class PluginComponent<T extends AeroPlugin<T>> implements PluginContainer<T> {

    protected final T plugin;
    protected final AeroLogger logger;
    protected final Server server;

    // TODO: Describe awesomeness
    @SuppressWarnings("unchecked")
    public PluginComponent() {

        T foundPlugin = null;
        try {
            final Class<T> typeClass = (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass())
                    .getActualTypeArguments()[0];

            for (RegisteredPlugin registeredPlugin : Aero.getInstance().getRegisteredPlugins()) {
                if (registeredPlugin.getPlugin().getClass().isAssignableFrom(typeClass)) {
                    foundPlugin = (T) registeredPlugin.getPlugin();
                }
            }
        } catch (Exception ex) {
            throw new AeroException("Could not determine plugin class type! (Are you properly extending with generics?)", ex);
        }

        if (foundPlugin == null) {
            throw new AeroException("Could not find associated plugin instance!");
        }

        this.plugin = foundPlugin;
        this.logger = plugin.getPluginLogger();
        this.server = plugin.getServer();
    }

    public PluginComponent(T plugin) {
        this(Preconditions.checkNotNull(plugin, "Plugin may not be null!"), plugin.getPluginLogger());
    }

    public PluginComponent(T plugin, AeroLogger logger) {
        this.plugin = Preconditions.checkNotNull(plugin, "Plugin may not be null!");
        this.logger = Preconditions.checkNotNull(logger, "Logger may not be null!");
        this.server = plugin.getServer();
    }

    @Override
    public T getPlugin() {
        return plugin;
    }

    public AeroLogger getPluginLogger() {
        return logger;
    }

}

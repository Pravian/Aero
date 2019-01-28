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
package net.pravian.aero.plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import net.pravian.aero.Aero;
import net.pravian.aero.base.PluginContainer;
import net.pravian.aero.component.PluginListener;
import net.pravian.aero.config.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AeroPlugin<T extends AeroPlugin<T>> extends JavaPlugin implements
    PluginContainer<T>, Listener {

    //
    public final YamlConfig config;
    public final AeroLogger logger;
    protected final T plugin;
    protected final Server server;
    //
    protected Aero aero;

    @SuppressWarnings("unchecked")
    public AeroPlugin() {
        try {
            this.plugin = (T) this;
        } catch (ClassCastException ex) {
            getLogger()
                .severe("Could not cast plugin type! (Are you extending AeroPlugin properly?)");
            throw new RuntimeException(ex);
        }

        this.server = plugin.getServer();
        this.logger = new AeroLogger(plugin);
        this.config = new YamlConfig(plugin, "config.yml"); // Requires logger to be present
    }

    public final Aero getAero() {
        return aero;
    }

    @Override
    public final T getPlugin() {
        return plugin;
    }

    @Override
    public final void onLoad() {
        load();
    }

    @Override
    public final void onEnable() {
        this.aero = Aero.getInstance();
        aero.register(plugin);

        enable();
        server.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public final void onDisable() {
        HandlerList.unregisterAll();

        try {
            disable();
        } finally {
            aero.unregister(plugin);
        }
    }

    protected void setup() {
    }

    protected void load() {
    }

    protected abstract void enable();

    protected abstract void disable();

    /**
     * Returns the YamlConfig associated to this plugin.
     * <p>
     * <p>
     * Assuming using <b>config.yml</b> and copying defaults.</p>
     *
     * @return The YamlConfig instance.
     */
    @Override
    public final YamlConfig getConfig() {
        return config;
    }

    /**
     * Returns the PluginLogger associated to this plugin.
     *
     * @return The PluginLogger instance.
     */
    public final AeroLogger getPluginLogger() {
        return logger;
    }

    /**
     * Returns the version of this plugin.
     *
     * @return the version
     */
    public final String getVersion() {
        return plugin.getDescription().getVersion();
    }

    /**
     * Returns the first author of this plugin.
     *
     * @return The author.
     */
    public final String getAuthor() {
        return getAuthors().get(0);
    }

    /**
     * Returns the authors of this plugin.
     *
     * @return The authors.
     */
    public final List<String> getAuthors() {
        return plugin.getDescription().getAuthors();
    }

    /**
     * Registers an event listener.
     *
     * @param listener to register
     * @return True if the listener registered correctly.
     */
    public final boolean register(Listener listener) {
        if (listener == null) {
            return false;
        }

        if (listener instanceof PluginListener) {
            ((PluginListener<?>) listener).register();
            return true;
        }

        Bukkit.getPluginManager().registerEvents(listener, plugin);
        return true;
    }

    /**
     * Registers an event listener.
     * <p>
     * TODO: Better docs for this, multiple usage, etc.
     *
     * @param listener class to register
     * @return True if the listener registered correctly.
     */
    @SuppressWarnings("unchecked")
    public final <T extends Listener> boolean register(Class<T> listener)
        throws IllegalArgumentException {
        if (listener == null) {
            return false;
        }

        // TODO: Speed up PluginListener classes
        // Find plugin constructor, if it's present
        Constructor<T> foundCon = null;
        for (Constructor<?> con : listener.getConstructors()) {
            if (con.getParameterTypes().length != 1) {
                continue;
            }

            if (Plugin.class.isAssignableFrom(con.getParameterTypes()[0])) {
                foundCon = (Constructor<T>) con;
                break;
            }
        }

        // Instantiate
        final T inst;
        try {
            inst = (foundCon == null ? listener.newInstance() : foundCon.newInstance(this));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            handleException(ex);
            return false;
        }

        // Register
        register(inst);
        return true;
    }

    public final void handleException(String msg) {
        handleException(msg, null);
    }

    public final void handleException(Throwable ex) {
        handleException(null, ex);
    }

    public final void handleException(String msg, Throwable ex) {
        logger.severe(msg, ex);
    }
}

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
package net.pravian.aero;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import net.pravian.aero.internal.AeroContainer;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Represents the Aero framework.
 */
public class Aero {

    /**
     * The name of this library.
     */
    public static final String NAME = "Aero";
    /**
     * The author of this library.
     */
    public static final String AUTHOR = "Prozza";

    private final AeroContainer plugin;
    private final Logger logger;
    private final Map<String, RegisteredPlugin> plugins;
    //
    private boolean debug = false;
    private boolean init = false;

    public Aero(AeroContainer plugin) {
        // Note: created at constructor time, plugin is not complete yet
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.plugins = new HashMap<String, RegisteredPlugin>();
    }

    /**
     * Initializes this framework.
     */
    public void init() {
        if (init) {
            throw new AeroException("Framework already initialized!");
        }

        this.init = true;
        logger.info(NAME + " v" + getFullVersion() + " by " + AUTHOR + " initialized");
    }

    public void deinit() {
        if (!init) {
            throw new AeroException("Framework not initialized!");
        }

        plugins.clear();

        this.init = false;
    }

    /**
     * Validates if this framework has been initialized.
     *
     * @return True if the framework has been initialized.
     */
    public boolean isInitialized() {
        return init;
    }

    /**
     * Registers a new plugin for use with Aero.
     *
     * @param plugin The plugin to register.
     * @return The options for this plugin.
     */
    public RegisteredPlugin register(AeroPlugin<?> plugin) {
        verifyInitialized();

        if (plugin == null) {
            throw new IllegalArgumentException("Plugin is null.");
        }

        final String key = getPluginKey(plugin);

        if (plugins.containsKey(key)) {
            throw new AeroException("Plugin already registered!");
        }

        final PluginDescriptionFile pdf = plugin.getDescription();

        if (pdf.getVersion() == null
                || pdf.getVersion().isEmpty()) {
            throw new AeroException("Incomplete plugin description file: Missing version number!");
        }

        if (pdf.getAuthors() == null
                || pdf.getAuthors().isEmpty()) {
            throw new AeroException("Incomplete plugin description file: Missing author!");
        }

        final RegisteredPlugin options = new RegisteredPlugin(plugin);
        plugins.put(key, new RegisteredPlugin(plugin));
        logger.info("Registered " + NAME + " plugin: " + plugin.getName());
        return options;
    }

    public void unregister(AeroPlugin<?> plugin) {
        verifyInitialized();

        if (plugin == null) {
            throw new IllegalArgumentException();
        }

        final String key = getPluginKey(plugin);

        if (plugins.remove(key) == null) {
            throw new AeroException("Plugin not registered!");
        }
    }

    /**
     * Returns the registered plugin associated with the specified plugin.
     *
     * @param plugin The plugin for which to obtain the registered plugin.
     * @return The registered plugin.
     */
    public RegisteredPlugin getRegisteredPlugin(Plugin plugin) {
        verifyInitialized();
        return plugins.get(getPluginKey(plugin));
    }

    /**
     * Returns a set containing all the registered plugins.
     *
     * @return The set.
     */
    public Set<RegisteredPlugin> getRegisteredPlugins() {
        verifyInitialized();
        return new HashSet<RegisteredPlugin>(plugins.values());
    }

    /**
     * Returns the build version of this framework.
     *
     * @return The version of this Platform.
     */
    public String getVersion() {
        return plugin.getBuildVersion();
    }

    /**
     * Returns the build number of this build.
     *
     * @return The build number.
     */
    public String getBuildNumber() {
        return plugin.getBuildNumber();
    }

    /**
     * Returns the full framework version in the <b>major.minor.build</b> format.
     *
     * @return The version.
     */
    public String getFullVersion() {
        return plugin.getBuildVersion() + "." + plugin.getBuildNumber();
    }

    /**
     * Returns the date this framework build was compiled.
     *
     * @return The date
     */
    public String getBuildDate() {
        return plugin.getBuildDate();
    }

    /**
     * Explicitly enables debug mode.
     *
     * <p>
     * <b>Note</b>: Debug should never be left enabled when releasing your plugin.</p>
     */
    public void explicitEnableDebugging() {
        verifyInitialized();

        if (debug) {
            logger.warning("Attempted to enabled debug mode twice!");
            return;
        }

        this.debug = true;
        logger.warning("***********************************");
        logger.warning("* Warning: Debug mode is enabled! *");
        logger.warning("***********************************");
    }

    /**
     * Validates if debug mode is enabled.
     *
     * @return True if debug mode is enabled
     */
    public boolean isDebugging() {
        return debug;
    }

    private void verifyInitialized() {
        if (!init) {
            throw new AeroUninitializedException();
        }
    }

    private String getPluginKey(Plugin plugin) {
        return plugin.getName().toLowerCase().replace(" ", "");
    }

    /**
     * Obtains the framework instance running on this platform.
     *
     * @return The framework.
     * @throws AeroException If this method is called before the framework has been initialized.
     */
    public static Aero getInstance() {
        RegisteredServiceProvider<AeroContainer> container = Bukkit.getServicesManager().getRegistration(AeroContainer.class);

        if (container == null) {
            throw new AeroException("Could not find " + NAME + "!");
        }

        final Aero aero = container.getProvider().getAero();

        if (aero == null) {
            throw new AeroUninitializedException("Aero is null!");
        }

        return aero;
    }
}

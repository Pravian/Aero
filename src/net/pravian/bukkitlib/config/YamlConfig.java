package net.pravian.bukkitlib.config;

import java.io.File;
import java.io.IOException;
import net.pravian.bukkitlib.serializable.SerializableBlock;
import net.pravian.bukkitlib.serializable.SerializableBlockLocation;
import net.pravian.bukkitlib.serializable.SerializableEntityLocation;
import net.pravian.bukkitlib.serializable.SerializableInventory;
import net.pravian.bukkitlib.serializable.SerializableObject;
import net.pravian.bukkitlib.util.FileUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Represents a definable YAML configuration.
 *
 * @see YamlConfiguration
 */
public class YamlConfig extends YamlConfiguration {

    private final Plugin PLUGIN;
    private final File CONFIG_FILE;
    private final boolean COPY_DEFAULTS;

    /**
     * Creates a new YamlConfig instance.
     *
     * <p>Example:
     * <pre>
     * YamlConfig config = new YamlConfig(this, "config.yml");
     * config.load();
     * </pre></p>
     *
     * <p>Note: This will automatically attempt to load the config from the the plugin archive.</p>
     *
     * @param plugin The plugin to which the config belongs.
     * @param fileName The filename of the config file.
     */
    public YamlConfig(Plugin plugin, String fileName) {
        this(plugin, fileName, true);
    }

    /**
     * Creates a new YamlConfig instance.
     *
     * <p>Example:
     * <pre>
     * YamlConfig config = new YamlConfig(this, "config.yml", true);
     * config.load();
     * </pre></p>
     *
     * @param plugin The plugin to which the config belongs.
     * @param fileName The filename of the config file.
     * @param copyDefaults If the defaults should be copied and/loaded from a config in the plugin jar-file.
     */
    public YamlConfig(Plugin plugin, String fileName, boolean copyDefaults) {
        this(plugin, FileUtils.getPluginFile(plugin, fileName), copyDefaults);
    }

    /**
     * Creates a new YamlConfig instance.
     *
     * <p>Example:
     * <pre>
     * YamlConfig config = new YamlConfig(this, new File(plugin.getDataFolder() + "/players", "DarthSalamon.yml"), false);
     * config.load();
     * </pre></p>
     *
     * @param plugin The plugin to which the config belongs.
     * @param file The file of the config file.
     * @param copyDefaults If the defaults should be copied and/loaded from a config in the plugin jar-file.
     */
    public YamlConfig(Plugin plugin, File file, boolean copyDefaults) {
        this.PLUGIN = plugin;
        this.CONFIG_FILE = file;
        this.COPY_DEFAULTS = copyDefaults;
    }

    /**
     * Stores an instance of SerializableObject.
     *
     * <p><b>Warning</b>: SerializableObjects should never be stored using {@link #set(String, Object)}
     *
     * @param path
     * @param object
     * @see SerializableObject
     */
    public void setSerializable(String path, SerializableObject object) {
        super.set(path, object.serialize());
    }

    /**
     * Retrieves a stored SerializableBlock
     *
     * @param path The path where the SerializedObject is stored.
     * @return The retrieved item. / null
     * @see SerializableBlock
     */
    public SerializableBlock getSerializableBlock(String path) {
        final String serialized = super.getString(path);

        if (serialized == null) {
            return null;
        }

        SerializableBlock object = new SerializableBlock(serialized);

        if (object.deserialize() == null) {
            return null;
        }

        return object;
    }

    /**
     * Retrieves a stored SerializableBlockLocation
     *
     * @param path The path where the SerializedObject is stored.
     * @return The retrieved item. / null
     * @see SerializableBlockLocation
     */
    public SerializableBlockLocation getSerializableBlockLocation(String path) {
        final String serialized = super.getString(path);

        if (serialized == null) {
            return null;
        }

        SerializableBlockLocation object = new SerializableBlockLocation(serialized);

        if (object.deserialize() == null) {
            return null;
        }

        return object;
    }

    /**
     * Retrieves a stored SerializableEntityLocation
     *
     * @param path The path where the SerializedObject is stored.
     * @return The retrieved item. / null
     * @see SerializableEntityLocation
     */
    public SerializableEntityLocation getSerializableEntityLocation(String path) {
        final String serialized = super.getString(path);

        if (serialized == null) {
            return null;
        }

        SerializableEntityLocation object = new SerializableEntityLocation(serialized);

        if (object.deserialize() == null) {
            return null;
        }

        return object;
    }

    /**
     * Retrieves a stored SerializableInventory
     *
     * @param path The path where the SerializedObject is stored.
     * @return The retrieved item. / null
     * @see SerializableInventory
     */
    public SerializableInventory getSerializableInventory(String path) {
        final String serialized = super.getString(path);

        if (serialized == null) {
            return null;
        }

        SerializableInventory object = new SerializableInventory(serialized);

        if (object.deserialize() == null) {
            return null;
        }

        return object;
    }

    /**
     * Saves the configuration to the predefined file.
     *
     * @see #YamlConfig(Plugin, String, boolean)
     */
    public void save() {
        try {
            super.save(CONFIG_FILE);
        } catch (Exception ex) {
            LoggerUtils.severe(PLUGIN, "Could not save configuration file: " + CONFIG_FILE.getName());
            LoggerUtils.severe(PLUGIN, ex);
        }
    }

    /**
     * Loads the configuration from the predefined file.
     *
     * <p>Optionally, if loadDefaults has been set to true, the file will be copied over from the default inside the jar-file of the owning plugin.</p>
     *
     * @see #YamlConfig(Plugin, String, boolean)
     */
    public void load() {
        try {
            if (COPY_DEFAULTS) {
                if (!CONFIG_FILE.exists()) {
                    CONFIG_FILE.getParentFile().mkdirs();
                    try {
                        FileUtils.copy(PLUGIN.getResource(CONFIG_FILE.getName()), CONFIG_FILE);
                    } catch (IOException ex) {
                        LoggerUtils.severe(PLUGIN, "Could not write default configuration file: " + CONFIG_FILE.getName());
                        LoggerUtils.severe(PLUGIN, ex);
                    }
                    LoggerUtils.info(PLUGIN, "Installed default configuration " + CONFIG_FILE.getName());
                }

                super.addDefaults(getDefaultConfig());
            }

            super.load(CONFIG_FILE);
        } catch (Exception ex) {
            LoggerUtils.severe(PLUGIN, "Could not load configuration file: " + CONFIG_FILE.getName());
            LoggerUtils.severe(PLUGIN, ex);
        }
    }

    /**
     * Returns the raw YamlConfiguration this config is based on.
     *
     * @return The YamlConfiguration.
     * @see YamlConfiguration
     */
    public YamlConfiguration getConfig() {
        return this;
    }

    /**
     * Returns the default configuration as been stored in the jar-file of the owning plugin.
     *
     * @return The default configuration.
     */
    public YamlConfiguration getDefaultConfig() {
        final YamlConfiguration DEFAULT_CONFIG = new YamlConfiguration();
        try {
            DEFAULT_CONFIG.load(PLUGIN.getResource(CONFIG_FILE.getName()));
        } catch (Throwable ex) {
            LoggerUtils.severe(PLUGIN, "Could not load default configuration: " + CONFIG_FILE.getName());
            LoggerUtils.severe(PLUGIN, ex);
            return null;
        }
        return DEFAULT_CONFIG;
    }
}
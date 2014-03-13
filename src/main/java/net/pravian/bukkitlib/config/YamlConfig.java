package net.pravian.bukkitlib.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.pravian.bukkitlib.InternalExceptionHandler;
import net.pravian.bukkitlib.serializable.SerializableObject;
import net.pravian.bukkitlib.util.FileUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * Represents a definable YAML configuration.
 *
 * @see YamlConfiguration
 */
public class YamlConfig extends YamlConfiguration implements BukkitConfig<YamlConfig> {

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
     * @see #set(java.lang.String, java.lang.Object)
     */
    @Override
    public void set(PathContainer path, Object value) {
        super.set(path.getPath(), value);
    }

    /**
     * @see #getString(java.lang.String)
     */
    @Override
    public String getString(PathContainer path) {
        return super.getString(path.getPath());
    }

    /**
     * @see #getBoolean(java.lang.String)
     */
    @Override
    public boolean getBoolean(PathContainer path) {
        return super.getBoolean(path.getPath());
    }

    /**
     * @see #getInt(java.lang.String)
     */
    @Override
    public int getInt(PathContainer path) {
        return super.getInt(path.getPath());
    }

    /**
     * @see #getDouble(java.lang.String)
     */
    @Override
    public double getDouble(PathContainer path) {
        return super.getDouble(path.getPath());
    }

    /**
     * @see #getLong(java.lang.String)
     */
    @Override
    public long getLong(PathContainer path) {
        return super.getLong(path.getPath());
    }

    /**
     * @see #getColor(java.lang.String)
     */
    @Override
    public Color getColor(PathContainer path) {
        return super.getColor(path.getPath());
    }

    /**
     * @see #getItemStack(java.lang.String)
     */
    @Override
    public ItemStack getItemStack(PathContainer path) {
        return super.getItemStack(path.getPath());
    }

    /**
     * @see #getOfflinePlayer(java.lang.String)
     */
    @Override
    public OfflinePlayer getOfflinePlayer(PathContainer path) {
        return super.getOfflinePlayer(path.getPath());
    }

    /**
     * @see #getVector(java.lang.String)
     */
    @Override
    public Vector getVector(PathContainer path) {
        return super.getVector(path.getPath());
    }

    /**
     * @see #getStringList(java.lang.String)
     */
    @Override
    public List<String> getStringList(PathContainer path) {
        return super.getStringList(path.getPath());
    }

    /**
     * @see #getIntegerList(java.lang.String)
     */
    @Override
    public List<Integer> getIntegerList(PathContainer path) {
        return super.getIntegerList(path.getPath());
    }

    /**
     * @see #getCharacterList(java.lang.String)
     */
    @Override
    public List<Character> getCharacterList(PathContainer path) {
        return super.getCharacterList(path.getPath());
    }

    /**
     * @see #getBooleanList(java.lang.String)
     */
    @Override
    public List<Boolean> getBooleanList(PathContainer path) {
        return super.getBooleanList(path.getPath());
    }

    /**
     * @see #getByteList(java.lang.String)
     */
    @Override
    public List<Byte> getByteList(PathContainer path) {
        return super.getByteList(path.getPath());
    }

    /**
     * @see #getDoubleList(java.lang.String)
     */
    @Override
    public List<Double> getDoubleList(PathContainer path) {
        return super.getDoubleList(path.getPath());
    }

    /**
     * @see #getFloatList(java.lang.String)
     */
    @Override
    public List<Float> getFloatList(PathContainer path) {
        return super.getFloatList(path.getPath());
    }

    /**
     * @see #getLongList(java.lang.String)
     */
    @Override
    public List<Long> getLongList(PathContainer path) {
        return super.getLongList(path.getPath());
    }

    /**
     * @see #getShortList(java.lang.String)
     */
    @Override
    public List<Short> getShortList(PathContainer path) {
        return super.getShortList(path.getPath());
    }

    /**
     * @see #getList(java.lang.String)
     */
    @Override
    public List<?> getList(PathContainer path) {
        return super.getList(path.getPath());
    }

    /**
     * @see #getMapList(java.lang.String)
     */
    @Override
    public List<Map<?, ?>> getMapList(PathContainer path) {
        return super.getMapList(path.getPath());
    }

    @Override
    public Object getDefault(PathContainer path) {
        return super.getDefault(path.getPath());
    }

    /**
     * @see #setSerializable(java.lang.String, net.pravian.bukkitlib.serializable.SerializableObject)
     */
    @Override
    public void setSerializable(PathContainer path, SerializableObject<?> object) {
        setSerializable(path.getPath(), object);
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
    @Override
    public void setSerializable(String path, SerializableObject<?> object) {
        super.set(path, object.serialize());
    }

    /**
     * @see #getSerializable(java.lang.String, java.lang.Class)
     */
    @Override
    public <T extends SerializableObject<?>> T getSerializable(PathContainer path, Class<T> type) {
        return getSerializable(path.getPath(), type);
    }

    /**
     * Retrieves a stored SerializableObject
     *
     * @param path The path where the SerializedObject is stored.
     * @return The retrieved item / null.
     * @see SerializableObject
     */
    @Override
    public <T extends SerializableObject<?>> T getSerializable(String path, Class<T> type) {
        final String serialized = super.getString(path);

        if (serialized == null) {
            return null;
        }

        final Constructor<T> cons;
        try {
            cons = type.getDeclaredConstructor(String.class);
        } catch (Exception ex) {
            InternalExceptionHandler.handle(PLUGIN, ex);
            return null;
        }

        final T object;

        try {
            object = cons.newInstance(serialized);
        } catch (Exception ex) {
            InternalExceptionHandler.handle(PLUGIN, ex);
            return null;
        }

        return object;
    }

    /**
     * Stores a Map of any generic type.
     *
     * @param <K> The key type to the map.
     * @param <V> The value type to the map.
     * @param path The path at which the map should be stored.
     * @param map The map to store.
     */
    @Override
    public <K, V> void getMap(PathContainer path, Map<K, V> map) {
        setMap(path.getPath(), map);
    }

    /**
     * Stores a Map of any generic type.
     *
     * @param <K> The key type to the map.
     * @param <V> The value type to the map.
     * @param path The path at which the map should be stored.
     * @param map The map to store.
     */
    @Override
    public <K, V> void setMap(String path, Map<K, V> map) {
        for (K key : map.keySet()) {
            super.set(path + "." + key.toString(), map.get(key));
        }
    }

    /**
     * Retrieves a Map of any generic type.
     *
     * <p><b>Note</b>: Any objects which couldn't be casted won't be returned in the map.</p>
     *
     * @param <K> The key type to the map.
     * @param <V> The value type to the map.
     * @param path The path where the map is stored.
     * @return The map.
     */
    @Override
    public <K, V> Map<K, V> getMap(PathContainer path) {
        return getMap(path.getPath());
    }

    /**
     * Retrieves a Map of any generic type.
     *
     * <p><b>Note</b>: Any objects which couldn't be casted won't be returned in the map.</p>
     *
     * @param <K> The key type to the map.
     * @param <V> The value type to the map.
     * @param path The path where the map is stored.
     * @return The map.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Map<K, V> getMap(String path) {
        final Map<K, V> keyMap = new HashMap<K, V>();

        for (String item : super.getConfigurationSection(path).getKeys(false)) {
            try {
                keyMap.put((K) item, (V) super.getConfigurationSection(path).get(item));
            } catch (Exception ex) {
                InternalExceptionHandler.handle(PLUGIN, ex);
            }
        }

        return keyMap;
    }

    /**
     * Saves the configuration to the predefined file.
     *
     * @see #YamlConfig(Plugin, String, boolean)
     */
    @Override
    public void save() {
        try {
            super.save(CONFIG_FILE);
        } catch (Exception ex) {
            InternalExceptionHandler.handle(PLUGIN, "Could not save configuration file: " + CONFIG_FILE.getName());
            InternalExceptionHandler.handle(PLUGIN, ex);
        }
    }

    /**
     * Loads the configuration from the predefined file.
     *
     * <p>Optionally, if loadDefaults has been set to true, the file will be copied over from the default inside the jar-file of the owning plugin.</p>
     *
     * @see #YamlConfig(Plugin, String, boolean)
     */
    @Override
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
            InternalExceptionHandler.handle(PLUGIN, "Could not load configuration file: " + CONFIG_FILE.getName());
            InternalExceptionHandler.handle(PLUGIN, ex);
        }
    }

    /**
     * Returns the raw YamlConfiguration this config is based on.
     *
     * @return The YamlConfiguration.
     * @see YamlConfiguration
     */
    @Override
    public YamlConfig getConfig() {
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
            InternalExceptionHandler.handle(PLUGIN, "Could not load default configuration: " + CONFIG_FILE.getName());
            InternalExceptionHandler.handle(PLUGIN, ex);
            return null;
        }
        return DEFAULT_CONFIG;
    }
}

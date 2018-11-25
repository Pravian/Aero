package net.pravian.aero.config;

import com.google.common.base.Preconditions;
import net.pravian.aero.plugin.AeroLogger;
import net.pravian.aero.plugin.AeroPlugin;
import net.pravian.aero.serializable.SerializableObject;
import net.pravian.aero.util.Plugins;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a definable YAML configuration.
 *
 * @see YamlConfiguration
 */
public class YamlConfig extends YamlConfiguration implements ConfigurationContainer<YamlConfig>
{

    private final AeroPlugin<?> plugin;
    private final AeroLogger logger;
    private final File configFile;
    private final boolean copyDefaults;

    /**
     * Creates a new YamlConfig instance.
     * <p>
     * <p>
     * Example:
     * <pre>
     * YamlConfig config = new YamlConfig(this, "config.yml");
     * config.load();
     * </pre></p>
     *
     * <p>
     * Note: This will automatically attempt to load the config from the the plugin archive.</p>
     *
     * @param plugin   The plugin to which the config belongs.
     * @param fileName The filename of the config file.
     */
    public YamlConfig(AeroPlugin<?> plugin, String fileName)
    {
        this(plugin, fileName, true);
    }

    /**
     * Creates a new YamlConfig instance.
     * <p>
     * <p>
     * Example:
     * <pre>
     * YamlConfig config = new YamlConfig(this, "config.yml", true);
     * config.load();
     * </pre></p>
     *
     * @param plugin       The plugin to which the config belongs.
     * @param fileName     The filename of the config file.
     * @param copyDefaults If the defaults should be copied and/loaded from a config in the plugin jar-file.
     */
    public YamlConfig(AeroPlugin<?> plugin, String fileName, boolean copyDefaults)
    {
        this(plugin, Plugins.getPluginFile(plugin, fileName), copyDefaults);
    }

    /**
     * Creates a new YamlConfig instance.
     * <p>
     * <p>
     * Example:
     * <pre>
     * YamlConfig config = new YamlConfig(this, new File(plugin.getDataFolder() + "/players", "DarthSalamon.yml"), false);
     * config.load();
     * </pre></p>
     *
     * @param plugin       The plugin to which the config belongs.
     * @param file         The file of the config file.
     * @param copyDefaults If the defaults should be copied and/loaded from a config in the plugin jar-file.
     */
    public YamlConfig(AeroPlugin<?> plugin, File file, boolean copyDefaults)
    {
        this(Preconditions.checkNotNull(plugin, "Plugin may not be null!"), plugin.getPluginLogger(), file, copyDefaults);
    }

    /**
     * Creates a new YamlConfig instance.
     * <p>
     * <p>
     * Example:
     * <pre>
     * YamlConfig config = new YamlConfig(this, new File(plugin.getDataFolder() + "/players", "DarthSalamon.yml"), false);
     * config.load();
     * </pre></p>
     *
     * @param plugin       The plugin to which the config belongs.
     * @param logger       The logger to use.
     * @param file         The file of the config file.
     * @param copyDefaults If the defaults should be copied and/loaded from a config in the plugin jar-file.
     */
    public YamlConfig(AeroPlugin<?> plugin, AeroLogger logger, File file, boolean copyDefaults)
    {
        this.plugin = Preconditions.checkNotNull(plugin, "Plugin may not be null!");
        this.logger = Preconditions.checkNotNull(logger, "Logger may not be null!");
        this.configFile = Preconditions.checkNotNull(file, "File may not be null!");
        this.copyDefaults = copyDefaults;
    }

    /**
     * Verifies if the config file exists.
     *
     * @return True if the config exists.
     */
    public boolean exists()
    {
        return configFile.exists();
    }

    /**
     * Loads the configuration from the predefined file.
     * <p>
     * <p>
     * Optionally, if loadDefaults has been set to true, the file will be copied over from the default inside the jar-file of the owning plugin.</p>
     *
     * @see #YamlConfig(Plugin, String, boolean)
     */
    @Override
    public void load()
    {
        try
        {
            if (copyDefaults)
            {
                if (!exists())
                {
                    configFile.getParentFile().mkdirs();
                    try
                    {
                        Plugins.copy(plugin.getResource(configFile.getName()), configFile);
                    }
                    catch (IOException ex)
                    {
                        plugin.handleException("Could not write default configuration file: " + configFile.getName(), ex);
                    }
                    logger.info("Installed default configuration " + configFile.getName());
                }

                super.addDefaults(getDefaultConfig());
            }

            if (configFile.exists())
            {
                super.load(configFile);
            }
        }
        catch (Exception ex)
        {
            plugin.handleException("Could not load configuration file: " + configFile.getName(), ex);
        }
    }

    /**
     * Saves the configuration to the predefined file.
     *
     * @see #YamlConfig(Plugin, String, boolean)
     */
    @Override
    public void save()
    {
        try
        {
            super.save(configFile);
        }
        catch (Exception ex)
        {
            plugin.handleException("Could not save configuration file: " + configFile.getName(), ex);
        }
    }

    /**
     * Deletes all the values in the config.
     */
    public void clear()
    {
        for (String key : super.getKeys(false))
        {
            super.set(key, null);
        }
    }

    /**
     * Deletes the config if it exists.
     */
    public void delete()
    {
        if (exists())
        {
            configFile.delete();
        }
    }

    /**
     * Returns the raw YamlConfiguration this config is based on.
     *
     * @return The YamlConfiguration.
     * @see YamlConfiguration
     */
    @Override
    public YamlConfig getConfig()
    {
        return this;
    }

    /**
     * Returns the default configuration as been stored in the jar-file of the owning plugin.
     *
     * @return The default configuration.
     */
    public YamlConfiguration getDefaultConfig()
    {
        final YamlConfiguration DEFAULT_CONFIG = new YamlConfiguration();
        try
        {
            DEFAULT_CONFIG.load(new InputStreamReader(plugin.getResource(configFile.getName())));
        }
        catch (Throwable ex)
        {
            plugin.handleException("Could not load default configuration: " + configFile.getName(), ex);
            return null;
        }
        return DEFAULT_CONFIG;
    }

    /**
     * @see #set(java.lang.String, java.lang.Object)
     */
    @Override
    public void set(PathContainer path, Object value)
    {
        super.set(path.getPath(), value);
    }

    /**
     * @see #getString(java.lang.String)
     */
    @Override
    public String getString(PathContainer path)
    {
        return super.getString(path.getPath());
    }

    /**
     * @see #getBoolean(java.lang.String)
     */
    @Override
    public boolean getBoolean(PathContainer path)
    {
        return super.getBoolean(path.getPath());
    }

    /**
     * @see #getInt(java.lang.String)
     */
    @Override
    public int getInt(PathContainer path)
    {
        return super.getInt(path.getPath());
    }

    /**
     * @see #getDouble(java.lang.String)
     */
    @Override
    public double getDouble(PathContainer path)
    {
        return super.getDouble(path.getPath());
    }

    /**
     * @see #getLong(java.lang.String)
     */
    @Override
    public long getLong(PathContainer path)
    {
        return super.getLong(path.getPath());
    }

    /**
     * @see #getColor(java.lang.String)
     */
    @Override
    public Color getColor(PathContainer path)
    {
        return super.getColor(path.getPath());
    }

    /**
     * @see #getItemStack(java.lang.String)
     */
    @Override
    public ItemStack getItemStack(PathContainer path)
    {
        return super.getItemStack(path.getPath());
    }

    /**
     * @see #getOfflinePlayer(java.lang.String)
     */
    @Override
    public OfflinePlayer getOfflinePlayer(PathContainer path)
    {
        return super.getOfflinePlayer(path.getPath());
    }

    /**
     * @see #getVector(java.lang.String)
     */
    @Override
    public Vector getVector(PathContainer path)
    {
        return super.getVector(path.getPath());
    }

    /**
     * @see #getStringList(java.lang.String)
     */
    @Override
    public List<String> getStringList(PathContainer path)
    {
        return super.getStringList(path.getPath());
    }

    /**
     * @see #getIntegerList(java.lang.String)
     */
    @Override
    public List<Integer> getIntegerList(PathContainer path)
    {
        return super.getIntegerList(path.getPath());
    }

    /**
     * @see #getCharacterList(java.lang.String)
     */
    @Override
    public List<Character> getCharacterList(PathContainer path)
    {
        return super.getCharacterList(path.getPath());
    }

    /**
     * @see #getBooleanList(java.lang.String)
     */
    @Override
    public List<Boolean> getBooleanList(PathContainer path)
    {
        return super.getBooleanList(path.getPath());
    }

    /**
     * @see #getByteList(java.lang.String)
     */
    @Override
    public List<Byte> getByteList(PathContainer path)
    {
        return super.getByteList(path.getPath());
    }

    /**
     * @see #getDoubleList(java.lang.String)
     */
    @Override
    public List<Double> getDoubleList(PathContainer path)
    {
        return super.getDoubleList(path.getPath());
    }

    /**
     * @see #getFloatList(java.lang.String)
     */
    @Override
    public List<Float> getFloatList(PathContainer path)
    {
        return super.getFloatList(path.getPath());
    }

    /**
     * @see #getLongList(java.lang.String)
     */
    @Override
    public List<Long> getLongList(PathContainer path)
    {
        return super.getLongList(path.getPath());
    }

    /**
     * @see #getShortList(java.lang.String)
     */
    @Override
    public List<Short> getShortList(PathContainer path)
    {
        return super.getShortList(path.getPath());
    }

    /**
     * @see #getList(java.lang.String)
     */
    @Override
    public List<?> getList(PathContainer path)
    {
        return super.getList(path.getPath());
    }

    /**
     * @see #getMapList(java.lang.String)
     */
    @Override
    public List<Map<?, ?>> getMapList(PathContainer path)
    {
        return super.getMapList(path.getPath());
    }

    @Override
    public Object getDefault(PathContainer path)
    {
        return super.getDefault(path.getPath());
    }

    /**
     * @see #setSerializable(java.lang.String, net.pravian.aero.serializable.SerializableObject)
     */
    @Override
    public void setSerializable(PathContainer path, SerializableObject<?> object)
    {
        setSerializable(path.getPath(), object);
    }

    /**
     * Stores an instance of SerializableObject.
     * <p>
     * <p>
     * <b>Warning</b>: SerializableObjects should never be stored using {@link #set(String, Object)}
     *
     * @param path
     * @param object
     * @see SerializableObject
     */
    @Override
    public void setSerializable(String path, SerializableObject<?> object)
    {
        super.set(path, object.serialize());
    }

    /**
     * @see #getSerializable(java.lang.String, java.lang.Class)
     */
    /*@Override
    public <T extends SerializableObject<T>> T getSerializable(PathContainer path, Class<T> type)
    {
        return getSerializable(path.getPath(), type);
    }*/

    /**
     * Retrieves a stored SerializableObject
     *
     * @param path The path where the SerializedObject is stored.
     * @return The retrieved item / null.
     * @see SerializableObject
     */
    /*@Override
    public <T extends SerializableObject<?>> T getSerializable(String path, Class<T> type)
    {
        final String serialized = super.getString(path);

        if (serialized == null)
        {
            return null;
        }

        final Constructor<T> cons;
        try
        {
            cons = type.getDeclaredConstructor(String.class);
        }
        catch (Exception ex)
        {
            plugin.handleException(ex);
            return null;
        }

        final T object;

        try
        {
            object = cons.newInstance(serialized);
        }
        catch (Exception ex)
        {
            plugin.handleException(ex);
            return null;
        }

        return object;
    }*/

    /**
     * Stores a Map of any generic type.
     *
     * @param <K>  The key type to the map.
     * @param <V>  The value type to the map.
     * @param path The path at which the map should be stored.
     * @param map  The map to store.
     */
    @Override
    public <K, V> void setMap(PathContainer path, Map<K, V> map)
    {
        setMap(path.getPath(), map);
    }

    /**
     * Stores a Map of any generic type.
     *
     * @param <K>  The key type to the map.
     * @param <V>  The value type to the map.
     * @param path The path at which the map should be stored.
     * @param map  The map to store.
     */
    @Override
    public <K, V> void setMap(String path, Map<K, V> map)
    {
        for (K key : map.keySet())
        {
            super.set(path + "." + key.toString(), map.get(key));
        }
    }

    /**
     * Retrieves a Map of any generic type.
     * <p>
     * <p>
     * <b>Note</b>: Any objects which couldn't be casted won't be returned in the map.</p>
     *
     * @param <K>  The key type to the map.
     * @param <V>  The value type to the map.
     * @param path The path where the map is stored.
     * @return The map.
     */
    @Override
    public <K, V> Map<K, V> getMap(PathContainer path)
    {
        return getMap(path.getPath());
    }

    /**
     * Retrieves a Map of any generic type.
     * <p>
     * <p>
     * <b>Note</b>: Any objects which couldn't be casted won't be returned in the map.</p>
     *
     * @param <K>  The key type to the map.
     * @param <V>  The value type to the map.
     * @param path The path where the map is stored.
     * @return The map.
     */
    @SuppressWarnings("unchecked")
    @Override
    // TODO fix
    public <K, V> Map<K, V> getMap(String path)
    {
        final Map<K, V> keyMap = new HashMap<K, V>();

        for (String item : super.getConfigurationSection(path).getKeys(false))
        {
            try
            {
                keyMap.put((K) item, (V) super.getConfigurationSection(path).get(item));
            }
            catch (Exception ex)
            {
                plugin.handleException(ex);
            }
        }

        return keyMap;
    }

    @Override
    public ConfigurationSection getConfigurationSection(PathContainer path)
    {
        return super.getConfigurationSection(path.getPath());
    }
}

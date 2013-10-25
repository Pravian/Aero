package net.pravian.bukkitlib.config;

import java.io.File;
import java.io.IOException;
import net.pravian.bukkitlib.serializable.SerializableBlock;
import net.pravian.bukkitlib.serializable.SerializableBlockLocation;
import net.pravian.bukkitlib.serializable.SerializableEntityLocation;
import net.pravian.bukkitlib.serializable.SerializableInventory;
import net.pravian.bukkitlib.serializable.SerializableObject;
import net.pravian.bukkitlib.utils.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class YamlConfig extends YamlConfiguration {

    private final Plugin PLUGIN;
    private final File CONFIG_FILE;
    private final boolean COPY_DEFAULTS;

    public YamlConfig(Plugin plugin, String fileName, boolean copyDefaults) {
        this.PLUGIN = plugin;
        this.CONFIG_FILE = FileUtils.getPluginFile(plugin, fileName);
        this.COPY_DEFAULTS = copyDefaults;
    }

    public void setSerializable(String path, SerializableObject object) {
        super.set(path, object.serialize());
    }

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

    public void save() {
        try {
            super.save(CONFIG_FILE);
        } catch (Exception ex) {
            PLUGIN.getLogger().severe("Could not save configuration file: " + CONFIG_FILE.getName());
            PLUGIN.getLogger().severe(ExceptionUtils.getStackTrace(ex));
        }
    }

    public void load() {
        try {
            if (COPY_DEFAULTS) {
                if (!CONFIG_FILE.exists()) {
                    CONFIG_FILE.getParentFile().mkdirs();
                    try {
                        FileUtils.copy(PLUGIN.getResource(CONFIG_FILE.getName()), CONFIG_FILE);
                    } catch (IOException e) {
                        PLUGIN.getLogger().severe("Could not write default configuration file: " + CONFIG_FILE.getName());
                        PLUGIN.getLogger().severe(ExceptionUtils.getStackTrace(e));
                    }
                    PLUGIN.getLogger().info("Installed default configuration " + CONFIG_FILE.getName());
                }

                super.addDefaults(getDefaultConfig());
            }

            super.load(CONFIG_FILE);
        } catch (Exception ex) {
            PLUGIN.getLogger().severe("Could not load configuration file: " + CONFIG_FILE.getName());
            PLUGIN.getLogger().severe(ExceptionUtils.getStackTrace(ex));
        }
    }

    public YamlConfiguration getConfig() {
        return this;
    }

    public YamlConfiguration getDefaultConfig() {
        final YamlConfiguration DEFAULT_CONFIG = new YamlConfiguration();
        try {
            DEFAULT_CONFIG.load(PLUGIN.getResource(CONFIG_FILE.getName()));
        } catch (Throwable ex) {
            PLUGIN.getLogger().severe("Could not load default configuration: " + CONFIG_FILE.getName());
            PLUGIN.getLogger().severe(ExceptionUtils.getStackTrace(ex));
            return null;
        }
        return DEFAULT_CONFIG;
    }
}

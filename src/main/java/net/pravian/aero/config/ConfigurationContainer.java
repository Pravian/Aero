package net.pravian.aero.config;

import net.pravian.aero.serializable.SerializableObject;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

public interface ConfigurationContainer<T extends ConfigurationSection> extends ConfigurationSection
{

    public void set(PathContainer path, Object value);

    public String getString(PathContainer path);

    public boolean getBoolean(PathContainer path);

    public ConfigurationSection getConfigurationSection(PathContainer container);

    public int getInt(PathContainer path);

    public double getDouble(PathContainer path);

    public long getLong(PathContainer path);

    public Color getColor(PathContainer path);

    public ItemStack getItemStack(PathContainer path);

    public OfflinePlayer getOfflinePlayer(PathContainer path);

    public Vector getVector(PathContainer path);

    public List<String> getStringList(PathContainer path);

    public List<Integer> getIntegerList(PathContainer path);

    public List<Character> getCharacterList(PathContainer path);

    public List<Boolean> getBooleanList(PathContainer path);

    public List<Byte> getByteList(PathContainer path);

    public List<Double> getDoubleList(PathContainer path);

    public List<Float> getFloatList(PathContainer path);

    public List<Long> getLongList(PathContainer path);

    public List<Short> getShortList(PathContainer path);

    public List<?> getList(PathContainer path);

    public List<Map<?, ?>> getMapList(PathContainer path);

    public Object getDefault(PathContainer path);

    public void setSerializable(PathContainer path, SerializableObject<?> object);

    public void setSerializable(String path, SerializableObject<?> object);

    //public <T extends SerializableObject<?>> T getSerializable(PathContainer path, Class<T> type);

    //public <T extends SerializableObject<?>> T getSerializable(String path, Class<T> type);

    public <K, V> void setMap(PathContainer path, Map<K, V> map);

    public <K, V> void setMap(String path, Map<K, V> map);

    public <K, V> Map<K, V> getMap(PathContainer path);

    public <K, V> Map<K, V> getMap(String path);

    public void save();

    public void load();

    public T getConfig();
}

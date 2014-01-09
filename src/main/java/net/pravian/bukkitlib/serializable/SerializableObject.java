package net.pravian.bukkitlib.serializable;

/**
 * Represents a serializable object.
 *
 * <p>Bukkit, by default comes with no functionality to serialize commonly used objects (Location, Inventory, etc) to Strings and back. The classes in this package are easy to use and fit to
 * the needs of those who need to store those objects in configuration files.</p>
 *
 * <p>{@link net.pravian.bukkitlib.config.YamlConfig#setSerializable(String, SerializableObject)} has easy methods to save and load SerializableObjects.</p>
 *
 * @param <T> Optional type-safety for the object.
 * @see net.pravian.bukkitlib.config.YamlConfig
 */
public abstract class SerializableObject<T> {

    /**
     * Serializes the Object to a String.
     *
     * @return The serialized object in String-form.
     */
    public abstract String serialize();

    /**
     * De-serializes the String to a SerializableObject
     *
     * @return The de-serialized String.
     */
    public abstract T deserialize();

    /**
     * Serializes the Object to a String.
     *
     * <p><b>Warning</b>: The use of this method is not recommended, instead, use {@link #serialize()}.
     *
     * @return The serialized object in String-form.
     */
    @Override
    public String toString() {
        return serialize();
    }
}

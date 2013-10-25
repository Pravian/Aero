package net.pravian.bukkitlib.serializable;

public abstract class SerializableObject<T> {

    public abstract String serialize();

    public abstract T deserialize();

    @Override
    public String toString() {
        return serialize();
    }
}

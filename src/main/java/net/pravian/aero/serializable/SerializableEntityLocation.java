package net.pravian.aero.serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Represents a serializable entity location.
 *
 * @see SerializableObject
 */
public class SerializableEntityLocation extends SerializableObject<Location>
{

    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final String worldName;

    /**
     * Creates a new SerializableEntityLocation instance.
     *
     * @param location The Location to be serialized.
     */
    public SerializableEntityLocation(Location location)
    {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        this.worldName = location.getWorld().getName();
    }

    /**
     * Creates a new SerializableBlock instance.
     *
     * @param location The String to serialize from.
     */
    public SerializableEntityLocation(String location)
    {
        if (location == null || location.equals("") || location.split(":").length != 6)
        {
            this.worldName = null;
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.yaw = 0;
            this.pitch = 0;
            return;
        }
        final String[] locationParts = location.split(":");
        this.worldName = locationParts[0];
        this.x = Double.valueOf(locationParts[1]);
        this.y = Double.valueOf(locationParts[2]);
        this.z = Double.valueOf(locationParts[3]);
        this.yaw = Float.valueOf(locationParts[4]);
        this.pitch = Float.valueOf(locationParts[5]);
    }

    @Override
    public String serialize()
    {
        return worldName + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch;
    }

    @Override
    public Location deserialize()
    {
        try
        {
            return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        }
        catch (NullPointerException ex)
        {
            return null;
        }
    }
}

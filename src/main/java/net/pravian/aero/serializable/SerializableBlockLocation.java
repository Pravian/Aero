package net.pravian.aero.serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Represents a serializable block location
 *
 * @see SerializableObject
 */
public class SerializableBlockLocation extends SerializableObject<Location>
{

    private final int x;
    private final int y;
    private final int z;
    private final String worldName;

    /**
     * Creates a new SerializableBlockLocation instance.
     *
     * @param location The Location to be serialized.
     */
    public SerializableBlockLocation(Location location)
    {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.worldName = location.getWorld().getName();
    }

    /**
     * Creates a new SerializableBlock instance.
     *
     * @param location The String to serialize from.
     */
    public SerializableBlockLocation(String location)
    {
        if (location == null || location.isEmpty() || location.split(":").length != 4)
        {
            this.worldName = null;
            this.x = 0;
            this.y = 0;
            this.z = 0;
            return;
        }
        final String[] locationParts = location.split(":");
        this.worldName = locationParts[0];
        this.x = Integer.valueOf(locationParts[1]);
        this.y = Integer.valueOf(locationParts[2]);
        this.z = Integer.valueOf(locationParts[3]);
    }

    /**
     * Returns the block at the location.
     *
     * @return The block.
     */
    public Block getBlock()
    {
        World world = Bukkit.getWorld(worldName);
        if (world == null)
        {
            return null;
        }

        return world.getBlockAt(x, y, z);
    }

    @Override
    public String serialize()
    {
        return worldName + ":" + x + ":" + y + ":" + z;
    }

    @Override
    public Location deserialize()
    {
        try
        {
            return getBlock().getLocation();
        }
        catch (NullPointerException ex)
        {
            return null;
        }
    }
}

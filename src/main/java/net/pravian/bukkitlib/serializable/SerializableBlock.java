package net.pravian.bukkitlib.serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Represents a serializable Block
 *
 * @see SerializableObject
 */
@SuppressWarnings("deprecation")
public class SerializableBlock extends SerializableObject<Block> {

    private final String worldName;
    private final int x;
    private final int y;
    private final int z;
    private final int id;
    private final byte data;

    /**
     * Creates a new SerializableBlock instance.
     *
     * @param block The Block to be serialized.
     */
    public SerializableBlock(Block block) {
        this.worldName = block.getLocation().getWorld().getName();
        this.x = block.getLocation().getBlockX();
        this.y = block.getLocation().getBlockY();
        this.z = block.getLocation().getBlockZ();
        this.id = block.getTypeId();
        this.data = block.getData();
    }

    /**
     * Creates a new SerializableBlock instance.
     *
     * @param block The String to serialize from.
     */
    public SerializableBlock(String block) {
        if (block == null || block.isEmpty() || block.split(":").length != 4) {
            this.worldName = null;
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.id = 0;
            this.data = 0;
            return;
        }

        final String[] blockParts = block.split(":");
        this.worldName = blockParts[0];
        this.x = Integer.valueOf(blockParts[1]);
        this.y = Integer.valueOf(blockParts[2]);
        this.z = Integer.valueOf(blockParts[3]);
        this.id = Integer.valueOf(blockParts[4]);
        this.data = Byte.valueOf(blockParts[5]);
    }

    /**
     * Returns the Material-ID the block is made of.
     *
     * @return The material-ID;
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the Material the block is made of.
     *
     * @return The material.
     */
    public Material getType() {
        return Material.getMaterial(id);
    }

    /**
     * The block-specific data of the block.
     *
     * @return The data.
     */
    public byte getData() {
        return data;
    }

    /**
     * Sets the block at the location to the stored id and data value
     */
    public void put() {
        getLocation().getBlock().setTypeIdAndData(id, data, false);
    }

    /**
     * The Location the block is located at.
     *
     * @return The Location
     */
    public Location getLocation() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }

        return world.getBlockAt(x, y, z).getLocation();
    }

    @Override
    public String serialize() {
        return worldName + ":" + x + ":" + y + ":" + z + ":" + id + ":" + data;
    }

    @Override
    public Block deserialize() {
        final Location location = getLocation();
        if (location == null) {
            return null;
        }

        return location.getBlock();
    }
}

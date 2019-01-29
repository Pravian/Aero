package net.pravian.aero.serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

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
  private final String name;
  private final BlockData data;

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
    this.name = block.toString();
    this.data = block.getBlockData();
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
      this.name = null;
      this.data = null;
      return;
    }

    final String[] blockParts = block.split(":");
    this.worldName = blockParts[0];
    this.x = Integer.valueOf(blockParts[1]);
    this.y = Integer.valueOf(blockParts[2]);
    this.z = Integer.valueOf(blockParts[3]);
    this.name = blockParts[4];
    this.data = Material.getMaterial(name).createBlockData();
  }

  /**
   * Returns the Material the block is made of.
   *
   * @return The material.
   */
  public Material getType() {
    return Material.getMaterial(name);
  }

  /**
   * The block-specific data of the block.
   *
   * @return The data.
   */
  public BlockData getData() {
    return data;
  }

  /** Sets the block at the location to the stored id and data value */
  public void put() {
    getLocation().getBlock().setBlockData(data);
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
    return worldName + ":" + x + ":" + y + ":" + z + ":" + name + ":" + data.getAsString();
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

package net.pravian.bukkitlib.serializable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SerializableBlock extends SerializableObject<Block> {

    private final String worldName;
    private final int x;
    private final int y;
    private final int z;
    private final int id;
    private final byte data;

    public SerializableBlock(Block block) {
        this.worldName = block.getLocation().getWorld().getName();
        this.x = block.getLocation().getBlockX();
        this.y = block.getLocation().getBlockY();
        this.z = block.getLocation().getBlockZ();
        this.id = block.getType().getId();
        this.data = block.getData();
    }

    public SerializableBlock(String block) {
        if (block == null || block.equals("") || block.split(":").length != 4) {
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

    public int getId() {
        return id;
    }

    public Material getType() {
        return Material.getMaterial(id);
    }

    public byte getData() {
        return data;
    }

    @Override
    public String serialize() {
        return worldName + x + ":" + y + ":" + z + ":" + id + ":" + data;
    }

    @Override
    public Block deserialize() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }

        return world.getBlockAt(x, y, z);
    }
}

package net.pravian.bukkitlib.util;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockUtils {

    /**
     * Gets the chunk a Block Location is in<br>
     * <b>Will load the chunk if it isn't loaded</b>
     *
     * <p>Author: bergerkiller</p>
     * 
     * @param block to check
     * @return the Chunk, or null if the world this location is in is not loaded
     */
    public static Chunk getChunk(Block block) {
        final World world = block.getWorld();
        if (world == null) {
            return null;
        }
        return world.getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    /**
     * Checks if this Block Location is within the boundaries of a chunk
     *
     * <p>Author: bergerkiller</p>
     * 
     * @param block to check
     * @param chunk to check
     * @return true if within, false if not
     */
    public static boolean isIn(Block block, Chunk chunk) {
        return (block.getX() >> 4) == chunk.getX() && (block.getZ() >> 4) == chunk.getZ();
    }
}

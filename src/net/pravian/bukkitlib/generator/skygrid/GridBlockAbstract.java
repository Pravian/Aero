package net.pravian.bukkitlib.generator.skygrid;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class GridBlockAbstract {

    public final int x;
    public final int y;
    public final int z;

    public GridBlockAbstract(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Block getBlock(Chunk chunk) {
        return chunk.getBlock(this.x * 4, this.y * 4, this.z * 4);
    }

    @Override
    public String toString() {
        return "(" + Integer.toString(this.x) + ", "
                + Integer.toString(this.y) + ", "
                + Integer.toString(this.z) + ")";
    }
}

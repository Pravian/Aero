package net.pravian.bukkitlib.generator.skygrid;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

public class RandomBlockSeries {

    private GridBlockAbstract[] reset;
    private GridBlockAbstract[] series;
    private int pos;
    public final int height;

    public static int maxHeight(World world, int size) {
        if (world.getMaxHeight() < size) {
            return world.getMaxHeight();
        }
        return size;
    }

    public RandomBlockSeries(World world, int size) {
        int t = maxHeight(world, size);
        t -= t % 16;
        this.height = t;

        this.reset = new GridBlockAbstract[4 * this.height];

        int i = 0;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < this.height / 4; y++) {
                for (int z = 0; z < 4; z++) {
                    this.reset[i] = new GridBlockAbstract(x, y, z);
                    i++;
                }
            }
        }

        reset();
    }

    public void reset() {
        this.pos = 0;
        this.series = ((GridBlockAbstract[]) this.reset.clone());
    }

    public Block nextBlock(Chunk chunk, Random random) {
        return nextAbstractBlock(random).getBlock(chunk);
    }

    public GridBlockAbstract nextAbstractBlock(Random random) {
        int swap = this.pos + random.nextInt(this.series.length - this.pos);
        GridBlockAbstract temp = this.series[this.pos];
        this.series[this.pos] = this.series[swap];
        this.series[swap] = temp;
        return this.series[(this.pos++)];
    }
}
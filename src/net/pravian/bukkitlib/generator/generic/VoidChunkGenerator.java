package net.pravian.bukkitlib.generator.generic;


import java.util.Random;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;


public class VoidChunkGenerator extends ChunkGenerator {

    @Override
    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
        byte[][] result = new byte[world.getMaxHeight() / 16][];
        if ((chunkX == 0) && (chunkZ == 0)) {
            setBlock(result, 0, 64, 0, (byte) 7);
        }
        return result;
    }

    void setBlock(byte[][] result, int x, int y, int z, byte blkid) {
        if (result[(y >> 4)] == null) {
            result[(y >> 4)] = new byte[4096];
        }
        result[(y >> 4)][((y & 0xF) << 8 | z << 4 | x)] = blkid;
    }
}
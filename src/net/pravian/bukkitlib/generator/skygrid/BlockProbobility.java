package net.pravian.bukkitlib.generator.skygrid;

import java.util.Random;
import java.util.TreeMap;

/**
 * Represents BlockProbabilty.
 */
public class BlockProbobility {

    TreeMap<Integer, Byte> p = new TreeMap();
    int total = 0;

    public void addBlock(int id, int prob) {
        addBlock((byte) id, prob);
    }

    public void addBlock(byte id, int prob) {
        this.p.put(Integer.valueOf(this.total), Byte.valueOf(id));
        this.total += prob;
    }

    public byte getBlock(Random random, boolean bottom, boolean b) {
        byte temp = ((Byte) this.p.floorEntry(Integer.valueOf(random.nextInt(this.total))).getValue()).byteValue();
        if ((bottom) && (temp == 81)) {
            return getBlock(random, bottom, b);
        }
        if ((b) && ((temp == 9) || (temp == 11))) {
            return getBlock(random, bottom, b);
        }
        return temp;
    }
}
package net.pravian.bukkitlib.generator.skygrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

/**
 * Represents WorldStyles.
 */
public class WorldStyles {

    private static final Map<World.Environment, WorldStyles> map = new HashMap();
    public final BlockProbobility p;
    public final List<EntityType> spawns;

    static {
        map.put(World.Environment.NORMAL, new WorldStyles(normalP(), normalSpawns()));
        map.put(World.Environment.NETHER, new WorldStyles(netherP(), netherSpawns()));
    }

    private WorldStyles(BlockProbobility prob, List<EntityType> spwn) {
        this.p = prob;
        this.spawns = spwn;
    }

    public static WorldStyles get(World.Environment style) {
        if (!map.containsKey(style)) {
            throw new Error("SkyGrid can only generate The Overworld and The Nether");
        }
        return (WorldStyles) map.get(style);
    }

    private static BlockProbobility normalP() {
        BlockProbobility p = new BlockProbobility();
        p.addBlock(1, 120);
        p.addBlock(2, 80);
        p.addBlock(3, 20);
        p.addBlock(9, 10);
        p.addBlock(11, 5);
        p.addBlock(12, 20);
        p.addBlock(13, 10);
        p.addBlock(14, 10);
        p.addBlock(15, 20);
        p.addBlock(16, 40);
        p.addBlock(17, 100);
        p.addBlock(18, 40);
        p.addBlock(20, 1);
        p.addBlock(21, 5);
        p.addBlock(24, 10);
        p.addBlock(29, 1);
        p.addBlock(30, 10);
        p.addBlock(31, 3);
        p.addBlock(32, 3);
        p.addBlock(33, 1);
        p.addBlock(35, 25);
        p.addBlock(37, 2);
        p.addBlock(38, 2);
        p.addBlock(39, 2);
        p.addBlock(40, 2);
        p.addBlock(46, 2);
        p.addBlock(47, 3);
        p.addBlock(48, 5);
        p.addBlock(49, 5);

        p.addBlock(56, 1);
        p.addBlock(73, 8);
        p.addBlock(79, 4);
        p.addBlock(80, 8);
        p.addBlock(81, 1);
        p.addBlock(82, 20);
        p.addBlock(83, 15);
        p.addBlock(86, 5);
        p.addBlock(103, 5);
        p.addBlock(110, 15);
        return p;
    }

    private static BlockProbobility netherP() {
        BlockProbobility p = new BlockProbobility();
        p.addBlock(11, 50);
        p.addBlock(13, 30);

        p.addBlock(87, 300);
        p.addBlock(88, 100);
        p.addBlock(89, 50);
        p.addBlock(112, 30);
        p.addBlock(113, 10);
        p.addBlock(114, 15);
        p.addBlock(115, 30);
        return p;
    }

    private static List<EntityType> normalSpawns() {
        List s = new ArrayList();
        s.add(EntityType.CREEPER);
        s.add(EntityType.SKELETON);
        s.add(EntityType.SPIDER);
        s.add(EntityType.CAVE_SPIDER);
        s.add(EntityType.ZOMBIE);
        s.add(EntityType.SLIME);
        s.add(EntityType.PIG);
        s.add(EntityType.SHEEP);
        s.add(EntityType.COW);
        s.add(EntityType.CHICKEN);
        s.add(EntityType.SQUID);
        s.add(EntityType.WOLF);
        s.add(EntityType.ENDERMAN);
        s.add(EntityType.SILVERFISH);
        s.add(EntityType.VILLAGER);
        return s;
    }

    private static List<EntityType> netherSpawns() {
        List s = new ArrayList();
        s.add(EntityType.PIG_ZOMBIE);
        s.add(EntityType.BLAZE);
        s.add(EntityType.MAGMA_CUBE);
        return s;
    }

    public static double getSProb(World world, int size, int it) {
        int x = get(world.getEnvironment()).p.total;
        int y = 4 * RandomBlockSeries.maxHeight(world, size);
        int n = world.getEnvironment() == World.Environment.NETHER ? 3 : 2;
        x += n;
        return 1.0D - Math.pow((x - n) / x, y - it);
    }

    public static boolean isChest(World world, Random random) {
        switch (world.getEnvironment().ordinal()) {
            case 1:
                return random.nextDouble() < 0.5D;
            case 2:
                return random.nextDouble() < 0.0D;
        }
        return false;
    }
}
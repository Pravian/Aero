package net.pravian.bukkitlib.util;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;

@SuppressWarnings("deprecation")
public class MaterialUtils {

    /**
     * <p>Author: Essentials Team</p>
     */
    protected static final HashSet<Byte> TRANSPARENT_MATERIALS = new HashSet<Byte>();

    static {
        final HashSet<Integer> MATERIALS = new HashSet<Integer>();
        MATERIALS.add(Material.AIR.getId());
        MATERIALS.add(Material.SAPLING.getId());
        MATERIALS.add(Material.POWERED_RAIL.getId());
        MATERIALS.add(Material.DETECTOR_RAIL.getId());
        MATERIALS.add(Material.LONG_GRASS.getId());
        MATERIALS.add(Material.DEAD_BUSH.getId());
        MATERIALS.add(Material.YELLOW_FLOWER.getId());
        MATERIALS.add(Material.RED_ROSE.getId());
        MATERIALS.add(Material.BROWN_MUSHROOM.getId());
        MATERIALS.add(Material.RED_MUSHROOM.getId());
        MATERIALS.add(Material.TORCH.getId());
        MATERIALS.add(Material.REDSTONE_WIRE.getId());
        MATERIALS.add(Material.SEEDS.getId());
        MATERIALS.add(Material.SIGN_POST.getId());
        MATERIALS.add(Material.WOODEN_DOOR.getId());
        MATERIALS.add(Material.LADDER.getId());
        MATERIALS.add(Material.RAILS.getId());
        MATERIALS.add(Material.WALL_SIGN.getId());
        MATERIALS.add(Material.LEVER.getId());
        MATERIALS.add(Material.STONE_PLATE.getId());
        MATERIALS.add(Material.IRON_DOOR_BLOCK.getId());
        MATERIALS.add(Material.WOOD_PLATE.getId());
        MATERIALS.add(Material.REDSTONE_TORCH_OFF.getId());
        MATERIALS.add(Material.REDSTONE_TORCH_ON.getId());
        MATERIALS.add(Material.STONE_BUTTON.getId());
        MATERIALS.add(Material.SNOW.getId());
        MATERIALS.add(Material.SUGAR_CANE_BLOCK.getId());
        MATERIALS.add(Material.DIODE_BLOCK_OFF.getId());
        MATERIALS.add(Material.DIODE_BLOCK_ON.getId());
        MATERIALS.add(Material.PUMPKIN_STEM.getId());
        MATERIALS.add(Material.MELON_STEM.getId());
        MATERIALS.add(Material.VINE.getId());
        MATERIALS.add(Material.FENCE_GATE.getId());
        MATERIALS.add(Material.WATER_LILY.getId());
        MATERIALS.add(Material.NETHER_WARTS.getId());
        MATERIALS.add(Material.WATER.getId());
        MATERIALS.add(Material.LAVA.getId());

        try // 1.6 update
        {
            MATERIALS.add(Material.CARPET.getId());
        } catch (NoSuchFieldError e) {
        }

        for (Integer integer : MATERIALS) {
            TRANSPARENT_MATERIALS.add(integer.byteValue());
        }
    }

    /**
     * Returns all transparent materials
     *
     * @return the materials
     */
    public static Set<Material> getTransparentMaterials() {
        final Set<Material> materials = new HashSet<Material>();

        for (Byte material : TRANSPARENT_MATERIALS) {
            materials.add(Material.getMaterial(material));
        }

        return materials;
    }

    private MaterialUtils() {
    }
}

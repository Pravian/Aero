package net.pravian.aero.util;

import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("deprecation")
public class Materials
{

    /**
     * <p>
     * Author: Essentials Team</p>
     */
    protected static final HashSet<String> TRANSPARENT_MATERIALS = new HashSet<String>();

    static
    {
        final HashSet<String> MATERIALS = new HashSet<>();
        MATERIALS.add(Material.AIR.toString());
        MATERIALS.add(Material.LEGACY_SAPLING.toString());
        MATERIALS.add(Material.POWERED_RAIL.toString());
        MATERIALS.add(Material.DETECTOR_RAIL.toString());
        MATERIALS.add(Material.LEGACY_LONG_GRASS.toString());
        MATERIALS.add(Material.DEAD_BUSH.toString());
        MATERIALS.add(Material.LEGACY_YELLOW_FLOWER.toString());
        MATERIALS.add(Material.LEGACY_RED_ROSE.toString());
        MATERIALS.add(Material.BROWN_MUSHROOM.toString());
        MATERIALS.add(Material.RED_MUSHROOM.toString());
        MATERIALS.add(Material.TORCH.toString());
        MATERIALS.add(Material.REDSTONE_WIRE.toString());
        MATERIALS.add(Material.LEGACY_SEEDS.toString());
        MATERIALS.add(Material.LEGACY_SIGN_POST.toString());
        MATERIALS.add(Material.LEGACY_WOODEN_DOOR.toString());
        MATERIALS.add(Material.LADDER.toString());
        MATERIALS.add(Material.LEGACY_RAILS.toString());
        MATERIALS.add(Material.WALL_SIGN.toString());
        MATERIALS.add(Material.LEVER.toString());
        MATERIALS.add(Material.LEGACY_STONE_PLATE.toString());
        MATERIALS.add(Material.LEGACY_IRON_DOOR_BLOCK.toString());
        MATERIALS.add(Material.LEGACY_WOOD_PLATE.toString());
        MATERIALS.add(Material.LEGACY_REDSTONE_TORCH_OFF.toString());
        MATERIALS.add(Material.LEGACY_REDSTONE_TORCH_ON.toString());
        MATERIALS.add(Material.STONE_BUTTON.toString());
        MATERIALS.add(Material.SNOW.toString());
        MATERIALS.add(Material.LEGACY_SUGAR_CANE_BLOCK.toString());
        MATERIALS.add(Material.LEGACY_DIODE_BLOCK_OFF.toString());
        MATERIALS.add(Material.LEGACY_DIODE_BLOCK_ON.toString());
        MATERIALS.add(Material.PUMPKIN_STEM.toString());
        MATERIALS.add(Material.MELON_STEM.toString());
        MATERIALS.add(Material.VINE.toString());
        MATERIALS.add(Material.LEGACY_FENCE_GATE.toString());
        MATERIALS.add(Material.LEGACY_WATER_LILY.toString());
        MATERIALS.add(Material.LEGACY_NETHER_WARTS.toString());
        MATERIALS.add(Material.WATER.toString());
        MATERIALS.add(Material.LAVA.toString());

        try // 1.6 update
        {
            MATERIALS.add(Material.LEGACY_CARPET.toString());
        }
        catch (NoSuchFieldError e)
        {
        }

        for (String string : MATERIALS)
        {
            TRANSPARENT_MATERIALS.add(string);
        }
    }

    private Materials()
    {
    }

    /**
     * Returns all transparent materials
     *
     * @return the materials
     */
    public static Set<Material> getTransparentMaterials()
    {
        final Set<Material> materials = new HashSet<Material>();

        for (String material : TRANSPARENT_MATERIALS)
        {
            materials.add(Material.getMaterial(material));
        }

        return materials;
    }
}

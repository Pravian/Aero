package net.pravian.aero.util;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;

@SuppressWarnings("deprecation")
public class Materials {

  /**
   * <p>
   * Author: Essentials Team
   */
  protected static final HashSet<String> TRANSPARENT_MATERIALS = new HashSet<String>();

  static {
    final HashSet<String> MATERIALS = new HashSet<>();
    MATERIALS.add(Material.AIR.toString());
    MATERIALS.add(Material.ACACIA_SAPLING.toString());
    MATERIALS.add(Material.BIRCH_SAPLING.toString());
    MATERIALS.add(Material.DARK_OAK_SAPLING.toString());
    MATERIALS.add(Material.JUNGLE_SAPLING.toString());
    MATERIALS.add(Material.OAK_SAPLING.toString());
    MATERIALS.add(Material.SPRUCE_SAPLING.toString());
    MATERIALS.add(Material.POWERED_RAIL.toString());
    MATERIALS.add(Material.DETECTOR_RAIL.toString());
    MATERIALS.add(Material.TALL_GRASS.toString());
    MATERIALS.add(Material.DEAD_BUSH.toString());
    MATERIALS.add(Material.CHORUS_FLOWER.toString());
    MATERIALS.add(Material.SUNFLOWER.toString());
    MATERIALS.add(Material.ROSE_RED.toString());
    MATERIALS.add(Material.BROWN_MUSHROOM.toString());
    MATERIALS.add(Material.RED_MUSHROOM.toString());
    MATERIALS.add(Material.TORCH.toString());
    MATERIALS.add(Material.REDSTONE_WIRE.toString());
    MATERIALS.add(Material.BEETROOT_SEEDS.toString());
    MATERIALS.add(Material.MELON_SEEDS.toString());
    MATERIALS.add(Material.PUMPKIN_SEEDS.toString());
    MATERIALS.add(Material.WHEAT_SEEDS.toString());
    MATERIALS.add(Material.DARK_OAK_DOOR.toString());
    MATERIALS.add(Material.ACACIA_DOOR.toString());
    MATERIALS.add(Material.BIRCH_DOOR.toString());
    MATERIALS.add(Material.JUNGLE_DOOR.toString());
    MATERIALS.add(Material.OAK_DOOR.toString());
    MATERIALS.add(Material.SPRUCE_DOOR.toString());
    MATERIALS.add(Material.IRON_DOOR.toString());
    MATERIALS.add(Material.LADDER.toString());
    MATERIALS.add(Material.WALL_SIGN.toString());
    MATERIALS.add(Material.LEVER.toString());
    MATERIALS.add(Material.ACACIA_PRESSURE_PLATE.toString());
    MATERIALS.add(Material.BIRCH_PRESSURE_PLATE.toString());
    MATERIALS.add(Material.DARK_OAK_PRESSURE_PLATE.toString());
    MATERIALS.add(Material.JUNGLE_PRESSURE_PLATE.toString());
    MATERIALS.add(Material.OAK_PRESSURE_PLATE.toString());
    MATERIALS.add(Material.SPRUCE_PRESSURE_PLATE.toString());
    MATERIALS.add(Material.STONE_PRESSURE_PLATE.toString());
    MATERIALS.add(Material.STONE_BUTTON.toString());
    MATERIALS.add(Material.REDSTONE_TORCH.toString());
    MATERIALS.add(Material.REDSTONE_WALL_TORCH.toString());
    MATERIALS.add(Material.SNOW.toString());
    MATERIALS.add(Material.SUGAR_CANE.toString());
    MATERIALS.add(Material.PUMPKIN_STEM.toString());
    MATERIALS.add(Material.MELON_STEM.toString());
    MATERIALS.add(Material.VINE.toString());
    MATERIALS.add(Material.ACACIA_FENCE_GATE.toString());
    MATERIALS.add(Material.BIRCH_FENCE_GATE.toString());
    MATERIALS.add(Material.DARK_OAK_FENCE_GATE.toString());
    MATERIALS.add(Material.JUNGLE_FENCE_GATE.toString());
    MATERIALS.add(Material.NETHER_BRICK_FENCE.toString());
    MATERIALS.add(Material.OAK_FENCE_GATE.toString());
    MATERIALS.add(Material.SPRUCE_FENCE_GATE.toString());
    MATERIALS.add(Material.LILY_PAD.toString());
    MATERIALS.add(Material.NETHER_WART.toString());
    MATERIALS.add(Material.WATER.toString());
    MATERIALS.add(Material.LAVA.toString());
    MATERIALS.add(Material.CYAN_CARPET.toString());
    MATERIALS.add(Material.BLACK_CARPET.toString());
    MATERIALS.add(Material.BLUE_CARPET.toString());
    MATERIALS.add(Material.BROWN_CARPET.toString());
    MATERIALS.add(Material.GRAY_CARPET.toString());
    MATERIALS.add(Material.GREEN_CARPET.toString());
    MATERIALS.add(Material.LIGHT_BLUE_CARPET.toString());
    MATERIALS.add(Material.LIGHT_GRAY_CARPET.toString());
    MATERIALS.add(Material.LIME_CARPET.toString());
    MATERIALS.add(Material.MAGENTA_CARPET.toString());
    MATERIALS.add(Material.ORANGE_CARPET.toString());
    MATERIALS.add(Material.PINK_CARPET.toString());
    MATERIALS.add(Material.PURPLE_CARPET.toString());
    MATERIALS.add(Material.RED_CARPET.toString());
    MATERIALS.add(Material.WHITE_CARPET.toString());
    MATERIALS.add(Material.YELLOW_CARPET.toString());

    for (String string : MATERIALS) {
      TRANSPARENT_MATERIALS.add(string);
    }
  }

  private Materials() {}

  /**
   * Returns all transparent materials
   *
   * @return the materials
   */
  public static Set<Material> getTransparentMaterials() {
    final Set<Material> materials = new HashSet<Material>();

    for (String material : TRANSPARENT_MATERIALS) {
      materials.add(Material.getMaterial(material));
    }

    return materials;
  }
}

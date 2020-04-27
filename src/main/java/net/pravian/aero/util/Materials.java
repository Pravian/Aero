package net.pravian.aero.util;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;

@SuppressWarnings("deprecation")
public class Materials {

    /**
     * <p>
     * Author: Essentials Team</p>
     */
    public static final HashSet<Material> TRANSPARENT_MATERIALS = new HashSet<Material>();

    static {
        // Materials from Material.isTransparent()
        for (Material mat : Material.values()) {
            if (mat.isTransparent()) {
                TRANSPARENT_MATERIALS.add(mat);
            }
        }

        // Add water types
        TRANSPARENT_MATERIALS.addAll(Enums.getAllMatching(Material.class, "WATER", "FLOWING_WATER"));
    }

    private Materials() {
    }

    /**
     * Returns all transparent materials
     *
     * @return the materials
     * @deprecated Use TRANSPARENT_MATERIALS
     */
    public static Set<Material> getTransparentMaterials() {
        return TRANSPARENT_MATERIALS;
    }
}

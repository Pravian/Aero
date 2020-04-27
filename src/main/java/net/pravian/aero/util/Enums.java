package net.pravian.aero.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

/**
 * Represents all Enums-related utilities.
 * 
 * @author EssentialsX - https://github.com/EssentialsX/Essentials/blob/74d96ce6249e0c53d25ec7e03197e999d4dcc938/Essentials/src/com/earth2me/essentials/utils/EnumUtil.java
 */
public class Enums {

    private Enums() {
    }
    
    /**
     * Returns the field matching the first provided enum name that exists within the given
     * enum class. If no field is found, this method returns null.
     *
     * @param enumClass The class to search through
     * @param names The names of the fields to search for
     * @param <T> The enum to search through
     * @return The first matching enum field
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum> T valueOf(Class<T> enumClass, String... names) {
        for (String name : names) {
            try {
                Field enumField = enumClass.getDeclaredField(name);

                if (enumField.isEnumConstant()) {
                    return (T) enumField.get(null);
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        }

        return null;
    }

    /**
     * Return a set containing <b>all</b> fields of the given enum that maths one of the provided
     * names.
     *
     * @param enumClass The class to search through
     * @param names The names of the fields to search for
     * @param <T> The enum to search through
     * @return All matching enum fields
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum> Set<T> getAllMatching(Class<T> enumClass, String... names) {
        Set<T> set = new HashSet<>();

        for (String name : names) {
            try {
                Field enumField = enumClass.getDeclaredField(name);

                if (enumField.isEnumConstant()) {
                    set.add((T) enumField.get(null));
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        }

        return set;
    }

    /**
     * Gets the first Material field found from the given names.
     *
     * @param names The names of the fields to search for
     * @return The first matching Material
     */
    public static Material getMaterial(String... names) {
        return valueOf(Material.class, names);
    }

    /**
     * Gets the first Statistic field found from the given names.
     *
     * @param names The names of the fields to search for
     * @return The first matching Statistic
     */
    public static Statistic getStatistic(String... names) {
        return valueOf(Statistic.class, names);
    }

    public static EntityType getEntityType(String... names) {
        return valueOf(EntityType.class, names);
    }
}
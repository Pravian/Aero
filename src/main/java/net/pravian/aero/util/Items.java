package net.pravian.aero.util;

import org.bukkit.inventory.ItemStack;

/**
 * Represents all ItemStack-related utilities.
 */
public class Items
{

    private Items()
    {
    }

    public static boolean isSameType(ItemStack item, ItemStack compare)
    {
        return item.getType() == compare.getType();
    }

    @SuppressWarnings("deprecation")
    public static boolean isSameTypeAndDamage(ItemStack item, ItemStack compare)
    {
        return isSameType(item, compare) && item.getData().getData() == compare.getData().getData();
    }

    public static boolean isEqual(ItemStack item, ItemStack compare)
    {
        return isSameTypeAndDamage(item, compare) && item.getAmount() == compare.getAmount();
    }
}

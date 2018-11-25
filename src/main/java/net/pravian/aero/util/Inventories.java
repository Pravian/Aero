package net.pravian.aero.util;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Represents all Inventory-related utilities.
 */
public class Inventories
{

    private Inventories()
    {
    }

    /**
     * Gets the total item count of a given type and data
     * <p>
     * <p>
     * Author: bergerkiller</p>
     *
     * @param inventory to look in
     * @param type    of the items to look for, * for any item
     * @param data      of the items to look for, -1 for any data
     * @return Amount of items in the inventory
     */
    public static int getItemCount(Inventory inventory, String type, BlockData data)
    {
        if (type == "*")
        {
            int count = 0;
            for (ItemStack item : inventory.getContents())
            {
                if (item != null)
                {
                    count += item.getAmount();
                }
            }
            return count;
        }
        else
        {
            ItemStack rval = findItem(inventory, type, data);
            return rval == null ? 0 : rval.getAmount();
        }
    }

    /**
     * Obtains an item of the given type and data in the inventory specified<br>
     * If multiple items with the same type and data exist, their amounts are added together
     * <p>
     * <p>
     * Author: bergerkiller</p>
     *
     * @param inventory to look in
     * @param type    of the items to look for, * for any item
     * @param data      of the items to look for, * for any data
     * @return Amount of items in the inventory
     */
    @SuppressWarnings("deprecation")
    public static ItemStack findItem(Inventory inventory, String type, BlockData data)
    {
        ItemStack rval = null;
        BlockData itemData = data;
        String itemType = type;
        for (ItemStack item : inventory.getContents())
        {
            if (item == null)
            {
                continue;
            }
            // Compare type Id
            if (itemType == "*")
            {
                itemType = item.toString();
            }
            else if (itemType != item.toString())
            {
                continue;
            }
            // Compare data
            if (itemData == null)
            {
                itemData = item.getType().createBlockData();
            }
            else if (item.getData() != itemData)
            {
                continue;
            }
            // addition
            if (rval == null)
            {
                rval = item.clone();
            }
            else
            {
                addAmount(rval, item.getAmount());
            }
        }
        return rval;
    }

    /**
     * Adds a certain amount to an item, without limiting to the max stack size
     * <p>
     * <p>
     * Author: bergerkiller</p>
     *
     * @param item
     * @param amount to add
     */
    public static void addAmount(ItemStack item, int amount)
    {
        item.setAmount(Math.max(item.getAmount() + amount, 0));
    }

    /**
     * Validates if an inventory contains at certain amount of items from the specified material.
     *
     * @param inventory The Inventory to validate
     * @param material  The material to match to
     * @param minAmount The amount of items to validate
     * @return If the inventory contains that amount of items.
     */
    public static boolean containsAtLeast(Inventory inventory, Material material, int minAmount)
    {
        int amount = 0;

        for (ItemStack item : inventory.getContents())
        {
            if (item == null)
            {
                continue;
            }

            if (item.getType() == material)
            {
                amount += item.getAmount();
            }
        }

        return amount >= minAmount;
    }

    /**
     * Closes the inventory for all HumanEntity's viewing the inventory
     *
     * @param inventory The inventory to close
     */
    public static void closeInventory(Inventory inventory)
    {
        for (LivingEntity entity : inventory.getViewers())
        {
            if (entity instanceof HumanEntity)
            {
                ((HumanEntity) entity).closeInventory();
            }
        }
    }
}

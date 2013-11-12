package net.pravian.bukkitlib.util;

import net.pravian.java.implementation.SingletonBlock;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * Represents all World-related utilities.
 */
public class WorldUtils {

    /**
     * Executes the specified Block for every loaded world.
     * 
     * @param block The codeblock to use.
     * @see SingletonBlock
     */
    public static void eachWorld(SingletonBlock<World> block) {
        for (World world : Bukkit.getWorlds()) {
            block.run(world);
        }
    }

    /**
     * Sets the world time in ticks.
     * 
     * @param world The world to set the time on.
     * @param ticks The amount of ticks to set the time to.
     */
    public static void setWorldTime(World world, long ticks) {
        long time = world.getTime();
        time -= time % 24000;
        world.setTime(time + 24000 + ticks);
    }
}

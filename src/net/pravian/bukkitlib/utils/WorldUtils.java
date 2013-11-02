package net.pravian.bukkitlib.utils;

import net.pravian.util.SingletonBlock;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldUtils {

    public static void eachWorld(SingletonBlock<World> block) {
        for (World world : Bukkit.getWorlds()) {
            block.run(world);
        }
    }

    public static void setWorldTime(World world, long ticks) {
        long time = world.getTime();
        time -= time % 24000;
        world.setTime(time + 24000 + ticks);
    }
}

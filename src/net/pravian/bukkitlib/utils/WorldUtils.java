package net.pravian.bukkitlib.utils;

import net.pravian.util.SingletonBlock;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldUtils {

    public static void eachPlayer(SingletonBlock<Player> block) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            block.run(player);
        }
    }

    public static void setWorldTime(World world, long ticks) {
        long time = world.getTime();
        time -= time % 24000;
        world.setTime(time + 24000 + ticks);
    }
}

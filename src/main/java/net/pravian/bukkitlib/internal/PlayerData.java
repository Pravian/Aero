package net.pravian.bukkitlib.internal;

import java.util.HashMap;
import java.util.Map;
import net.pravian.bukkitlib.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerData {

    private static final Map<String, Long> PLAYER_JOIN_UNIX;

    static {
        PLAYER_JOIN_UNIX = new HashMap<String, Long>();
    }

    private PlayerData() {
    }

    public static void startListening(Plugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
            public void onPlayerJoin(PlayerJoinEvent event) {
                PlayerData.doPlayerJoin(event);
            }

            @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
            public void onPlayerQuit(PlayerQuitEvent event) {
                PlayerData.doPlayerQuit(event);
            }
        }, plugin);
    }

    protected static void doPlayerJoin(PlayerJoinEvent event) {
        final String key = getPlayerKey(event.getPlayer());

        PLAYER_JOIN_UNIX.put(key, TimeUtils.getUnix());
    }

    protected static void doPlayerQuit(PlayerQuitEvent event) {
        final String key = getPlayerKey(event.getPlayer());

        if (PLAYER_JOIN_UNIX.containsKey(key)) {
            PLAYER_JOIN_UNIX.remove(key);
        }
    }

    public static Long getPlayerUnixJoin(Player player) {
        return PLAYER_JOIN_UNIX.get(getPlayerKey(player));
    }

    private static String getPlayerKey(Player player) {
        return player.getName().toLowerCase().trim();
    }
}

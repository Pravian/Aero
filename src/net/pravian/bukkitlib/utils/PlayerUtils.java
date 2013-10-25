package net.pravian.bukkitlib.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.pravian.util.SingletonBlock;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public static void eachPlayer(SingletonBlock<Player> block) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            block.run(player);
        }
    }

    public static Player getPlayer(String name) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase().startsWith(name.toLowerCase())) {
                return player;
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (ChatColor.stripColor(player.getDisplayName()).equalsIgnoreCase(name)) {
                return player;
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (ChatColor.stripColor(player.getDisplayName()).toLowerCase().startsWith(name.toLowerCase())) {
                return player;
            }
        }

        return null;
    }

    public static OfflinePlayer getOfflinePlayer(String name) {
        return getOfflinePlayer(name, true);
    }

    public static OfflinePlayer getOfflinePlayer(String name, boolean onlineCheck) {
        if (onlineCheck) {
            OfflinePlayer player = getPlayer(name);

            if (player != null) {
                return player;
            }
        }

        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName().equalsIgnoreCase(name)) {
                return offlinePlayer;
            }
        }

        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName().toLowerCase().startsWith(name.toLowerCase())) {
                return offlinePlayer;
            }
        }

        return null;
    }

    public static String concatPlayerNames(Set<OfflinePlayer> players) {
        List<String> names = new ArrayList<String>();
        for (OfflinePlayer player : players) {
            names.add(player.getName());
        }
        return StringUtils.join(names, ", ");
    }
}

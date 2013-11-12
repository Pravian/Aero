package net.pravian.bukkitlib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.pravian.java.implementation.SingletonBlock;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Represents all Player-related utilities.
 */
public class PlayerUtils {

    /**
     * Executes the specified Block for every online player.
     * 
     * @param block The codeblock to use.
     * @see SingletonBlock
     */
    public static void eachPlayer(SingletonBlock<Player> block) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            block.run(player);
        }
    }
    
    /**
     * Attempts to resolve the full Player-Name by a portion of it.
     * 
     * <b>Note</b>: Uses {@link #getOfflinePlayer(java.lang.String)} to obtain the player-name.
     * 
     * @param name The partial name to search.
     * @return The player name, if the player could not be found, null.
     * @see #getOfflinePlayer(String) 
     */
    public String getPlayerName(String name) {
        final OfflinePlayer player = getOfflinePlayer(name);
        return (player == null ? null : player.getName());
    }

    /**
     * Attempts to resolve a player by a partial name.
     * 
     * <p>The following order is used to match player names:
     * <ul>
     * <li>The exact player name, case insensitive.</li>
     * <li>The start of the player name, case insensitive.</li>
     * <li>The contents of the player name, case insensitive.</li>
     * <li>The start of the player's custom name, case insensitive.</li>
     * <li>The contents of the player's custom name, case insensitive.</li>
     * </ul>
     * </p>
     * 
     * @param name The partial name to match.
     * @return The player. If the player could not be found, null.
     */
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
            if (player.getName().toLowerCase().contains(name.toLowerCase())) {
                return player;
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (ChatColor.stripColor(player.getDisplayName()).equalsIgnoreCase(name)) {
                return player;
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (ChatColor.stripColor(player.getDisplayName()).toLowerCase().contains(name.toLowerCase())) {
                return player;
            }
        }

        return null;
    }

    /**
     * Attempts to resolve a player (online or offline) by a partial name.
     * 
     * <p>Uses {@link #getOfflinePlayer(String, boolean)} with the second parameter as true.</p>
     * 
     * @see #getOfflinePlayer(String)
     */
    public static OfflinePlayer getOfflinePlayer(String name) {
        return getOfflinePlayer(name, true);
    }

    /**
     * Attempts to resolve an (Offline) player by a partial name.
     * 
     * <p>The following order is used to match player names:
     * <ul>
     * <li>The exact player name, case insensitive.</li>
     * </p>
     * 
     * @param name The partial name to match.
     * @param onlineCheck If true, the online players will be matched first.
     * @return The player. If the player could not be found, null.
     */
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
        
        return null;
    }

    /**
     * Returns a String of concatenated player names.
     * 
     * <p>Example:<pre>
     * concatPlayerNames(Arrays.asString(new String[]{"Notch", "DarthSalamon", "EvilPerson"}); // Notch, DarthSalamon, EvilPerson
     * </pre>
     * 
     * @param players The players to concatenate.
     * @return The resulting String.
     */
    public static String concatPlayerNames(Collection<OfflinePlayer> players) {
        List<String> names = new ArrayList<String>();
        for (OfflinePlayer player : players) {
            names.add(player.getName());
        }
        return concatPlayernames(names);
    }
    
    public static String concatPlayernames(Collection<String> names) {
       return StringUtils.join(names, ", "); 
    }
}

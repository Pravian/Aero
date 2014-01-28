package net.pravian.bukkitlib.util;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import net.minecraft.server.v1_7_R1.BanEntry;
import net.minecraft.server.v1_7_R1.BanList;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import net.minecraft.server.v1_7_R1.PropertyManager;

/**
 * Represents all Server-related utilities.
 */
public class ServerUtils {

    /**
     * Sets a configuration option in server.properties.
     *
     * <p>Example:
     * <pre>
     * setConfigOption("enable-command-block", true);
     * </pre></p>
     *
     * @param option
     * @param value
     */
    public static void setConfigOption(String option, Object value) {
        final PropertyManager propertyManager = MinecraftServer.getServer().getPropertyManager();
        propertyManager.a(option, value);
        propertyManager.savePropertiesFile();
    }

    /**
     * Bans a player from the server using banned-players.txt
     *
     * <p>This method is helpful because Bukkit by default does not allow banning with options (Expiry date, source, etc.). Any of these options may be omitted by passing in null to the
     * corresponding parameter.</p>
     *
     * @param name The username to ban.
     * @param reason The reason for the ban.
     * @param source The source for the ban.
     * @param expireDate The expiry date for the ban.
     */
    public static void banUsername(String name, String reason, String source, Date expireDate) {
        name = name.toLowerCase().trim();

        BanEntry entry = new BanEntry(name);
        if (expireDate != null) {
            entry.setExpires(expireDate);
        }

        if (reason != null) {
            entry.setReason(reason);
        }

        if (source != null) {
            entry.setSource(source);
        }

        final BanList nameBans = MinecraftServer.getServer().getPlayerList().getNameBans();
        nameBans.add(entry);
    }

    /**
     * Unbans a username from banned-players.txt.
     *
     * @param name The username to unban.
     * @see #banUsername(String, String, String, Date)
     */
    public static void unbanUsername(String name) {
        name = name.toLowerCase().trim();
        final BanList nameBans = MinecraftServer.getServer().getPlayerList().getNameBans();
        nameBans.remove(name);
    }

    /**
     * Purges all bans from banned-players.txt.
     */
    public static void purgeNameBans() {
        final BanList nameBans = MinecraftServer.getServer().getPlayerList().getNameBans();
        nameBans.getEntries().clear();
        nameBans.save();
    }

    /**
     * Validates if a player has been banned by name in banned-players.txt.
     *
     * @param name The player-name to validate.
     * @return true if the player is banned.
     */
    public static boolean isNameBanned(String name) {
        name = name.toLowerCase().trim();
        final BanList nameBans = MinecraftServer.getServer().getPlayerList().getNameBans();
        nameBans.removeExpired();
        return nameBans.getEntries().containsKey(name);
    }

    /**
     * Bans an IP from the server using banned-ips.txt
     *
     * <p>This method is helpful because Bukkit by default does not allow banning with options (Expiry date, source, etc.). Any of these options may be omitted by passing in null to the
     * corresponding parameter.</p>
     *
     * @param ip The IP to ban.
     * @param reason The reason for the ban.
     * @param source The source for the ban.
     * @param expiryDate The expiry date for the ban.
     */
    public static void banIp(String ip, String reason, String source, Date expiryDate) {
        ip = ip.toLowerCase().trim();
        final BanEntry entry = new BanEntry(ip);
        if (expiryDate != null) {
            entry.setExpires(expiryDate);
        }
        if (reason != null) {
            entry.setReason(reason);
        }
        if (source != null) {
            entry.setSource(source);
        }
        BanList ipBans = MinecraftServer.getServer().getPlayerList().getIPBans();
        ipBans.add(entry);
    }

    /**
     * Unbans an IP from banned-ips.txt.
     *
     * @param ip The IP to unban.
     * @see #banIp(String, String, String, Date)
     */
    public static void unbanIp(String ip) {
        ip = ip.toLowerCase().trim();
        final BanList ipBans = MinecraftServer.getServer().getPlayerList().getIPBans();
        ipBans.remove(ip);
    }

    /**
     * Purges all Ip-bans from banned-ips.txt.
     */
    public static void purgeIpBans() {
        final BanList IPBans = MinecraftServer.getServer().getPlayerList().getIPBans();
        IPBans.getEntries().clear();
        IPBans.save();
    }

    /**
     * Validates if an IP has been banned by name in banned-ips.txt.
     *
     * @param ip The IP to validate.
     * @return true if the IP is banned.
     */
    public static boolean isIpBanned(String ip) {
        ip = ip.toLowerCase().trim();
        final BanList ipBans = MinecraftServer.getServer().getPlayerList().getIPBans();
        ipBans.removeExpired();
        return ipBans.getEntries().containsKey(ip);
    }

    /**
     * Purges all whitelisted people from whitelist.txt.
     *
     * @return The amount of players removed from the whitelist whitelisted.
     */
    public static int purgeWhitelist() {
        final Set whitelist = MinecraftServer.getServer().getPlayerList().getWhitelisted();
        final int size = whitelist.size();
        whitelist.clear();
        return size;
    }

    /**
     * Validates if a player is whitelisted.
     *
     * @param name The player-name to validate.
     * @return true if the player is whitelisted.
     */
    public static boolean isWhiteListed(String name) {
        if (MinecraftServer.getServer().getPlayerList().getWhitelisted().contains(name.trim().toLowerCase())) {
            return true;
        }
        return false;
    }

    /**
     * Adds a collection of player-names to the whitelist.
     *
     * @param names The players to add to the whitelist.
     */
    public static void addToWhitelist(Collection<String> names) {
        for (String name : names) {
            addToWhitelist(name);
        }
    }

    /**
     * Adds a player to the whitelist by name.
     *
     * @param name The player-name to whitelist.
     */
    public static void addToWhitelist(String name) {
        MinecraftServer.getServer().getPlayerList().addWhitelist(name);
    }

    /**
     * Removes a collection of player-names from the whitelist.
     *
     * @param names The players to remove from the whitelist.
     */
    public static void removeFromWhitelist(Collection<String> names) {
        for (String name : names) {
            removeFromWhitelist(name);
        }
    }

    /**
     * Removes a player from the whitelist by name.
     *
     * @param name The player-name to remove from the whitelist.
     */
    public static void removeFromWhitelist(String name) {
        MinecraftServer.getServer().getPlayerList().removeWhitelist(name);
    }

    /**
     * Adds a collection of player-names to the operator-list.
     *
     * @param names The player-names to add to operator.
     */
    public static void addToOperators(Collection<String> names) {
        for (String name : names) {
            addToOperators(name);
        }
    }

    /**
     * Adds a player to the operator-list by name.
     *
     * @param name The player-name to add to operator.
     */
    public static void addToOperators(String name) {
        MinecraftServer.getServer().getPlayerList().addOp(name);
    }

    /**
     * Removes a collection of player-names from the operator-list.
     *
     * @param names The player-names to remove from operator.
     */
    public static void removeFromOperators(Collection<String> names) {
        for (String name : names) {
            removeFromOperators(name);
        }
    }

    /**
     * Removes a player from the operator-list by name.
     *
     * @param name The player-name remove from operator.
     */
    public static void removeFromOperators(String name) {
        MinecraftServer.getServer().getPlayerList().removeOp(name);
    }

    /**
     * Validates if the whitelist is enabled.
     *
     * @return true if the whitelist is enabled.
     */
    public static boolean whitelistEnabled() {
        return MinecraftServer.getServer().getPlayerList().getHasWhitelist();
    }

    /**
     * Enables/disables the whitelist.
     *
     * @param enabled true if the whitelist should be enabled.
     */
    public static void setWhitelistEnabled(boolean enabled) {
        MinecraftServer.getServer().getPlayerList().setHasWhitelist(enabled);
    }

    /**
     * Returns the version of the software running the server.
     *
     * @return The software version.
     */
    public static String getServerVersion() {
        return MinecraftServer.getServer().getVersion();
    }

    /**
     * Returns the MineCraft server instance running the server.
     *
     * @return The MinecraftServer instance.
     * @deprecated Added methods to reflect commonly needed functions.
     */
    @Deprecated()
    public static MinecraftServer getMineCraftServer() {
        return MinecraftServer.getServer();
    }
}

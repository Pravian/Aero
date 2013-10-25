package net.pravian.bukkitlib.utils;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import net.minecraft.server.v1_6_R2.BanEntry;
import net.minecraft.server.v1_6_R2.BanList;
import net.minecraft.server.v1_6_R2.MinecraftServer;
import net.minecraft.server.v1_6_R2.PropertyManager;

public class ServerUtils {

    public static void setConfigOption(String option, Object value) {
        final PropertyManager propertyManager = MinecraftServer.getServer().getPropertyManager();
        propertyManager.a(option, value);
        propertyManager.savePropertiesFile();
    }

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

    public static void unbanUsername(String name) {
        name = name.toLowerCase().trim();
        final BanList nameBans = MinecraftServer.getServer().getPlayerList().getNameBans();
        nameBans.remove(name);
    }

    public static void purgeNameBans() {
        final BanList nameBans = MinecraftServer.getServer().getPlayerList().getNameBans();
        nameBans.getEntries().clear();
        nameBans.save();
    }

    public static void purgeIpBans() {
        final BanList IPBans = MinecraftServer.getServer().getPlayerList().getIPBans();
        IPBans.getEntries().clear();
        IPBans.save();
    }

    public static boolean isNameBanned(String name) {
        name = name.toLowerCase().trim();
        final BanList nameBans = MinecraftServer.getServer().getPlayerList().getNameBans();
        nameBans.removeExpired();
        return nameBans.getEntries().containsKey(name);
    }

    public static void banIp(String ip, String reason, String source, Date expireDate) {
        ip = ip.toLowerCase().trim();
        final BanEntry entry = new BanEntry(ip);
        if (expireDate != null) {
            entry.setExpires(expireDate);
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

    public static void unbanIp(String ip) {
        ip = ip.toLowerCase().trim();
        final BanList ipBans = MinecraftServer.getServer().getPlayerList().getIPBans();
        ipBans.remove(ip);
    }

    public static boolean isIpBanned(String ip) {
        ip = ip.toLowerCase().trim();
        final BanList ipBans = MinecraftServer.getServer().getPlayerList().getIPBans();
        ipBans.removeExpired();
        return ipBans.getEntries().containsKey(ip);
    }

    public static int purgeWhitelist() {
        final Set whitelist = MinecraftServer.getServer().getPlayerList().getWhitelisted();
        final int size = whitelist.size();
        whitelist.clear();
        return size;
    }

    public static boolean isWhiteListed(String name) {
        if (MinecraftServer.getServer().getPlayerList().getWhitelisted().contains(name.trim().toLowerCase())) {
            return true;
        }
        return false;
    }

    public static void addToWhitelist(Collection<String> names) {
        for (String name : names) {
            addToWhitelist(name);
        }
    }

    public static void addToWhitelist(String name) {
        MinecraftServer.getServer().getPlayerList().addWhitelist(name);
    }

    public static void removeFromWhitelist(Collection<String> names) {
        for (String name : names) {
            removeFromWhitelist(name);
        }
    }

    public static void removeFromWhitelist(String name) {
        MinecraftServer.getServer().getPlayerList().removeWhitelist(name);
    }

    public static void addToOperators(Collection<String> names) {
        for (String name : names) {
            addToOperators(name);
        }
    }

    public static void addToOperators(String name) {
        MinecraftServer.getServer().getPlayerList().addOp(name);
    }

    public static void removeFromOperators(Collection<String> names) {
        for (String name : names) {
            removeFromOperators(name);
        }
    }

    public static void removeFromOperators(String name) {
        MinecraftServer.getServer().getPlayerList().removeOp(name);
    }
    
    public static boolean whitelistEnabled() {
        return MinecraftServer.getServer().getPlayerList().getHasWhitelist();
    }
    
    public static void setWhitelistEnabled(boolean enabled) {
        MinecraftServer.getServer().getPlayerList().setHasWhitelist(enabled);
    }

    public static String getServerVersion() {
        return MinecraftServer.getServer().getVersion();
    }
    
    @Deprecated
    public static MinecraftServer getMineCraftServer() {
        return MinecraftServer.getServer();
    }
}

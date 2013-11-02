package net.pravian.bukkitlib.utils;

import java.util.regex.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

public class IpUtils {

    private static final String IP_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static boolean fuzzyIpMatch(String a, String b, int octets) {
        boolean match = true;

        String[] aParts = a.split("\\.");
        String[] bParts = b.split("\\.");

        if (aParts.length != 4 || bParts.length != 4) {
            return false;
        }

        if (octets > 4) {
            octets = 4;
        } else if (octets < 1) {
            octets = 1;
        }

        for (int i = 0; i < octets && i < 4; i++) {
            if (aParts[i].equals("*") || bParts[i].equals("*")) {
                continue;
            }

            if (!aParts[i].equals(bParts[i])) {
                match = false;
                break;
            }
        }

        return match;
    }
    
    public static String getIp(Player player) {
        return player.getAddress().getAddress().getHostAddress().trim();
    }

    public static String getIp(PlayerLoginEvent event) {
        return event.getAddress().getHostAddress().trim();
    }
    
    public static int getPort(Player player) {
        return player.getAddress().getPort();
    }

    public static String toEscapedString(String ip) {
        return ip.trim().replaceAll("\\.", "_");
    }

    public static String fromEscapedString(String escapedIp) {
        return escapedIp.trim().replaceAll("_", "\\.");
    }

    public static boolean isValidIp(String ip) {
        return Pattern.compile(IP_PATTERN).matcher(ip.trim()).matches();
    }
}

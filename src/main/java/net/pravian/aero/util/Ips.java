package net.pravian.aero.util;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.regex.Pattern;

/**
 * Represents all IP-related utilities.
 */
public class Ips
{

    private static final String IP_PATTERN
            = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private Ips()
    {
    }

    /**
     * Matches two "fuzzy IPs" to each other with the specified amount of octets.
     * <p>
     * <p>
     * Fuzzy IPs are IPv4-addresses which may contain wildcard values. Example: <i>192.168.*.*</i>. This method matches two (or one) fuzzy IP(s) to each other and return a boolean based on the
     * result.</p>
     * <p>
     * <p>
     * A simple example: the example-IP, <i>192.168.*.*</i>, contains 2 fuzzy octets. Both 192.168.2.1 and 192.168.123.231 match this fuzzy IP but 123.12.2.2 does not.</p>
     * <p>
     * <p>
     * Other examples:
     * <pre>
     * Ips.fuzzyIpMatch("19.19.*.*", "19.19.1.1", 4); // true
     * Ips.fuzzyIpMatch("*.*.*.*", "122.124.52.44", 4); // true
     * Ips.fuzzyIpMatch("192.*.33.22", "192.44.33.*", 4); // true
     * Ips.fuzzyIpMatch("31.23.88.44", 31.23.12.8", 2); // true
     * Ips.fuzzyIpMatch("80.34.10.2", "80.*.12.3", 2; // true
     * Ips.fuzzyIpMatch("12.15.23.5", "12.42.3.2", 2); // <b>false</b>
     * </pre></p>
     *
     * @param ipA    The first IP to matched.
     * @param ipB    The second IP to matched.
     * @param octets The amount of octets which must be equal.
     * @return true if the IP matches the fuzzy IP.
     */
    public static boolean fuzzyIpMatch(String ipA, String ipB, int octets)
    {
        boolean match = true;

        String[] ippartsA = ipA.split("\\.");
        String[] ipPartsB = ipB.split("\\.");

        if (ippartsA.length != 4 || ipPartsB.length != 4)
        {
            return false;
        }

        if (octets > 4)
        {
            octets = 4;
        }
        else if (octets < 1)
        {
            octets = 1;
        }

        for (int i = 0; i < octets && i < 4; i++)
        {
            if (ippartsA[i].equals("*") || ipPartsB[i].equals("*"))
            {
                continue;
            }

            if (!ippartsA[i].equals(ipPartsB[i]))
            {
                match = false;
                break;
            }
        }

        return match;
    }

    /**
     * Returns the Ipv4 address of the specified player in String form.
     *
     * @param player The player from which to obtain the IP from.
     * @return The IP-address.
     */
    public static String getIp(Player player)
    {
        return player.getAddress().getAddress().getHostAddress().trim();
    }

    /**
     * Returns the Ipv4 address of the specified PlayerLoginEvent.
     *
     * @param event The player to obtain the IP from.
     * @return The IP-address.
     * @see PlayerLoginEvent
     */
    public static String getIp(PlayerLoginEvent event)
    {
        return event.getAddress().getHostAddress().trim();
    }

    /**
     * Returns the public port the player used to connect to this server.
     *
     * @param player The player from which to obtain the port from.
     * @return The port.
     */
    public static int getPort(Player player)
    {
        return player.getAddress().getPort();
    }

    /**
     * Escapes an IP-address to a config-friendly version.
     * <p>
     * <p>
     * Example:
     * <pre>
     * Ips.toEscapedString("192.168.1.192"); // 192_168_1_192
     * </pre></p>
     *
     * @param ip The IP-address to escape.
     * @return The config-friendly IP address.
     * @see #fromEscapedString(String)
     */
    public static String toEscapedString(String ip)
    {
        return ip.trim().replaceAll("\\.", "_");
    }

    /**
     * Un-escapes a config-friendly Ipv4-address.
     * <p>
     * <p>
     * Example:
     * <pre>
     * Ips.fromEscapedString("192_168_1_192"); // 192.168.1.192
     * </pre></p>
     *
     * @param escapedIp The IP-address to un-escape.
     * @return The config-friendly IP address.
     * @see #toEscapedString(String)
     */
    public static String fromEscapedString(String escapedIp)
    {
        return escapedIp.trim().replaceAll("_", "\\.");
    }

    /**
     * Validates if an String is a valid Ipv4 address.
     * <p>
     * <p>
     * <b>Warning</b>: This validation is very strict, validating only proper IP-addresses.</p>.
     * <p>
     * <p>
     * Examples:
     * <pre>
     * Ips.isValidIp("192.0.0.256"); // false
     * Ips.isValidIp("127.0.0.0.1"); // false
     * Ips.isValidIp("-12.3.4.3"); // false
     * Ips.isValidIp("a.b.c.d"); // false
     * Ips.isValidIp("192.1.1.1"); // true
     * </pre></p>
     *
     * @param ip The IP-address to validate.
     * @return true if the IP-Address is valid.
     */
    public static boolean isValidIp(String ip)
    {
        return Pattern.compile(IP_PATTERN).matcher(ip.trim()).matches();
    }
}

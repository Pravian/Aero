package net.pravian.bukkitlib.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;

/**
 * Represents all Chat-related utilities.
 */
public class ChatUtils {

    private static final Random RANDOM = new Random();
    
    /**
     * A static list of all ChatColors.
     * 
     * <p><b>Warning</b>: This does not include special ChatColors
     * ChatColor.RESET, ChatColor.BOLD, ChatColor.UNDERLINE, 
     * ChatColor.STRIKETHROUGH, ChatColor.ITALIC and ChatColor.WHITE</p>
     * 
     * @see ChatColor
     */
    public static final List<ChatColor> CHAT_COLOR_POOL = Arrays.asList(
            ChatColor.DARK_BLUE,
            ChatColor.DARK_GREEN,
            ChatColor.DARK_AQUA,
            ChatColor.DARK_RED,
            ChatColor.DARK_PURPLE,
            ChatColor.DARK_GRAY,
            ChatColor.GOLD,
            ChatColor.BLACK,
            ChatColor.BLUE,
            ChatColor.GREEN,
            ChatColor.AQUA,
            ChatColor.RED,
            ChatColor.LIGHT_PURPLE,
            ChatColor.YELLOW);

    /**
     * Translates a String with color codes (&2, &4, etc) to a colored string.
     * 
     * @param string The String which should be translated.
     * @return The Colored string.
     */
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Returns a random chat color from the ChatColor pool.
     * 
     * @return The randomly selected ChatColor.
     * @see #CHAT_COLOR_POOL
     */
    public static ChatColor randomChatColor() {
        return CHAT_COLOR_POOL.get(RANDOM.nextInt(CHAT_COLOR_POOL.size()));
    }
}

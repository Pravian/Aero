package net.pravian.bukkitlib.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Represents a collection of players.
 */
public class PlayerLobby extends ArrayList<Player> {

    private final boolean dynamic;
    //
    private String messageFormat = "_";

    /**
     * Creates a new PlayerLobby instance.
     *
     * <p>This is a shorthand for PlayerLobby(false)</p>
     *
     * @see #PlayerLobby(boolean)
     */
    public PlayerLobby() {
        this(false);
    }

    /**
     * Creates a new PlayerLobby instance.
     *
     * <p>Passing true as the second argument will result in a dynamic lobby. A dynamic lobby has all the features a regular player lobby has with exception that the players in the lobby is
     * always equal to the online players. Attempts to add or remove players will fail in that case.</p>
     *
     * @param dynamic Whenever to make the lobby dynamic.
     */
    public PlayerLobby(boolean dynamic) {
        this.dynamic = dynamic;
    }

    /**
     * Returns the players in the lobby.
     *
     * @return The players.
     */
    public List<Player> getPlayers() {
        if (dynamic) {
            return Arrays.asList(Bukkit.getServer().getOnlinePlayers());
        } else {
            return this;
        }
    }

    private void removeExpired() {
        if (dynamic) {
            return;
        }

        final List<Player> removes = new ArrayList<Player>();
        for (Player player : getPlayers()) {
            if (!player.isOnline()) {
                removes.add(player);
                continue;
            }
        }

        for (Player player : removes) {
            super.remove(player);
        }
    }

    @Override
    public int size() {
        removeExpired();
        return super.size();
    }

    @Override
    public boolean add(Player player) {
        removeExpired();
        if (getPlayers().contains(player) || dynamic) {
            return false;
        }

        return super.add(player);
    }

    @Deprecated
    @Override
    public boolean remove(Object object) {
        try {
            return remove((Player) object);
        } catch (Exception ex) {
        }
        return false;
    }

    /**
     * Removes the first occurrence of the specified element from this list, if it is present.
     *
     * <p>If the list does not contain the element, it is unchanged. More formally, removes the element with the lowest index i such that (o==null ? get(i)==null : o.equals(get(i))) (if such
     * an element exists). Returns true if this list contained the specified element (or equivalently, if this list changed as a result of the call).
     *
     * @param player - element to be removed from this list, if present
     * @return true if this list contained the specified element
     */
    public boolean remove(Player player) {
        removeExpired();
        if (!getPlayers().contains(player) || dynamic) {
            return false;
        }

        return super.remove(player);
    }

    /**
     * Validates if a player with a certain player-name is in the lobby.
     *
     * <p><b>Note</b>This validation is case-insensitive.</p>
     *
     * @param playerName The player-name to match.
     * @return True if the player exists in the lobby.
     */
    public boolean contains(String playerName) {
        removeExpired();
        for (Player player : getPlayers()) {
            if (player.getName().equalsIgnoreCase(playerName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Validates if the lobby is dynamic.
     *
     * @return True if the lobby is dynamic.
     * @see #PlayerLobby(boolean)
     */
    public boolean isDynamic() {
        return dynamic;
    }

    /**
     * Returns the raw message format.
     *
     * <p>The message format is a single string representing how messages from {@link #message(java.lang.String) message(String)} should be formatted. When message() gets called, all
     * occurrences of the underscore character will be replaced by the message. The default message format is <b>_</b>.
     * </p>
     *
     * @return The message format.
     */
    public String getMessageFormat() {
        return messageFormat;
    }

    /**
     * Sets the raw message format.
     *
     * <p><b>Note</b>: See getMessageFormat() for more information.</p>
     *
     * @param messageFormat
     * @see #getMessageFormat()
     */
    public void setMessageFormat(String messageFormat) {
        this.messageFormat = messageFormat;
    }

    /**
     * Messages all players in the lobby.
     *
     * <p><b>Note</b>: If a custom message format has been set, that will be used to display the message.</p>
     *
     * @param message The message to display.
     * @see #getMessageFormat()
     */
    public void message(String message) {
        for (Player player : getPlayers()) {
            player.sendMessage(messageFormat.replace("_", message));
        }
    }

    /**
     * Sets the prefix to the message format.
     *
     * <p><b>Note</b>: See getMessageFormat() for more information.</p>
     *
     * @param prefix The prefix to set to.
     * @see #getMessageFormat()
     */
    public void setPrefix(String prefix) {
        final String[] parts = messageFormat.split("_");

        messageFormat = prefix + StringUtils.join(parts, "_", 1, messageFormat.split("_").length);
    }

    /**
     * Sets the suffix to the message format.
     *
     * <p><b>Note</b>: See getMessageFormat() for more information.</p>
     *
     * @param suffix The suffix to set.
     * @see #getMessageFormat()
     */
    public void setSuffix(String suffix) {
        final String[] parts = messageFormat.split("_");

        if (parts.length == 0) {
            messageFormat += suffix;
            return;
        }

        messageFormat = StringUtils.join(parts, "_", 0, parts.length - 1) + suffix;
    }

    /**
     * Kicks all players in the lobby.
     *
     * @param message The message to kick with.
     */
    public void kickAll(String message) {
        removeExpired();
        for (Player player : getPlayers()) {
            player.kickPlayer(message);
        }
    }

    /**
     * Returns a list of players in the lobby who have a certain permission.
     *
     * @param permission The permission to validate.
     * @return The players who have the permission.
     */
    public List<Player> getPermitted(String permission) {
        removeExpired();

        final List<Player> permitted = new ArrayList<Player>();

        for (Player player : getPlayers()) {
            if (player.hasPermission(permission)) {
                permitted.add(player);
            }
        }
        return permitted;
    }
}

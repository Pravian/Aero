package net.pravian.bukkitlib.implementation;

import java.util.Arrays;
import java.util.List;
import net.pravian.bukkitlib.util.ChatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

/**
 * Represents a collection of sign-related activities
 */
public class BukkitSign {

    final Sign sign;

    /**
     * Creates a new BukkitSign instance from a location.
     *
     * <p>Note: If a sign does not exist at the specified location, this instance will be invalid. Validity can be checked using isSign().</p>
     *
     * @param location The location the sign is located at.
     */
    public BukkitSign(Location location) {
        this(location.getBlock());
    }

    /**
     * Creates a new BukkitSign instance from a Block.
     *
     * <p>Note: If a sign does not exist at the specified block, this instance will be invalid. Validity can be checked using isSign().</p>
     *
     * @param block The block the sign is located at.
     */
    public BukkitSign(Block block) {
        this(block.getState());
    }

    /**
     * Creates a new BukkitSign instance from a BlockState.
     *
     * <p>Note: If the BlockState does not belong to a sign, this instance will be invalid. Validity can be checked using isSign()</p>
     *
     * @param state The BlockState of the sign.
     */
    public BukkitSign(BlockState state) {
        this(state, false);
    }

    /**
     * Creates a new BukkitSign instance from a BlockState.
     *
     * <p>Note: If create is true, a sign will be created at the specified location, if there isn't one already. If no sign is present and create is false, this instance will be invalid. Validity can
     * be checked using isSign()</p>
     *
     * @param state The BlockState of the sign.
     * @param create If the sign should be created if it isn't present.
     */
    public BukkitSign(BlockState state, boolean create) {
        if (state instanceof Sign) {
            sign = (Sign) state;
        } else {
            if (create) {
                state.getBlock().setType(Material.SIGN_POST);
                sign = (Sign) state;
            } else {
                sign = null;
            }
        }
    }

    /**
     * Returns the BlockState (Sign) of this sign.
     *
     * @return The BlockState
     */
    public Sign getSign() {
        return sign;
    }

    /**
     * Returns the Block the sign is located at.
     *
     * @return The Block.
     */
    public Block getBlock() {
        return sign.getBlock();
    }

    /**
     * Validates if a this BukkitSign was created correctly.
     *
     * @return True if the BukkitSign is valid.
     * @see #BukkitSign(org.bukkit.block.BlockState, boolean)
     */
    public boolean isSign() {
        return sign != null;
    }

    /**
     * Returns a list of the text on the sign.
     *
     * @return The list.
     */
    public List<String> getLines() {
        return Arrays.asList(sign.getLines());
    }

    public String getLine(int line) {
        if (line < 0 || line > 3) {
            return null;
        }

        return sign.getLine(line);
    }

    /**
     * Sets the text on the sign.
     *
     * @param lines The lines to set.
     */
    public void setLines(List<String> lines) {
        setLines(lines, false);
    }

    /**
     * Sets the text on the sign.
     *
     * @param lines The lines to set
     * @param colorize If the text should be colored using ChatUtils.colorize()
     * @see ChatUtils#colorize(java.lang.String)
     */
    public void setLines(List<String> lines, boolean colorize) {
        for (String line : lines) {
            if (lines.indexOf(line) > 3) {
                break;
            }

            sign.setLine(lines.indexOf(line), colorize ? ChatUtils.colorize(line) : line);
        }
        sign.update();
    }

    /**
     * Sets the line on a specified linenumber to the specified line.
     *
     * @param lineNumber The line-number to set.
     * @param line The line to set the line-number to.
     */
    public void setLine(int lineNumber, String line) {
        if (lineNumber > 3) {
            return;
        }
        sign.setLine(lineNumber, line);
        sign.update();
    }

    /**
     * Clears all the lines on the sign.
     */
    public void clearLines() {
        sign.setLine(0, "");
        sign.setLine(1, "");
        sign.setLine(2, "");
        sign.setLine(3, "");
        sign.update();
    }

    /**
     * Gets the type of sign (wall-sign or sign-post).
     *
     * @return The type of sign.
     */
    public SignType getType() {
        return (((org.bukkit.material.Sign) sign.getData()).isWallSign() ? SignType.WALL_SIGN : SignType.SIGN_POST);
    }

    /**
     * Returns the direction the sign is facing
     *
     * @return the direction
     */
    public BlockFace getDirection() {
        return ((org.bukkit.material.Sign) sign.getData()).getFacing();
    }

    /**
     * Sets the direction the sign is facing.
     *
     * @param face The direction to face.
     */
    public void setDirection(BlockFace face) {
        final org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
        signData.setFacingDirection(face);
        sign.update();
    }

    /**
     * Returns the BlockFace the sign is attached to.
     *
     * @return The BlockFace.
     */
    public BlockFace getAttachedFace() {
        return ((org.bukkit.material.Sign) sign.getData()).getAttachedFace();
    }

    /**
     * Validates if all the lines on a sign is empty.
     *
     * @return True if a sign is empty.
     */
    public boolean isEmpty() {
        return getLine(0).equals("") && getLine(1).equals("") && getLine(2).equals("") && getLine(3).equals("");
    }

    /**
     * Represents the different types of signs.
     */
    public static enum SignType {

        WALL_SIGN,
        SIGN_POST;
    }
}

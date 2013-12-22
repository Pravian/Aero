package net.pravian.bukkitlib.command;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the class which stores information about per-command permissions.
 */
public abstract class BukkitPermissionHolder {

    private final Map<Class<? extends BukkitCommand>, String> PERMISSIONS;

    /**
     * Creates a new BukkitPermissionHolder instance
     */
    protected BukkitPermissionHolder() {
        this.PERMISSIONS = new HashMap<Class<? extends BukkitCommand>, String>();
    }

    /**
     * Returns the permission required to execute a command or null if the command has no permission attached.
     *
     * @param command The command to which the permission is required
     * @return The permission / null
     */
    protected String getPermission(Class<? extends BukkitCommand> command) {
        return PERMISSIONS.get(command);
    }

    /**
     * Sets a permission required for a command.
     *
     * @param command The command for which the permission must be set
     * @param permission The permission required to execute the command
     */
    protected void setPermission(Class<? extends BukkitCommand> command, String permission) {
        PERMISSIONS.put(command, permission);
    }

    /**
     * Should initialize all permissions.
     *
     * <p><b>In normal conditions, this should never be ran.</b></p>
     *
     */
    protected abstract void initPermissions();
}

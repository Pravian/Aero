package net.pravian.bukkitlib.command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a class which stores information about command permissions.
 */
public abstract class BukkitPermissionHolder {

    private final Map<Class<? extends BukkitCommand>, Set<String>> permissions;

    /**
     * Creates a new BukkitPermissionHolder instance
     */
    protected BukkitPermissionHolder() {
        this.permissions = new HashMap<Class<? extends BukkitCommand>, Set<String>>();
    }

    /**
     * Returns the permission-set required to execute a command or null if the command has no permission attached.
     *
     * <p><b>Note</b>: The set contains a list of permissions of which <b>one</b> must be owned by the sender in order to process the command.</p>
     *
     * @param command The command to which the permission is required
     * @return The permissions / null
     */
    protected Set<String> getPermissions(Class<? extends BukkitCommand> command) {
        return permissions.get(command);
    }

    /**
     * Validates if a command has the specified permission attached to it.
     *
     * @param command The command for which to check the permission.
     * @param permission The permission to validate.
     * @return True if the command has the said permission attached to it.
     */
    protected boolean containsPermission(Class<? extends BukkitCommand> command, String permission) {
        if (!permissions.containsKey(command)) {
            return false;
        }

        return permissions.get(command).contains(permission.toLowerCase());
    }

    /**
     * Adds a permission required for a command.
     *
     * @param command The command for which the permission must be set.
     * @param permission The permission required to execute the command.
     * @deprecated Moved to [addPermission()]{@link #addPermission(java.lang.Class, java.lang.String)}
     */
    protected void setPermission(Class<? extends BukkitCommand> command, String permission) {
        addPermission(command, permission);
    }

    /**
     * Adds a permission to the accepted permissions for a command.
     *
     * @param command The command for which the permission must be set.
     * @param permission The permission required to execute the command.
     */
    protected void addPermission(Class<? extends BukkitCommand> command, String permission) {

        Set<String> requires = permissions.get(command);

        if (requires == null) {
            requires = new HashSet<String>();
        }

        requires.add(permission.toLowerCase());
        permissions.put(command, requires);
    }

    /**
     * Clears all the permissions stored.
     */
    protected void clearPermissions() {
        permissions.clear();
    }

    /**
     * Clears all the permissions attached to a command.
     *
     * @param command The command from which the permissions must be cleared.
     */
    protected void clearPermissions(Class<? extends BukkitCommand> command) {
        if (permissions.containsKey(command)) {
            return;
        }

        permissions.put(command, null);
    }

    /**
     * Should initialize all permissions.
     *
     * <p><b>In normal conditions, this should never be ran.</b></p>
     */
    protected abstract void initPermissions();
}

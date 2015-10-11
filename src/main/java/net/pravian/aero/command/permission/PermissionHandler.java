/*
 * Copyright 2015 Jerom van der Sar.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.pravian.aero.command.permission;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.pravian.aero.command.CommandBase;

/**
 * Represents a class which stores information about command permissions.
 */
public abstract class PermissionHandler {

    private final Map<Class<? extends CommandBase<?>>, Set<String>> permissions;

    /**
     * Creates a new BukkitPermissionHolder instance
     */
    protected PermissionHandler() {
        this.permissions = new HashMap<Class<? extends CommandBase<?>>, Set<String>>();
    }

    /**
     * Returns the permission-set required to execute a command or null if the command has no permission attached.
     *
     * <p>
     * <b>Note</b>: The set contains a list of permissions of which <b>one</b> must be owned by the sender in order to process the command.</p>
     *
     * @param command The command to which the permission is required
     * @return The permissions / null
     */
    public Set<String> getPermissions(Class<? extends CommandBase<?>> command) {
        return permissions.get(command);
    }

    public boolean containsPermissions(Class<? extends CommandBase<?>> command) {
        return permissions.containsKey(command);
    }

    /**
     * Validates if a command has the specified permission attached to it.
     *
     * @param command The command for which to check the permission.
     * @param permission The permission to validate.
     * @return True if the command has the said permission attached to it.
     */
    public boolean containsPermission(Class<? extends CommandBase<?>> command, String permission) {
        if (!containsPermissions(command)) {
            return false;
        }

        return permissions.get(command).contains(permission.toLowerCase());
    }

    /**
     * Adds a permission to the accepted permissions for a command.
     *
     * @param command The command for which the permission must be set.
     * @param permission The permission required to execute the command.
     */
    public void addPermission(Class<? extends CommandBase<?>> command, String permission) {

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
    public void clearPermissions() {
        permissions.clear();
    }

    /**
     * Clears all the permissions attached to a command.
     *
     * @param command The command from which the permissions must be cleared.
     */
    public void clearPermissions(Class<? extends CommandBase<?>> command) {
        if (permissions.containsKey(command)) {
            return;
        }

        permissions.put(command, null);
    }

    /**
     * Should initialize all permissions.
     *
     * <p>
     * <b>In normal conditions, this should never be ran.</b></p>
     */
    public abstract void initPermissions();
}

package net.pravian.bukkitlib.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents the annotation for defining the permission, source and usage for a BukkitCommand.
 *
 * @see BukkitCommand
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandPermissions {

    /**
     * The permission which is required to use this command.
     *
     * <p>Ignored if a custom BukkitPermissionHolder has been set.</p>
     *
     * @return The permission which is required for this command.
     * @see BukkitCommandHandler#setPermissionHandler(BukkitPermissionHandler)
     */
    String permission() default "";

    /**
     * The source where this command may be executed from.
     *
     * @return The required source.
     */
    SourceType source() default SourceType.ANY;
}

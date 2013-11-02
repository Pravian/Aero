package net.pravian.bukkitlib.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandPermissions
{
    String permission() default "";

    SourceType source() default SourceType.ANY;
    
    String usage() default "";
}

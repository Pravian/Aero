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
package net.pravian.aero.command;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.pravian.aero.command.handler.AeroCommandHandler;
import net.pravian.aero.command.permission.AeroPermissionHandler;

// TODO docs
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CommandOptions {

  public String usage() default "";

  public SourceType source() default SourceType.ANY;

  public SourceType[] sources() default {};

  public String permission() default "";

  public String description() default "";

  public String aliases() default "";

  /**
   * The sub permission which is required to use this command.
   *
   * <p>Ignored if a custom BukkitPermissionHolder has been set.
   *
   * @return The subPermission which is required for this command.
   * @see AeroCommandHandler#setPermissionHandler(AeroPermissionHandler)
   */
  public String subPermission() default "";
}

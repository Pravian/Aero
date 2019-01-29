/*
 * Copyright 2015 Pravian Systems.
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
package net.pravian.aero.reflection;

import java.lang.reflect.Constructor;

public class ConstructorInvoker implements ReflectionAccess {

  private Constructor constructor = null;
  private boolean wasAccessible = false;

  public ConstructorInvoker(Constructor method) {
    this(method, method.isAccessible());
  }

  public ConstructorInvoker(Constructor method, boolean wasAccessible) {
    this.constructor = method;
    this.wasAccessible = wasAccessible;
  }

  public Constructor getConstructor() {
    return this.constructor;
  }

  @Override
  public boolean isAccessible() {
    return this.constructor.isAccessible();
  }

  @Override
  public ConstructorInvoker setAccessible(boolean flag) {
    this.constructor.setAccessible(flag);
    return this;
  }

  public Object newInstance(Object... parameters) throws Exception {
    this.constructor.setAccessible(true);
    Object newInstance =
        parameters != null && parameters.length > 0
            ? this.constructor.newInstance(parameters)
            : this.constructor.newInstance();
    if (!this.wasAccessible()) {
      this.constructor.setAccessible(false);
    }
    return newInstance;
  }

  @Override
  public ConstructorInvoker setAccessible() {
    return this.setAccessible(this.wasAccessible);
  }

  @Override
  public boolean wasAccessible() {
    return this.wasAccessible;
  }
}

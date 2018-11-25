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

import java.lang.reflect.Method;

public class MethodInvoker implements ReflectionAccess
{

    private Method method = null;
    private boolean wasAccessible = false;

    public MethodInvoker(Method method)
    {
        this(method, method.isAccessible());
    }

    public MethodInvoker(Method method, boolean wasAccessible)
    {
        this.method = method;
        this.wasAccessible = wasAccessible;
    }

    public Method getMethod()
    {
        return this.method;
    }

    public Object invoke(Object instance, Object... paramValues) throws Exception
    {
        this.method.setAccessible(true);
        Object invoked = this.method.invoke(instance, paramValues);
        this.method.setAccessible(this.wasAccessible());
        return invoked;
    }

    @Override
    public boolean isAccessible()
    {
        return this.method.isAccessible();
    }

    @Override
    public MethodInvoker setAccessible(boolean flag)
    {
        this.method.setAccessible(flag);
        return this;
    }

    @Override
    public MethodInvoker setAccessible()
    {
        return this.setAccessible(this.wasAccessible);
    }

    @Override
    public boolean wasAccessible()
    {
        return this.wasAccessible;
    }
}

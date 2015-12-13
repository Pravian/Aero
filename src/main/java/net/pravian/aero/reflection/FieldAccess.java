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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public class FieldAccess implements ReflectionAccess {

    private Field field = null;
    private boolean wasAccessible = false;

    public FieldAccess(Field method) {
        this(method, method.isAccessible());
    }

    public FieldAccess(Field method, boolean wasAccessible) {
        this.field = method;
        this.wasAccessible = wasAccessible;
    }

    public <T> T get(Class<T> unused) throws Exception {
        return unused == String.class ? (T) this.getObject(null).toString() : (unused == Integer.class ? (T) new Integer(Integer.parseInt(this.getObject(null).toString())) : unused == Boolean.class ? (T) new Boolean(Boolean.parseBoolean(this.getObject(null).toString())) : (T) this.getObject(null));
    }

    public <T> T get(Class<T> unused, Object instance) throws Exception {
        return unused == String.class ? (T) this.getObject(instance).toString() : (unused == Integer.class ? (T) new Integer(Integer.parseInt(this.getObject(instance).toString())) : unused == Boolean.class ? (T) new Boolean(Boolean.parseBoolean(this.getObject(instance).toString())) : (T) this.getObject(instance));
    }

    public Object getObject(Object instance) throws Exception {
        this.field.setAccessible(true);
        try {
            Object value = this.field.get(instance);
            this.field.setAccessible(this.wasAccessible());
            return value;
        } catch (Exception ex) {
            this.field.setAccessible(this.wasAccessible());
            throw ex;
        }
    }

    public Field getField() {
        return this.field;
    }

    @Override
    public boolean isAccessible() {
        return this.field.isAccessible();
    }

    public boolean isStatic() {
        return Modifier.isStatic(this.field.getModifiers());
    }

    public void set(Object value) throws Exception {
        this.set(null, value);
    }

    public void set(Object instance, Object value) throws Exception {
        if (Modifier.isFinal(this.field.getModifiers())) {
            this.field.setAccessible(true);

            FieldAccess modifiersFieldAccess = Reflection.getField(Field.class, "modifiers");
            Field modifiersField = modifiersFieldAccess.getField();
            modifiersField.setAccessible(true);

            int previousModifier = modifiersField.getInt(this.field);
            modifiersField.setInt(this.field, this.field.getModifiers() & ~Modifier.FINAL);

            this.field.set(instance, value);

            modifiersField.setInt(this.field, previousModifier);
            modifiersField.setAccessible(modifiersFieldAccess.wasAccessible());

            this.field.setAccessible(this.wasAccessible());
        } else {
            this.field.setAccessible(true);
            this.field.set(instance, value);
            if (!this.wasAccessible()) {
                this.field.setAccessible(false);
            }
        }
    }

    @Override
    public FieldAccess setAccessible() {
        return this.setAccessible(this.wasAccessible);
    }

    @Override
    public FieldAccess setAccessible(boolean flag) {
        this.field.setAccessible(flag);
        return this;
    }

    @Override
    public boolean wasAccessible() {
        return this.wasAccessible;
    }
}

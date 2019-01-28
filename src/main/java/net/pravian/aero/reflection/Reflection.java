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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Reflection {

    public static String VERSION = Bukkit.getServer().getClass().getPackage().getName()
        .split("\\.")[3];
    public static String NMS = "net.minecraft.server";
    public static String OBC = "org.bukkit.craftbukkit";

    /**
     * Get a class from OBC.
     *
     * @param className - The name of the class.
     * @return The class from OBC package.
     */
    public static Class getBukkitClass(String className) {
        try {
            return Class.forName(NMS + "." + className);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Get the Bukkit NMS version ID.
     *
     * @return The bukkit NMS version ID.
     */
    public static String getBukkitVersion() {
        return VERSION;
    }

    /**
     * Get a class within a class.
     *
     * @param clazz - The super class.
     * @param className - The subclass name.
     * @return The class within a class.
     */
    public static Class getSubClass(Class clazz, String className) {
        if (clazz != null && className != null) {
            try {
                do {
                    for (Class insideClass : clazz.getDeclaredClasses()) {
                        if (insideClass != null && insideClass.getSimpleName().equals(className)) {
                            return insideClass;
                        }
                    }
                    for (Class insideClass : clazz.getClasses()) {
                        if (insideClass != null && insideClass.getSimpleName().equals(className)) {
                            return insideClass;
                        }
                    }
                }
                while (clazz.getSuperclass() != Object.class && ((clazz = clazz.getSuperclass())
                    != null));
            } catch (Exception ignore) {
            }
        }
        return null;
    }

    /**
     * Get a constructor from a class by the constructor index.
     *
     * @param clazz - The class.
     * @param constructorIndex - The constructor's index in the array of constructors. E.g. 0 for
     * the first constructor.
     * @return The constructor from a class.
     */
    public static ConstructorInvoker getConstructor(Class clazz, int constructorIndex) {
        if (clazz != null) {
            try {
                Constructor constructor = clazz.getConstructors()[constructorIndex];
                if (constructor == null) {
                    constructor = clazz.getDeclaredConstructors()[constructorIndex];
                }
                if (constructor != null) {
                    return new ConstructorInvoker(constructor);
                } else if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
                    return getConstructor(clazz.getSuperclass(), constructorIndex);
                }
            } catch (ArrayIndexOutOfBoundsException ignore) {
                try {
                    Constructor method = clazz.getDeclaredConstructors()[constructorIndex];
                    if (method != null) {
                        return new ConstructorInvoker(method);
                    } else if (clazz.getSuperclass() != null
                        && clazz.getSuperclass() != Object.class) {
                        return getConstructor(clazz.getSuperclass(), constructorIndex);
                    }
                } catch (Exception ignore2) {
                }
            } catch (Exception ignore) {
            }
        }
        return null;
    }

    /**
     * Get a constructor from a class by the parameter types.
     *
     * @param clazz - The class.
     * @param parameterTypes - The parameter types (classes).
     * @return The constructor from a class.
     */
    @SuppressWarnings("unchecked") // Reflection
    public static ConstructorInvoker getConstructor(Class clazz, Class... parameterTypes) {
        if (clazz != null) {
            try {
                Constructor constructor = clazz.getConstructor(parameterTypes);
                if (constructor == null) {
                    constructor = clazz.getDeclaredConstructor(parameterTypes);
                }
                if (constructor != null) {
                    return new ConstructorInvoker(constructor);
                } else if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
                    return getConstructor(clazz.getSuperclass(), parameterTypes);
                }
            } catch (NoSuchMethodException ignore) {
                try {
                    Constructor constructor = clazz.getDeclaredConstructor(parameterTypes);
                    if (constructor != null) {
                        return new ConstructorInvoker(constructor);
                    } else if (clazz.getSuperclass() != null
                        && clazz.getSuperclass() != Object.class) {
                        return getConstructor(clazz.getSuperclass(), parameterTypes);
                    }
                } catch (Exception ignored) {
                }
            } catch (Exception ignore) {
            }
        }
        return null;
    }

    /**
     * Get all the declared fields in a class.
     *
     * @param clazz - The class.
     * @return The list of the declared fields.
     */
    public static List<FieldAccess> getDeclaredFields(Class clazz, boolean superClass) {
        List<FieldAccess> fieldList = new ArrayList<FieldAccess>();
        if (clazz != null) {
            try {
                if (superClass) {
                    do {
                        for (Field field : clazz.getDeclaredFields()) {
                            if (field != null) {
                                fieldList.add(new FieldAccess(field));
                            }
                        }
                    }
                    while (clazz.getSuperclass() != Object.class && ((clazz = clazz.getSuperclass())
                        != null));
                } else {
                    for (Field field : clazz.getDeclaredFields()) {
                        if (field != null) {
                            fieldList.add(new FieldAccess(field));
                        }
                    }
                }
            } catch (Exception ignore) {
            }
        }
        return fieldList;
    }

    /**
     * Get an enum constant from an enum.
     *
     * @param enumClass - The class (that's an enum)
     * @param enumName - The constant's name
     * @return The enum constant.
     */
    public static Object getEnum(Class enumClass, String enumName) {
        if (enumClass != null) {
            for (Object ob : enumClass.getEnumConstants()) {
                if (ob != null && ob.toString().equals(enumName)) {
                    return ob;
                }
            }
        }
        return null;
    }

    /**
     * Get a field from a class by the field name.
     *
     * @param clazz - The class.
     * @param fieldName - The name of the method.
     * @return The field from a class.
     */
    public static FieldAccess getField(Class clazz, String fieldName) {
        if (clazz != null && fieldName != null) {
            do {
                try {
                    Field field = clazz.getField(fieldName);
                    if (field == null) {
                        field = clazz.getDeclaredField(fieldName);
                    }
                    if (field != null) {
                        return new FieldAccess(field);
                    }
                } catch (NoSuchFieldException ex) {
                    try {
                        Field field = clazz.getDeclaredField(fieldName);
                        if (field != null) {
                            return new FieldAccess(field);
                        }
                    } catch (Exception ignored2) {
                    }
                } catch (Exception ignored) {
                }
            }
            while (clazz.getSuperclass() != Object.class && ((clazz = clazz.getSuperclass())
                != null));
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    /**
     * Get a field as a type.
     *
     * @param instance - The instance as an Object.
     * @param fieldName - The field name.
     * @return The field's value for that instance.
     */
    public static <T> T getField(Object instance, String fieldName) {
        Class<?> checkClass = instance.getClass();
        do {
            try {
                Field field = checkClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return (T) field.get(instance);
            } catch (Exception e) {
            }
        }
        while (checkClass.getSuperclass() != Object.class && (
            (checkClass = checkClass.getSuperclass())
                != null));
        return null;
    }

    /**
     * Get a field from a class by the field type's class name.
     *
     * @param clazz - The class.
     * @param className - The field type's name.
     * @param fieldIndex - The field index.
     * @return The field from a class.
     */
    public static FieldAccess getFieldByClass(Class clazz, String className, int fieldIndex) {
        List<FieldAccess> fieldAccessList = new LinkedList<FieldAccess>();
        for (FieldAccess field : getFields(clazz)) {
            if (field.getField().getType().getSimpleName().equals(className)) {
                fieldAccessList.add(field);
            }
        }
        return fieldIndex < fieldAccessList.size() ? fieldAccessList.get(fieldIndex) : null;
    }

    /**
     * Get all the fields in a class.
     *
     * @param clazz - The class.
     * @return The Map of the fields and the old accessibility from a class.
     */
    public static List<FieldAccess> getFields(Class clazz) {
        List<FieldAccess> fieldList = new ArrayList<FieldAccess>();
        if (clazz != null) {
            try {
                for (Field field : clazz.getFields()) {
                    if (field != null) {
                        fieldList.add(new FieldAccess(field));
                    }
                }
                for (Field field : clazz.getDeclaredFields()) {
                    if (field != null) {
                        fieldList.add(new FieldAccess(field));
                    }
                }
            } catch (Exception ex) {
            }
        }
        return fieldList;
    }

    /**
     * Get a method from a class by the method name.
     *
     * @param clazz - The class.
     * @param methodName - The name of the method.
     * @return The method from a class.
     */
    @SuppressWarnings("unchecked")
    public static MethodInvoker getMethod(Class clazz, String methodName, Class... parameterTypes) {
        return getMethod(clazz, methodName, true, parameterTypes);
    }

    /**
     * Get a method from a class by the method name.
     *
     * @param clazz - The class.
     * @param methodName - The name of the method.
     * @return The method from a class.
     */
    @SuppressWarnings("unchecked")
    public static MethodInvoker getMethod(Class clazz, String methodName, boolean superClasses,
        Class... parameterTypes) {
        if (clazz != null && methodName != null) {
            try {
                Method method = clazz.getMethod(methodName, parameterTypes);
                if (method == null) {
                    method = clazz.getDeclaredMethod(methodName, parameterTypes);
                }
                if (method != null) {
                    return new MethodInvoker(method);
                } else if (superClasses && clazz.getSuperclass() != null
                    && clazz.getSuperclass() != Object.class) {
                    return getMethod(clazz.getSuperclass(), methodName, parameterTypes);
                }
            } catch (NoSuchMethodException ignore) {
                try {
                    Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                    if (method != null) {
                        return new MethodInvoker(method);
                    } else if (superClasses && clazz.getSuperclass() != null
                        && clazz.getSuperclass() != Object.class) {
                        return getMethod(clazz.getSuperclass(), methodName, parameterTypes);
                    }
                } catch (Exception ignore2) {
                }
            } catch (Exception ignore) {
            }
        }
        return null;
    }

    /**
     * Get all the methods in a class and its super class(es).
     *
     * @param clazz - The class.
     * @return The List of methods in a class and its super class(es).
     */
    public static List<MethodInvoker> getMethods(Class clazz) {
        return getMethods(clazz, true);
    }

    /**
     * Get all the methods in a class and optionally its super class.
     *
     * @param clazz - The class.
     * @param showSuperclassMethods - Return methods from the super class.
     * @return The List of methods in a class.
     */
    public static List<MethodInvoker> getMethods(Class clazz, boolean showSuperclassMethods) {
        List<MethodInvoker> methodList = new ArrayList<MethodInvoker>();
        if (clazz != null) {
            try {
                for (Method method : clazz.getMethods()) {
                    if (method != null) {
                        boolean isSuperclassMethod = false;
                        if (!showSuperclassMethods && clazz.getSuperclass() != null
                            && clazz.getSuperclass() != Object.class) {
                            for (Method superMethod : clazz.getSuperclass().getMethods()) {
                                if (superMethod != null && superMethod.getName()
                                    .equals(method.getName())) {
                                    isSuperclassMethod = true;
                                    break;
                                }
                            }
                        }
                        if (showSuperclassMethods || !isSuperclassMethod) {
                            methodList.add(new MethodInvoker(method));
                        }
                    }
                }
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method != null) {
                        boolean isSuperclassMethod = false;
                        if (!showSuperclassMethods && clazz.getSuperclass() != null
                            && clazz.getSuperclass() != Object.class) {
                            for (Method superMethod : clazz.getSuperclass().getDeclaredMethods()) {
                                if (superMethod != null && superMethod.getName()
                                    .equals(method.getName())) {
                                    isSuperclassMethod = true;
                                    break;
                                }
                            }
                        }
                        if (showSuperclassMethods || !isSuperclassMethod) {
                            methodList.add(new MethodInvoker(method));
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return methodList;
    }

    @SuppressWarnings("unchecked")
    public static boolean instanceOf(Class clazz, Class superClass) {
        return clazz != null && superClass != null && clazz.isAssignableFrom(superClass);
    }

    public static Class<?> getCraftBukkitClass(String className) {
        try {
            return Class.forName(OBC + "." + VERSION + "." + className);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Class<?> getMinecraftClass(String className) {
        try {
            return Class.forName(NMS + "." + VERSION + "." + className);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static Class<?> getPacketClass() {
        return getMinecraftClass("Packet");
    }

    public static Object getHandle(Object entity) {
        try {
            Method method = entity.getClass().getDeclaredMethod("getHandle");
            return method.invoke(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Object getHandle(Entity entity) {
        try {
            Method method = entity.getClass().getMethod("getHandle");

            return method.invoke(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object entity_player = getHandle(player);
            Field player_connection = entity_player.getClass().getField("playerConnection");
            player_connection.setAccessible(true);
            Object initiatedConnection = player_connection.get(entity_player);
            Method sendPacket = initiatedConnection.getClass()
                .getMethod("sendPacket", getPacketClass());
            sendPacket.invoke(initiatedConnection, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

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
package net.pravian.aero.command.parser;

import org.bukkit.entity.Player;

public enum Parsers {

    PLAYER(Player.class, new PlayerParser()),
    INTEGER_PRIM(int.class, new IntegerParser()),
    INTEGER(Integer.class, INTEGER_PRIM.parser),
    FLOAT_PRIM(float.class, new FloatParser()),
    FLOAT(Float.class, FLOAT_PRIM.parser),
    DOUBLE_PRIM(double.class, new DoubleParser()),
    DOUBLE(Double.class, DOUBLE_PRIM.parser);
    //
    private final Class<?> fromType;
    private final Parser parser;

    private Parsers(Class<?> fromType, Parser parser) {
        this.fromType = fromType;
        this.parser = parser;
    }

    public Class<?> getFromClass() {
        return fromType;
    }

    public Parser getParser() {
        return parser;
    }

    public Class<? extends Parser> getParserClass() {
        return getParser().getClass();
    }

    public static Parser forClass(Class<?> type) {
        for (Parsers loopType : Parsers.values()) {
            if (loopType.getClass().isAssignableFrom(type)) {
                return loopType.getParser();
            }
        }

        return null;
    }
}

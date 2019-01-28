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
package net.pravian.aero.command.dynamic.parser;

import org.bukkit.entity.Player;

public enum DefaultParser {

  PLAYER(new PlayerParser(), Player.class),
  INTEGER(new IntegerParser(), int.class, Integer.class),
  FLOAT(new FloatParser(), float.class, Float.class),
  DOUBLE(new DoubleParser(), double.class, Double.class);
  //
  private final Class<?>[] types;
  private final Parser parser;

  private DefaultParser(Parser parser, Class<?>... types) {
    this.types = types;
    this.parser = parser;
  }

  public static Parser forClass(Class<?> type) {
    for (DefaultParser defaultParser : DefaultParser.values()) {
      for (Class<?> loopType : defaultParser.getTypes()) {
        if (loopType.equals(type)) {
          return defaultParser.getParser();
        }
      }
    }

    for (DefaultParser defaultParser : DefaultParser.values()) {
      for (Class<?> loopType : defaultParser.getTypes()) {
        if (loopType.isAssignableFrom(type)) {
          return defaultParser.getParser();
        }
      }
    }

    return null;
  }

  public Class<?>[] getTypes() {
    return types;
  }

  public Parser getParser() {
    return parser;
  }

  public Class<? extends Parser> getParserClass() {
    return getParser().getClass();
  }
}

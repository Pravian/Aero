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
package net.pravian.aero.component;

import com.google.common.base.Preconditions;
import java.lang.reflect.ParameterizedType;
import net.pravian.aero.Aero;
import net.pravian.aero.AeroException;
import net.pravian.aero.base.PluginContainer;
import net.pravian.aero.plugin.AeroLogger;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.Server;

public abstract class PluginComponent<T extends AeroPlugin<T>> implements PluginContainer<T> {

  protected final T plugin;
  protected final AeroLogger logger;
  protected final Server server;

  // TODO: Describe awesomeness
  @SuppressWarnings("unchecked")
  public PluginComponent() {

    T foundPlugin = null;

    Class<?> checkClass = getClass();

    // Try each superclass iteratively
    do {
      final Class<T> typeClass;
      try {
        // Get parameter class of checkClass extending PluginComponent<SomePlugin>
        typeClass = (Class<T>) ((ParameterizedType) checkClass
            .getGenericSuperclass())
            .getActualTypeArguments()[0];
      } catch (Exception ex) { // Nope, this class doesn't directly extend PluginComponent<SomePlugin>
        continue;
      }

      // Find `SomePlugin` instance
      for (AeroPlugin registeredPlugin : Aero.getInstance().getRegisteredPlugins()) {
        if (typeClass.isAssignableFrom(registeredPlugin.getPlugin().getClass())) {
          foundPlugin = (T) registeredPlugin.getPlugin();
        }
      }

    }
    while ((checkClass = checkClass.getSuperclass()) != null);

    if (foundPlugin == null) {
      throw new AeroException(
          "Could not determine plugin class type! (Are you properly extending with generics?)");
    }

    this.plugin = foundPlugin;
    this.logger = plugin.getPluginLogger();
    this.server = plugin.getServer();
  }

  public PluginComponent(T plugin) {
    this(Preconditions.checkNotNull(plugin, "Plugin may not be null!"), plugin.getPluginLogger());
  }

  public PluginComponent(T plugin, AeroLogger logger) {
    this.plugin = Preconditions.checkNotNull(plugin, "Plugin may not be null!");
    this.logger = Preconditions.checkNotNull(logger, "Logger may not be null!");
    this.server = plugin.getServer();
  }

  @Override
  public T getPlugin() {
    return plugin;
  }

  public AeroLogger getLogger() {
    return logger;
  }
}

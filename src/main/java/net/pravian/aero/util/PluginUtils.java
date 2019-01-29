package net.pravian.aero.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

/** Represents all Plugin-related utilities. */
public class PluginUtils {

  private PluginUtils() {}

  /**
   * Disables all plugins.
   *
   * <p><b>Warning</b>: This includes the plugin which called this method.
   *
   * @see #disableAllPlugins(List)
   */
  public static void disableAllPlugins() {
    disableAllPlugins(Arrays.asList(new Plugin[] {}));
  }

  /**
   * Disables all plugins except the specified plugin.
   *
   * @param ignore Plugin to ignore.
   * @see #disableAllPlugins(List)
   */
  public static void disableAllPlugins(Plugin ignore) {
    disableAllPlugins(Arrays.asList(new Plugin[] {ignore}));
  }

  /**
   * Disables all plugins except the list of plugins.
   *
   * @param ignoreList The list of ignored plugins.
   */
  public static void disableAllPlugins(List<Plugin> ignoreList) {
    for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
      if (!ignoreList.contains(plugin)) {
        Bukkit.getPluginManager().disablePlugin(plugin);
      }
    }
  }

  /** Enables all plugins. */
  public static void enableAllPlugins() {
    for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
      Bukkit.getPluginManager().enablePlugin(plugin);
    }
  }

  /**
   * Loads a plugin from the plugins folder.
   *
   * @param fileName The filename of the plugin to load.
   * @see #loadPlugin(File)
   */
  public static void loadPlugin(String fileName)
      throws InvalidPluginException, InvalidDescriptionException {
    loadPlugin(new File(Plugins.getPluginsFolder(), fileName));
  }

  /**
   * Loads a plugin from the plugins folder.
   *
   * @param plugin The file of the plugin to load.
   */
  public static void loadPlugin(File plugin)
      throws InvalidPluginException, InvalidDescriptionException {
    Bukkit.getPluginManager().loadPlugin(plugin);
  }

  /**
   * Reloads a plugin.
   *
   * @param plugin The plugin to reload.
   */
  public static void reloadPlugin(Plugin plugin) {
    Bukkit.getPluginManager().disablePlugin(plugin);
    Bukkit.getPluginManager().enablePlugin(plugin);
  }
}

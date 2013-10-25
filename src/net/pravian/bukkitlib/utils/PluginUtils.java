package net.pravian.bukkitlib.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import net.pravian.util.SingletonBlock;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

public class PluginUtils {

    public static void eachPlugin(SingletonBlock<Plugin> block) {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            block.run(plugin);
        }
    }

    public static void disableAllPlugins() {
        disableAllPlugins(Arrays.asList(new Plugin[]{}));
    }

    public static void disableAllPlugins(Plugin ignore) {
        disableAllPlugins(Arrays.asList(new Plugin[]{ignore}));
    }

    public static void disableAllPlugins(List<Plugin> ignoreList) {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (!ignoreList.contains(plugin)) {
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
    }
    
    public static void enableAllPlugins() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            Bukkit.getPluginManager().enablePlugin(plugin);
        }
    }

    public static void loadPlugin(String name) throws InvalidPluginException, InvalidDescriptionException {
        Bukkit.getPluginManager().loadPlugin(new File(FileUtils.getPluginsFolder(), name));
    }
    
    public static void reloadPlugin(Plugin plugin) {
        Bukkit.getPluginManager().disablePlugin(plugin);
        Bukkit.getPluginManager().enablePlugin(plugin);
    }
}

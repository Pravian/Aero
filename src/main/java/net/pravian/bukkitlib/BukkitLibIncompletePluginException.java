package net.pravian.bukkitlib;

import org.bukkit.plugin.PluginBase;

public final class BukkitLibIncompletePluginException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BukkitLibIncompletePluginException(PluginBase plugin) {
        super("plugin.yml is incomplete for plugin: " + plugin.getName());
    }
}

package net.pravian.bukkitlib.implementation;

import java.util.List;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {

    /**
     * Returns the version of this plugin
     *
     * @return the version
     */
    public String getVersion() {
        return super.getDescription().getVersion();
    }

    /**
     * Returns the first author of this plugin.
     *
     * @return the author
     */
    public String getAuthor() {
        return super.getDescription().getAuthors().get(0);
    }

    /**
     * Returns the authors of this plugin.
     *
     * @return the authors
     */
    public List<String> getAuthors() {
        return super.getDescription().getAuthors();
    }

    /**
     * Registers an event-listener
     *
     * @param listener to register
     * @return true if the listener registered correctly
     */
    public final boolean register(Listener listener) {
        if (listener == null) {
            return false;
        }

        if (listener != this) {
            super.getServer().getPluginManager().registerEvents(listener, this);
        }
        return true;
    }

    /**
     * Registers an event-listener.
     *
     * @param listener class to register
     * @return true if the listener registered correctly
     */
    public final boolean register(Class<? extends Listener> listener) {
        if (listener == null) {
            return false;
        }

        try {
            register(listener.newInstance());
        } catch (InstantiationException ex) {
            return false;
        } catch (IllegalAccessException ex) {
            return false;
        }

        return true;
    }
}

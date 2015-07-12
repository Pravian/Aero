package net.pravian.aero.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.plugin.Plugin;

/**
 * Represents all File-related utilities.
 */
public class Plugins {

    private Plugins() {
    }

    /**
     * Downloads a file from the specified URL and saves it at the specified location.
     *
     * @param url The URL from where to download the file from.
     * @param output The file where the file will be stored.
     * @throws MalformedURLException @throws IOException
     */
    public static void downloadFile(String url, File output) throws MalformedURLException, IOException {
        final URL website = new URL(url);
        final ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        final FileOutputStream fos = new FileOutputStream(output);
        fos.getChannel().transferFrom(rbc, 0, 1 << 24);
        fos.close();
    }

    /**
     * Saves a raw Object to a file.
     *
     * @param object The object to save.
     * @param file The file where the object will be stored.
     * @throws IOException
     */
    public static void saveObject(Object object, File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(object);
        oos.close();
    }

    /**
     * Attempts to load a raw Object from a file.
     *
     * @param file The file where the object is stored.
     * @throws IOException
     */
    public static Object loadObject(File file) throws IOException, ClassNotFoundException {
        if (!file.exists()) {
            throw new IllegalStateException();
        }

        final ObjectInputStream oos = new ObjectInputStream(new FileInputStream(file));
        final Object object = oos.readObject();
        oos.close();

        return object;
    }

    /**
     * Returns a file at located at the Plugins Data folder.
     *
     * @param plugin The plugin to use
     * @param name The name of the file.
     * @return The requested file.
     */
    public static File getPluginFile(Plugin plugin, String name) {
        return new File(plugin.getDataFolder(), name);
    }

    /**
     * Returns the root location of the CraftBukkit server.
     *
     * @return The current working directory.
     */
    public static File getRoot() {
        return new File(".");
    }

    /**
     * Gets the Jar File where a given Plugin is loaded from. If the plugin is not in a Jar file, null is returned instead.
     *
     * <p>
     * Author: bergerkiller</p>
     *
     * @param plugin to get the Jar File of
     * @return the Jar File in which the plugin resides, or null if none found
     */
    public static File getPluginJarFile(AeroPlugin plugin) {
        final Class<?> pluginClass = plugin.getClass();
        try {
            URI uri = pluginClass.getProtectionDomain().getCodeSource().getLocation().toURI();
            File file = new File(uri);
            if (file.exists()) {
                return file;
            }
        } catch (Exception ex) {
            plugin.handleException("Could not get plugin Jar file!", ex);
        }
        return null;
    }

    /**
     * Obtains the folder in which plugin-specific information is contained.<br>
     * Usually this folder is <b>/plugins/[pluginname]</b>.<br>
     * This method can be used to properly obtain this folder if the plugin is not initialized yet.
     *
     * <p>
     * Author: bergerkiller</p>
     *
     * @param plugin to get the data folder of
     * @return Plugin data folder (never null)
     */
    public static File getPluginDataFolder(AeroPlugin plugin) {
        File folder = plugin.getDataFolder();
        if (folder == null) {
            File jarFile = getPluginJarFile(plugin);
            if (jarFile == null) {
                throw new RuntimeException("Plugin data folder can not be obtained: Not a valid JAR plugin");
            }
            folder = new File(jarFile.getAbsoluteFile().getParentFile(), plugin.getName());
        }
        return folder;
    }

    /**
     * Returns the folder where all plugins are stored.
     *
     * @return The plugins folder.
     */
    public static File getPluginsFolder() {
        return new File(getRoot(), "plugins");
    }

    /**
     * Returns a file at the root of the CraftBukkit server.
     *
     * @param name The name of the file.
     * @return The requested file.
     */
    public static File getRootFile(String name) {
        return new File(getRoot(), name);
    }

    /**
     * Delete a specified folder and all contents quietly.
     *
     * <p>
     * <b>Warning</b>: This method will delete files, only folders!</p>
     *
     * @param file The folder to delete.
     * @return true if the delete was successful.
     */
    public static boolean deleteFolder(File file) {
        if (file.exists() && file.isDirectory()) {
            return Plugins.deleteFolder(file);
        }
        return false;
    }

    /**
     * Write the specified InputStream to a file.
     *
     * @param in The InputStream from which to read.
     * @param file The File to write to.
     * @throws IOException
     */
    public static void copy(InputStream in, File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        OutputStream out = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        in.close();
    }
}

package net.pravian.bukkitlib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.bukkit.plugin.Plugin;

public class FileUtils {

    public static void downloadFile(String url, File output) throws MalformedURLException, IOException {
        final URL website = new URL(url);
        final ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        final FileOutputStream fos = new FileOutputStream(output);
        fos.getChannel().transferFrom(rbc, 0, 1 << 24);
        fos.close();
    }

    public static void saveObject(Object object, File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(object);
        oos.close();
    }

    public static Object loadObject(File file) throws IOException, ClassNotFoundException {
        if (!file.exists()) {
            throw new IllegalStateException();
        }

        final ObjectInputStream oos = new ObjectInputStream(new FileInputStream(file));
        final Object object = oos.readObject();
        oos.close();

        return object;
    }

    public static File getPluginFile(Plugin plugin, String name) {
        return new File(plugin.getDataFolder(), name);
    }

    public static File getRoot() {
        return new File(".");
    }

    public static File getPluginsFolder() {
        return new File(getRoot(), "plugins");
    }

    public static File getRootFolder(String name) {
        return new File(getRoot(), name);
    }

    public static boolean deleteFolder(File file) {
        if (file.exists() && file.isDirectory()) {
            return org.apache.commons.io.FileUtils.deleteQuietly(file);
        }
        return false;
    }

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

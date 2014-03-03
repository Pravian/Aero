package net.pravian.bukkitlib;

import net.pravian.bukkitlib.runner.Order;
import net.pravian.bukkitlib.runner.OrderedRunner;
import org.bukkit.plugin.Plugin;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.runner.RunWith;

@RunWith(OrderedRunner.class)
public class BukkitLibTest {

    @Test(expected = BukkitLibNotInitializedException.class)
    @Order(1)
    public void testVersion() {
        BukkitLib.getVersion();
    }

    @Test(expected = BukkitLibNotInitializedException.class)
    @Order(2)
    public void testBuildNumber() {
        BukkitLib.getBuildNumber();
    }

    @Test(expected = BukkitLibNotInitializedException.class)
    @Order(3)
    public void testBuildDate() {
        BukkitLib.getBuildDate();
    }

    @Test(expected = BukkitLibNotInitializedException.class)
    @Order(4)
    public void testFullVersion() {
        BukkitLib.getFullVersion();
    }

    @Test
    @Order(5)
    public void testInit() {
        try {
            BukkitLib.init(null);
            fail();
        } catch (IllegalStateException ex) {
        }

        final Plugin plugin = mock(Plugin.class);

        try {
            BukkitLib.init(plugin);
            fail();
        } catch (NullPointerException ex) { // Thrown at metrics
        }

        // These shouldn't throw exceptions anymore
        BukkitLib.getVersion();
        BukkitLib.getBuildNumber();
        BukkitLib.getFullVersion();
        BukkitLib.getBuildDate();
    }
}

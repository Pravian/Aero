package net.pravian.bukkitlib;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitLib extends JavaPlugin {

    public static String name;
    public static String version;
    public static String author;
    public static final String[] FEATURES = new String[]{
        "Compact serialization for Bukkit objects (Inventory, Block, Location(Block & Entity)",
        "Easy configfile management with",
        "Utils for: Logging, Plugin, Commands, Ips, Player, the Server, Worlds, Date, File management, the Chat",
        "Includes Code-Block for pasing around methods",
        "Includes Cleanroom world generator"
    };
    public static final String[] CREDITS = new String[]{
        "Steven Lawson (Github) for his great helper methods in TotalFreedomMod",
        "Phil2812 (Bukkit Forums) for his inventory serializer",
        "mkyong (mykong.com) for his great examples on how to write objects to files",
        "nvx for their Cleanroom generator",
        "sk89q (sk89q.com) for his getField() method"
    };
    public static final String[] CHANGELOG = new String[]{
        "-- 1.2-Beta:",
        "  - Added VoidChunkGenerator",
        "  - Added SkyGridGenerator",
        "  - Added BukkitCommand, BukkitCommandHandler, CommandPermissions and SourceType",
        "  - Added Plugin interface",
        "  - Removed BukkitLib.init()",
        "  - Few more Utils methods",
        "",
        "-- 1.1-Beta:",
        "  - Added BukkitLib.init(JavaPlugin)",
        "  - Moved everything which needs plugins over to BukkitLib.getPlugin()",
        "",
        "-- 1.0-Beta: Initial release --",
        "  - Added Cleanroom generator",
        "  - Added YamlConfig",
        "  - Added SerializableBlock",
        "  - Added SerializableBlockLocation",
        "  - Added SerializableEntityLocation",
        "  - Added SerializableInventory",
        "  - Added ChatUtils, CommandUtils, DateUtils, FileUtils, IpUtils, LoggerUtils, PlayerUtils, ServerUtils and WorldUtils",
        "  - Using net.pravian.util.Block, SingletonBlock, ClosedBlock",
        ""
    };
    public static JavaPlugin plugin;

    @Override
    public void onLoad() {
        PluginDescriptionFile pdf = this.getDescription();
        name = pdf.getName();
        version = pdf.getVersion();
        author = pdf.getAuthors().get(0);
    }
}

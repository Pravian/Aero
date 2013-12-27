package net.pravian.bukkitlib.implementation;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BukkitLobby {

    private final Plugin plugin;
    private final boolean dynamic;
    private final List<Player> players;

    public BukkitLobby(Plugin plugin, boolean dynamic) {
        this.plugin = plugin;
        this.dynamic = dynamic;
        this.players = new ArrayList<Player>();
    }
}

package net.pravian.aero.command.parser;

import net.pravian.aero.util.Players;
import org.bukkit.entity.Player;

public class PlayerParser implements Parser<Player> {

    @Override
    public Player parse(String[] args, int offset) {
        return Players.getPlayer(args[offset]);
    }

}

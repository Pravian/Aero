package net.pravian.aero.command.dynamic.parser;

import java.util.List;
import net.pravian.aero.util.Players;
import org.bukkit.entity.Player;

public class PlayerParser implements Parser<Player> {

    @Override
    public int parse(List<? super Player> result, String[] args, int offset) throws Exception {
        final Player player = Players.getPlayer(args[offset]);
        if (player == null) {
            throw new ParseException("Could not find player: " + args[offset]);
        }
        result.add(player);
        return offset + 1;
    }
}

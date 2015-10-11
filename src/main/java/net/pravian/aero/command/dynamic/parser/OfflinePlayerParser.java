package net.pravian.aero.command.dynamic.parser;

import java.util.List;
import net.pravian.aero.util.Players;
import org.bukkit.OfflinePlayer;

public class OfflinePlayerParser implements Parser<OfflinePlayer> {

    @Override
    public int parse(List<? super OfflinePlayer> result, String[] args, int offset) throws Exception {
        OfflinePlayer player = Players.getOfflinePlayer(args[offset]);
        if (player == null) {
            throw new ParseException("Could not find player: " + args[offset]);
        }
        result.add(player);
        return offset + 1;
    }

}

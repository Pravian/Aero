package net.pravian.aero.command;

import java.util.List;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface CommandBase<T extends AeroPlugin<T>> {

    public void register(AeroCommandHandler<T> handler) throws RegistrationException;

    public void unregister();

    public AeroCommandHandler<T> getHandler();

    public Class<? extends CommandBase<T>> getCommandClass();

    public boolean runCommand(final CommandSender sender, final Command command, final String label, final String[] args);

    public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args);

    public void onInit();

}

package net.pravian.aero.command;

import java.util.List;
import net.pravian.aero.command.handler.AeroCommandHandler;
import net.pravian.aero.command.handler.SimpleCommandHandler;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface AeroCommandBase<T extends AeroPlugin<T>> {

  public void register(SimpleCommandHandler<T> handler) throws CommandRegistrationException;

  public void unregister();

  public boolean isRegistered();

  public void onInit();

  public AeroCommandHandler<T> getHandler();

  public Class<? extends AeroCommandBase<T>> getCommandClass();

  public boolean runCommand(
      final CommandSender sender, final Command command, final String label, final String[] args);

  public List<String> tabComplete(
      CommandSender sender, Command command, String label, String[] args);
}

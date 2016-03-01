package net.pravian.aero.command;

import java.util.List;
import net.pravian.aero.base.PluginMessage;
import net.pravian.aero.command.handler.AeroCommandHandler;
import net.pravian.aero.command.handler.SimpleCommandHandler;
import net.pravian.aero.component.PluginComponent;
import net.pravian.aero.plugin.AeroPlugin;
import net.pravian.aero.util.Players;
import net.pravian.aero.util.Plugins;
import net.pravian.aero.util.Worlds;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class AbstractCommandBase<T extends AeroPlugin<T>> extends PluginComponent<T> implements AeroCommandBase<T> {

    // Default arguments
    protected CommandSender sender;
    protected Command command;
    protected String label;
    protected String[] args;
    /**
     * Represents the player sending the command.
     *
     * <p>
     * <b>Note</b>: Might be null if the console is sending the command.</p>
     */
    protected Player playerSender;
    //
    private AeroCommandHandler<T> handler = null;

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends AeroCommandBase<T>> getCommandClass() {
        return (Class<? extends AeroCommandBase<T>>) getClass();
    }

    @Override
    public void register(SimpleCommandHandler<T> handler) throws CommandRegistrationException {
        if (this.handler != null) {
            throw new CommandRegistrationException("Command already registered to a handler!");
        }

        if (handler == null) {
            throw new CommandRegistrationException("Cannot register to null handler!");
        }

        this.handler = handler;
    }

    @Override
    public void unregister() {
        handler = null;
    }

    @Override
    public boolean isRegistered() {
        return handler != null;
    }

    @Override
    public AeroCommandHandler<T> getHandler() {
        return handler;
    }

    protected void setVariables(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!isRegistered()) {
            throw new CommandException("Could not set variables for unregistered command!");
        }
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;

        if (sender instanceof Player) {
            this.playerSender = (Player) sender;
        } else {
            this.playerSender = null;
        }
    }

    @Override
    public void onInit() { // Called when the command is initialised
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

}

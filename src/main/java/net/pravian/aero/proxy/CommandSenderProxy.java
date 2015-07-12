package net.pravian.aero.proxy;

import java.util.Set;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

/**
 * Represents a proxy for an inventory.
 */
public class CommandSenderProxy extends ProxyBase<CommandSender> implements CommandSender {

    /**
     * Creates a new CommandSenderProxy instance.
     *
     * @param base The proxy base.
     */
    public CommandSenderProxy(CommandSender base) {
        super(base);
    }

    @Override
    public void sendMessage(String string) {
        base.sendMessage(string);
    }

    @Override
    public void sendMessage(String[] strings) {
        base.sendMessage(strings);
    }

    @Override
    public Server getServer() {
        return base.getServer();
    }

    @Override
    public String getName() {
        return base.getName();
    }

    @Override
    public boolean isPermissionSet(String string) {
        return base.isPermissionSet(string);
    }

    @Override
    public boolean isPermissionSet(Permission prmsn) {
        return base.isPermissionSet(prmsn);
    }

    @Override
    public boolean hasPermission(String string) {
        return base.hasPermission(string);
    }

    @Override
    public boolean hasPermission(Permission prmsn) {
        return base.hasPermission(prmsn);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String string, boolean bln) {
        return base.addAttachment(plugin, string, bln);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return base.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String string, boolean bln, int i) {
        return base.addAttachment(plugin, string, bln, i);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        return base.addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(PermissionAttachment pa) {
        base.removeAttachment(pa);
    }

    @Override
    public void recalculatePermissions() {
        base.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return base.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return base.isOp();
    }

    @Override
    public void setOp(boolean bln) {
        base.setOp(bln);
    }
}

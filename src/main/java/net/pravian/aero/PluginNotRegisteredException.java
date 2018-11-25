package net.pravian.aero;

import org.bukkit.plugin.Plugin;

public class PluginNotRegisteredException extends AeroException
{

    private static final long serialVersionUID = 1L;

    public PluginNotRegisteredException(Plugin plugin)
    {
        super(plugin.getName() + " isn't registerd with " + Aero.NAME + "!");
    }

}

package net.pravian.aero;

import org.bukkit.plugin.Plugin;

public final class MetricsDisabledException extends AeroException {

    private static final long serialVersionUID = 1L;

    public MetricsDisabledException(Plugin plugin) {
        super("Metrics are disabled for " + plugin.getName());
    }
}

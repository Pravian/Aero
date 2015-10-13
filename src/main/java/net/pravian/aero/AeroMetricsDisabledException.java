package net.pravian.aero;

import org.bukkit.plugin.Plugin;

public final class AeroMetricsDisabledException extends AeroException {

    private static final long serialVersionUID = 16662341723L;

    public AeroMetricsDisabledException(Plugin plugin) {
        super("Metrics are disabled for " + plugin.getName());
    }
}

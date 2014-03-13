package net.pravian.bukkitlib;

public final class MetricsDisabledException extends IllegalStateException {

    private static final long serialVersionUID = 1L;

    public MetricsDisabledException() {
        super("Metrics are disabled in BukkitLib");
    }
}

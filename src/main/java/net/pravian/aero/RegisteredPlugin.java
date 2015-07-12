package net.pravian.aero;

import net.pravian.aero.plugin.AeroPlugin;

public class RegisteredPlugin {

    private final AeroPlugin<?> plugin;
    private final Aero aero;

    private boolean metrics = true;
    private boolean throwExceptions = false;

    protected RegisteredPlugin(AeroPlugin<?> plugin) {
        this.plugin = plugin;
        this.aero = plugin.getAero();
    }

    /**
     * Returns the plugin associated with this class.
     *
     * @return The plugin.
     */
    public final AeroPlugin<?> getPlugin() {
        return plugin;
    }

    /**
     * Explicitly disables metrics for this plugin.
     *
     * <p>
     * <b>Note</b>: Using this will make the author sad... ;(
     *
     * @return This options instance.
     * @see <a href="http://mcstats.org">http://mcstats.org</a>a
     */
    public RegisteredPlugin explicitDisableMetrics() {
        this.metrics = false;
        return this;
    }

    /**
     * Explicitly enables throwing exceptions.
     *
     * @return This options instance.
     */
    public RegisteredPlugin explicitEnableExceptionThrowing() {
        this.throwExceptions = true;
        return this;
    }

    /**
     * Returns true if metrics are enabled for this plugin.
     *
     * @return True if metrics are enabled.
     * @see <a href="http://mcstats.org">http://mcstats.org</a>
     */
    public boolean metricsEnabled() {
        return metrics;
    }

    /**
     * Returns true if exception throwing is enabled.
     *
     * @return True if exception throwing is enabled.
     */
    public boolean doesThrowExceptions() {
        return throwExceptions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof RegisteredPlugin)) {
            return false;
        }

        return obj.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.plugin != null ? this.plugin.hashCode() : 0);
        return hash;
    }

}

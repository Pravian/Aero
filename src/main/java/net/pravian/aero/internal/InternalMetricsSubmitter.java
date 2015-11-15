package net.pravian.aero.internal;

import java.io.IOException;
import net.pravian.aero.Aero;
import net.pravian.aero.RegisteredPlugin;
import net.pravian.aero.metrics.FixedDonutPlotter;
import net.pravian.aero.metrics.Graph;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

class InternalMetricsSubmitter {

    private final AeroContainer aeroPlugin;
    private BukkitTask submitTask;

    InternalMetricsSubmitter(AeroContainer aero) {
        this.aeroPlugin = aero;
    }

    public void submit() {
        if (submitTask != null) {
            return;
        }

        submitTask = Bukkit.getServer().getScheduler().runTaskLater(aeroPlugin, newTask(), 10 * 20L); // 10 sec
    }

    private Runnable newTask() {
        return new Runnable() {

            @Override
            public void run() {
                try {
                    final InternalMetrics metrics = new InternalMetrics(aeroPlugin, Aero.NAME, aeroPlugin.getBuildVersion());

                    final Graph version = metrics.createGraph("Version");
                    version.addPlotter(new FixedDonutPlotter(aeroPlugin.getBuildVersion(), aeroPlugin.getBuildNumber()));

                    final Graph plugins = metrics.createGraph("Plugins");
                    for (RegisteredPlugin plugin : aeroPlugin.getAero().getRegisteredPlugins()) {
                        plugins.addPlotter(new FixedDonutPlotter(plugin.getPlugin().getName(), plugin.getPlugin().getDescription().getVersion()));
                    }

                    metrics.start();
                } catch (IOException ex) {
                    aeroPlugin.getLogger().warning("Failed to submit metrics.");
                    if (Aero.getInstance().isDebugging()) {
                        aeroPlugin.getLogger().severe(ExceptionUtils.getFullStackTrace(ex));
                    }
                }
            }

        };
    }

}

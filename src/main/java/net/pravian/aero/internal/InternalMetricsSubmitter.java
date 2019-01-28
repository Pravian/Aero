package net.pravian.aero.internal;

import java.util.HashMap;
import java.util.Map;
import net.pravian.aero.plugin.AeroPlugin;
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

        submitTask = Bukkit.getServer().getScheduler()
            .runTaskLater(aeroPlugin, new Submitter(), 10 * 20L); // 10 sec
    }

    private class Submitter implements Runnable {

        @Override
        public void run() {
            final InternalMetrics metrics = new InternalMetrics(aeroPlugin);

            metrics.addCustomChart(new InternalMetrics.AdvancedPie("dependents", () ->
            {
                Map<String, Integer> data = new HashMap<>();

                for (AeroPlugin plugin : aeroPlugin.getAero().getRegisteredPlugins()) {
                    data.put(plugin.getPlugin().getName(), 1);
                }

                return data;
            }));
        }

    }
}

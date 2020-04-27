package net.pravian.aero.internal;

import java.util.HashMap;
import java.util.Map;
import net.pravian.aero.bstats.Metrics;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

class MetricsSubmitter {

    private final AeroBukkit plugin;
    private BukkitTask submitTask;

    MetricsSubmitter(AeroBukkit aero) {
        this.plugin = aero;
    }

    public void submit() {
        if (submitTask != null) {
            return;
        }

        submitTask = Bukkit.getServer().getScheduler().runTaskLater(plugin, new Submitter(), 10 * 20L); // 10 sec
    }

    private class Submitter implements Runnable {

        @Override
        public void run() {
            final Metrics metrics = new Metrics(plugin, 1264);

            metrics.addCustomChart(new Metrics.AdvancedPie("dependents", () -> {
                Map<String, Integer> data = new HashMap<>();

                for (AeroPlugin plugin : plugin.getAero().getRegisteredPlugins()) {
                    data.put(plugin.getPlugin().getName(), 1);
                }

                return data;
            }));
        }

    }

}

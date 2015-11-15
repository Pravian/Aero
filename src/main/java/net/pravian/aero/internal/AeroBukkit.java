package net.pravian.aero.internal;

import java.io.InputStream;
import java.util.Properties;
import net.pravian.aero.Aero;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class AeroBukkit extends JavaPlugin implements AeroContainer {

    private final AeroBukkit plugin;
    private final Aero aero;
    //
    private String buildVersion;
    private String buildNumber;
    private String buildDate;

    @SuppressWarnings("deprecation")
    public AeroBukkit() {
        this.plugin = this;
        this.aero = new Aero(plugin);
    }

    @Override
    public void onLoad() {
        Bukkit.getServicesManager().register(AeroContainer.class, this, plugin, ServicePriority.Normal);
        loadBuildInformation();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onEnable() {
        aero.init();
        new InternalMetricsSubmitter(plugin).submit();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onDisable() {
        this.aero.deinit();
    }

    @Override
    public Aero getAero() {
        return aero;
    }

    @Override
    public String getBuildVersion() {
        return buildVersion;
    }

    @Override
    public String getBuildNumber() {
        return buildNumber;
    }

    @Override
    public String getBuildDate() {
        return buildDate;
    }

    private void loadBuildInformation() {
        // Plugin build-number and build-date
        try {
            final InputStream in = Aero.class
                    .getResourceAsStream("/" + Aero.class
                            .getPackage().getName().replace('.', '/') + "/build.properties");
            final Properties build = new Properties();

            build.load(in);

            in.close();
            buildVersion = build.getProperty("app.buildversion", "");
            buildNumber = build.getProperty("app.buildnumber", "");
            buildDate = build.getProperty("app.builddate", "");
        } catch (Exception ex) {
            if (aero.isDebugging()) {
                Bukkit.getLogger().severe("[" + Aero.NAME + "] Could not load build information!");
                Bukkit.getLogger().severe("[" + Aero.NAME + "] " + ExceptionUtils.getFullStackTrace(ex));
            }
        }
    }

}

package net.pravian.aero.internal;

import net.pravian.aero.Aero;
import org.bukkit.plugin.Plugin;

public interface AeroContainer extends Plugin {

  public Aero getAero();

  public String getBuildVersion();

  public String getBuildNumber();

  public String getBuildDate();
}

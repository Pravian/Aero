package net.pravian.aero.internal;

import java.util.logging.Logger;
import net.pravian.aero.Aero;

public interface AeroContainer {

    public Aero getAero();

    public Logger getLogger();

    public String getBuildVersion();

    public String getBuildNumber();

    public String getBuildDate();

}

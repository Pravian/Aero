package net.pravian.bukkitlib.metrics;

public abstract class DonutPlotter extends Plotter {

    private String majorName, minorName;

    public DonutPlotter(String major, String minor) {
        super(major + "~=~" + minor);
    }
}

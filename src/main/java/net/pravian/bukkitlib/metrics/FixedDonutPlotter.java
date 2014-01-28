package net.pravian.bukkitlib.metrics;

public class FixedDonutPlotter extends DonutPlotter {

    public final int value;

    public FixedDonutPlotter(final String major, final String minor) {
        this(major, minor, 1);
    }

    public FixedDonutPlotter(final String major, final String minor, final int value) {
        super(major, minor);
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}

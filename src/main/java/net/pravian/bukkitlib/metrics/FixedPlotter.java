package net.pravian.bukkitlib.metrics;

public class FixedPlotter extends Plotter {

    private final int value;

    public FixedPlotter(final String name) {
        this(name, 1);
    }

    public FixedPlotter(final String name, final int value) {
        super(name);
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
package net.pravian.bukkitlib.metrics;

public class IncrementalPlotter extends Plotter {

    private final int value;

    public IncrementalPlotter(final String name) {
        this(name, 1);
    }

    public IncrementalPlotter(final String name, final int value) {
        super(name);
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
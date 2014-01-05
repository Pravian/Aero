package net.pravian.bukkitlib.internal;

import net.pravian.bukkitlib.internal.InternalMetrics.Plotter;

public final class InternalSimplePlotter extends Plotter {

    public InternalSimplePlotter(final String name) {
        super(name);
    }

    @Override
    public int getValue() {
        return 1;
    }
}

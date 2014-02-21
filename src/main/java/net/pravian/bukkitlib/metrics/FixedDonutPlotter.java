package net.pravian.bukkitlib.metrics;

/**
 * Represents a plotter with special data for a donut-type graph and a fixed value.
 */
public class FixedDonutPlotter extends DonutPlotter {

    public final int value;

    /**
     * Creates a new DonutPlotter instance with the specified major and minor names and a value of 1.
     *
     * <p>The major name indicates the center area of the donut graph whilst the minor name indicates the edges.</p>
     *
     * @param major The donut filling (usually air).
     * @param minor The donut crust (usually dough with icing).
     */
    public FixedDonutPlotter(final String major, final String minor) {
        this(major, minor, 1);
    }

    /**
     * Creates a new DonutPlotter instance with the specified value and major and minor names.
     *
     * <p>The major name indicates the center area of the donut graph whilst the minor name indicates the edges.</p>
     *
     * @param major The donut filling (usually air).
     * @param minor The donut crust (usually dough with icing).
     */
    public FixedDonutPlotter(final String major, final String minor, final int value) {
        super(major, minor);
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}

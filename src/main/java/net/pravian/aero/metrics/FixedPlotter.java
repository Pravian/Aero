package net.pravian.aero.metrics;

/**
 * Represents a plotter with a fixed value.
 */
public class FixedPlotter extends Plotter {

  private final int value;

  /**
   * Creates a new FixedPlotter instance with a value of 1.
   *
   * @param name The plotter name.
   */
  public FixedPlotter(final String name) {
    this(name, 1);
  }

  /**
   * Creates a new FixedPlotter instance with the specified value.
   *
   * @param name The plotter name.
   * @param value The plotter value.
   */
  public FixedPlotter(final String name, final int value) {
    super(name);
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }
}

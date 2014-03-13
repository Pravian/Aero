package net.pravian.bukkitlib;

public final class BukkitLibNotInitializedException extends IllegalStateException {

    private static final long serialVersionUID = 1L;

    public BukkitLibNotInitializedException() {
        super("BukkitLib was not initialized properly!");
    }
}

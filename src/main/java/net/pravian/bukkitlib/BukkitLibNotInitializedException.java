package net.pravian.bukkitlib;

public class BukkitLibNotInitializedException extends IllegalStateException {

    public BukkitLibNotInitializedException() {
        super("BukkitLib was not initialized properly!");
    }
}

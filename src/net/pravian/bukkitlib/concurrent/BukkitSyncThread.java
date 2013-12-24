package net.pravian.bukkitlib.concurrent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Represents a synchronous thread.
 */
public abstract class BukkitSyncThread implements Runnable {

    private final Plugin plugin;
    private int taskId;
    private boolean running = false;

    /**
     * Creates a new BukkitSyncThread with the specified plugin.
     *
     * @param plugin The plugin to create the thread with
     */
    public BukkitSyncThread(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Starts the thread.
     */
    public synchronized void start() {
        if (isStarted()) {
            throw new IllegalStateException("Task already started!");
        }

        running = true;
        taskId = Bukkit.getScheduler().runTask(plugin, this).getTaskId();
    }

    /**
     * Starts the thread after a certain amount of ticks.
     *
     * @param delay The starting delay
     */
    public synchronized void start(long delay) {
        if (isStarted()) {
            throw new IllegalStateException("Task already started!");
        }

        running = true;
        taskId = Bukkit.getScheduler().runTaskLater(plugin, this, delay).getTaskId();
    }

    /**
     * Starts the thread after a certain amount of ticks and schedules the thread for repetition.
     *
     * @param delay The starting delay
     * @param interval The interval between every thread cycle
     */
    public synchronized void start(long delay, long interval) {
        if (isStarted()) {
            throw new IllegalStateException("Task already started!");
        }

        running = true;
        taskId = plugin.getServer().getScheduler().runTaskTimer(plugin, this, delay, interval).getTaskId();
    }

    /**
     * The plugin calling this thread
     *
     * @return The plugin
     */
    public synchronized Plugin getOwner() {
        return plugin;
    }

    /**
     * The Task-ID assigned to this thread.
     *
     * <p>Returns -1 if the thread hasn't been started yet</p>
     *
     * @return The Task-ID
     */
    public synchronized int getTaskId() {
        return taskId;
    }

    /**
     * Verifies if the thread is running.
     *
     * @return true if the thread is running.
     */
    public synchronized boolean isStarted() {
        return running;
    }

    /**
     * Stops the thread.
     */
    public synchronized void stop() {
        if (!isStarted()) {
            throw new IllegalStateException("Task not started!");
        }

        running = false;
        Bukkit.getScheduler().cancelTask(taskId);
    }

    /**
     * Validates if the thread is in queue.
     *
     * @return true if the thread is queued
     */
    public synchronized boolean isQueued() {
        return Bukkit.getServer().getScheduler().isQueued(taskId);
    }

    /**
     * Locks the current thread until the thread is finished.
     */
    public void waitFinished() {
        while (isStarted()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
            }
        }
    }
}

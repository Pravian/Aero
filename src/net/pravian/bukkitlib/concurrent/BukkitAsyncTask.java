package net.pravian.bukkitlib.concurrent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Represents a synchronous thread.
 */
public abstract class BukkitAsyncTask implements Runnable, Cloneable {

    private final Plugin plugin;
    private int taskId;

    /**
     * Creates a new BukkitSyncTask with the specified plugin.
     *
     * @param plugin The plugin to create the thread with
     */
    public BukkitAsyncTask(Plugin plugin) {
        this.plugin = plugin;
        this.taskId = -1;
    }

    /**
     * Starts the thread.
     *
     * <p><b>Warning</b>: A task may only be scheduled once at a time. If you need two duplicate threads, use clone()</p>
     *
     */
    public synchronized boolean start() {
        if (isRunning()) {
            return false;
        }

        taskId = Bukkit.getScheduler().runTaskAsynchronously(plugin, this).getTaskId();
        return true;
    }

    /**
     * Starts the thread after a certain amount of ticks.
     *
     * <p><b>Warning</b>: A task may only be scheduled once at a time. If you need two duplicate threads, use clone()</p>
     *
     * @param delay The starting delay
     */
    public synchronized boolean start(long delay) {
        if (isRunning()) {
            return false;
        }

        taskId = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, delay).getTaskId();
        return true;
    }

    /**
     * Starts the thread after a certain amount of ticks and schedules the thread for repetition.
     *
     * <p><b>Warning</b>: A task may only be scheduled once at a time. If you need two duplicate threads, use clone()</p>
     *
     * @param delay The starting delay
     * @param interval The interval between every thread cycle
     * @return true if the thread was started successfully
     */
    public synchronized boolean start(long delay, long interval) {
        if (isRunning()) {
            return false;
        }

        taskId = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, delay, interval).getTaskId();
        return true;
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
    public synchronized boolean isRunning() {
        return taskId != -1 && Bukkit.getScheduler().isCurrentlyRunning(taskId);
    }

    /**
     * Stops the thread.
     */
    public synchronized boolean stop() {
        if (!isRunning()) {
            return false;
        }

        Bukkit.getScheduler().cancelTask(taskId);
        return true;
    }

    /**
     * Validates if the thread is in queue.
     *
     * @return true if the thread is queued
     */
    public synchronized boolean isQueued() {
        return Bukkit.getScheduler().isQueued(taskId);
    }

    /**
     * Locks the current thread until the thread is finished.
     */
    public void waitFinished() {
        while (isRunning()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public synchronized BukkitAsyncTask clone() {
        try {
            final BukkitAsyncTask task = (BukkitAsyncTask) super.clone();
            task.taskId = -1;
            return task;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}

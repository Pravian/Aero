package net.pravian.aero.concurrent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class BukkitTask implements Runnable, Cloneable
{

    protected final Plugin plugin;
    protected int taskId;

    public BukkitTask(Plugin plugin)
    {
        this.plugin = plugin;
        this.taskId = -1;
    }

    /**
     * Starts the thread after a certain amount of ticks.
     * <p>
     * <p>
     * <b>Warning</b>: A task may only be scheduled once at a time. If you need two duplicate threads, use clone()</p>
     *
     * @param delay The starting delay
     */
    public synchronized boolean start(long delay)
    {
        if (isRunning())
        {
            return false;
        }

        taskId = startTask(delay);
        return true;
    }

    /**
     * Starts the thread after a certain amount of ticks and schedules the thread for repetition.
     * <p>
     * <p>
     * <b>Warning</b>: A task may only be scheduled once at a time. If you need two duplicate threads, use clone()</p>
     *
     * @param delay    The starting delay
     * @param interval The interval between every thread cycle
     * @return true if the thread was started successfully
     */
    public synchronized boolean start(long delay, long interval)
    {
        if (isRunning())
        {
            return false;
        }

        taskId = startTask(delay, interval);
        return true;
    }

    /**
     * Starts the task.
     * <p>
     * <p>
     * <b>Warning</b>: A task may only be scheduled once at a time. If you need two duplicate threads, use clone()</p>
     */
    public synchronized boolean start()
    {
        if (isRunning())
        {
            return false;
        }

        taskId = startTask();
        return true;
    }

    /**
     * Stops the task.
     */
    public synchronized boolean stop()
    {
        if (!isRunning())
        {
            return false;
        }

        Bukkit.getScheduler().cancelTask(taskId);
        return true;
    }

    /**
     * The Task-ID assigned to this thread.
     * <p>
     * <p>
     * Returns -1 if the thread hasn't been started yet</p>
     *
     * @return The Task-ID
     */
    public synchronized int getTaskId()
    {
        return taskId;
    }

    /**
     * The plugin calling this thread
     *
     * @return The plugin
     */
    public synchronized Plugin getOwner()
    {
        return plugin;
    }

    /**
     * Verifies if the thread is running.
     *
     * @return true if the thread is running.
     */
    public synchronized boolean isRunning()
    {
        return taskId != -1 && Bukkit.getScheduler().isCurrentlyRunning(taskId);
    }

    /**
     * Validates if the thread is in queue.
     *
     * @return true if the thread is queued
     */
    public synchronized boolean isQueued()
    {
        return Bukkit.getScheduler().isQueued(taskId);
    }

    /**
     * Locks the current thread until the thread is finished.
     */
    public void waitFinished()
    {
        while (isRunning())
        {
            try
            {
                Thread.sleep(20);
            }
            catch (InterruptedException ex)
            {
            }
        }
    }

    protected abstract int startTask();

    protected abstract int startTask(long delay);

    protected abstract int startTask(long delay, long interval);
}

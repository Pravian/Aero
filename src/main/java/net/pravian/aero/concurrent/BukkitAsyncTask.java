package net.pravian.aero.concurrent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Represents a synchronous thread.
 */
public abstract class BukkitAsyncTask extends BukkitTask
{

    public BukkitAsyncTask(Plugin plugin)
    {
        super(plugin);
    }

    @Override
    public int startTask()
    {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, this).getTaskId();
    }

    @Override
    public int startTask(long delay)
    {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, delay).getTaskId();
    }

    @Override
    public int startTask(long delay, long interval)
    {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, delay, interval).getTaskId();
    }

    @Override
    public synchronized BukkitAsyncTask clone()
    {
        try
        {
            final BukkitAsyncTask task = (BukkitAsyncTask) super.clone();
            task.taskId = -1;
            return task;
        }
        catch (CloneNotSupportedException ex)
        {
            return null;
        }
    }
}

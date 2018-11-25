package net.pravian.aero.concurrent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Represents a synchronous thread.
 */
public abstract class BukkitSyncTask extends BukkitTask
{

    public BukkitSyncTask(Plugin plugin)
    {
        super(plugin);
    }

    @Override
    public int startTask()
    {
        return Bukkit.getScheduler().runTask(plugin, this).getTaskId();
    }

    @Override
    public int startTask(long delay)
    {
        return Bukkit.getScheduler().runTaskLater(plugin, this, delay).getTaskId();
    }

    @Override
    public int startTask(long delay, long interval)
    {
        return Bukkit.getScheduler().runTaskTimer(plugin, this, delay, interval).getTaskId();
    }

    @Override
    public synchronized BukkitSyncTask clone()
    {
        try
        {
            final BukkitSyncTask task = (BukkitSyncTask) super.clone();
            task.taskId = -1;
            return task;
        }
        catch (CloneNotSupportedException ex)
        {
            return null;
        }
    }
}

/*
 * Copyright 2015 Jerom van der Sar.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.pravian.aero.component.service;

import net.pravian.aero.component.PluginListener;
import net.pravian.aero.plugin.AeroPlugin;

public abstract class AbstractService<T extends AeroPlugin<T>> extends PluginListener<T> implements Service
{

    private final String serviceId;
    //
    private boolean started = false;

    public AbstractService(T plugin)
    {
        this(plugin, null);
    }

    public AbstractService(T plugin, String id)
    {
        super(plugin);
        this.serviceId = id == null ? getClass().getSimpleName() : id;
    }

    @Override
    public void start()
    {
        if (started)
        {
            plugin.handleException("Tried to start service '" + serviceId + "' whilst already started!");
            return;
        }
        started = true;

        try
        {
            onStart();
        }
        catch (Exception ex)
        {
            plugin.handleException("Unhandled exception whilst starting service '" + serviceId + "'!", ex);
        }
        register();
    }

    @Override
    public void stop()
    {
        if (!started)
        {
            plugin.handleException("Tried to stop service '" + serviceId + "' whilst already stopped!");
            return;
        }
        started = false;
        unregister();
        try
        {
            onStop();
        }
        catch (Exception ex)
        {
            plugin.handleException("Unhandled exception whilst stopping service '" + serviceId + "'!", ex);
        }
    }

    @Override
    public boolean isStarted()
    {
        return started;
    }

    @Override
    public String getServiceId()
    {
        return serviceId;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 61 * hash + (this.serviceId != null ? this.serviceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final AbstractService<?> other = (AbstractService<?>) obj;
        return !((this.serviceId == null) ? (other.serviceId != null) : !this.serviceId.equals(other.serviceId));
    }

    protected abstract void onStart();

    protected abstract void onStop();
}

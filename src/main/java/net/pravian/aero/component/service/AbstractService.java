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

public abstract class AbstractService<T extends AeroPlugin<T>> extends PluginListener<T> implements Service {

    protected final String id;
    //
    protected boolean started;

    protected AbstractService(T plugin, String id) {
        super(plugin);
        this.id = id;
        //
        this.started = false;
    }

    @Override
    public void start() {
        if (started) {
            plugin.handleException("Tried to start service '" + id + "' whilst already started!");
            return;
        }
        started = true;

        try {
            onStart();
        } catch (Exception ex) {
            plugin.handleException("Unhandled exception whilst starting service '" + id + "'!", ex);
        }
        register();
    }

    @Override
    public void stop() {
        if (!started) {
            plugin.handleException("Tried to stop service '" + id + "' whilst already started!");
            return;
        }
        started = false;
        try {
            onStop();
        } catch (Exception ex) {
            plugin.handleException("Unhandled exception whilst stopping service '" + id + "'!", ex);
        }
        unregister();
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    protected abstract void onStart();

    protected abstract void onStop();
}

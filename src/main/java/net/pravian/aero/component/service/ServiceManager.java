package net.pravian.aero.component.service;

import com.google.common.collect.Lists;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.util.List;

public class ServiceManager<T extends AeroPlugin<T>> extends AbstractService<T>
{

    private final List<Service> services = Lists.newArrayList();

    public ServiceManager(T plugin)
    {
        super(plugin);
    }

    @Override
    protected void onStart()
    {
        for (Service service : services)
        {
            try
            {
                service.start();
            }
            catch (Exception ex)
            {
                plugin.handleException("Unhandled exception whilst starting service '" + service.getServiceId() + "'!", ex);
            }
        }
    }

    @Override
    protected void onStop()
    {
        for (Service service : Lists.reverse(services))
        {
            try
            {
                service.stop();
            }
            catch (Exception ex)
            {
                plugin.handleException("Unhandled exception whilst stopping service '" + service.getServiceId() + "'!", ex);
            }
        }
    }

    public boolean registerService(Service service)
    {
        if (isStarted())
        {
            logger.warning("Registering service: " + service.getServiceId() + " whilst services are already started!");
        }

        if (!services.add(service))
        {
            logger.warning("Could not register service: " + service.getServiceId() + ". Service already registered!");
            return false;
        }

        return true;
    }

    public <S extends Service> S registerService(Class<S> serviceClass)
    {

        S service = null;
        try
        {
            for (Constructor<?> cons : serviceClass.getConstructors())
            {
                Class<?>[] args = cons.getParameterTypes();

                if (args.length == 2
                        && Plugin.class.isAssignableFrom(args[0])
                        && String.class.equals(args[1]))
                {
                    service = serviceClass.cast(cons.newInstance(plugin, serviceClass.getSimpleName()));
                    break;
                }

                if (args.length == 1
                        && Plugin.class.isAssignableFrom(args[0]))
                {
                    service = serviceClass.cast(cons.newInstance(plugin));
                    break;
                }
            }
        }
        catch (Exception ex)
        {
            logger.severe("Could not register service class: " + serviceClass.getSimpleName());
            logger.severe(ex);
            return null;
        }
        catch (NoClassDefFoundError ex)
        {
            logger.severe("Could not register service class: " + serviceClass.getSimpleName());
            logger.severe(ex);
            return null;
        }

        if (service == null)
        {
            logger.severe("Could not register service class: " + serviceClass.getSimpleName() + ". No matching constructor found!");
            return null;
        }

        return registerService(service) ? service : null;
    }

    public <T> T getService(Class<T> wantedClass)
    {
        for (Service service : services)
        {
            if (wantedClass.isAssignableFrom(service.getClass()))
            {
                return wantedClass.cast(service);
            }
        }
        return null;
    }
}

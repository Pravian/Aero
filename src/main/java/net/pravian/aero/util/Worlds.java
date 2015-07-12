package net.pravian.aero.util;

import org.bukkit.World;

/**
 * Represents all World-related utilities.
 */
public class Worlds {

    private Worlds() {
    }

    /**
     * Sets the world time in ticks.
     *
     * @param world The world to set the time on.
     * @param ticks The amount of ticks to set the time to.
     */
    public static void setTime(World world, long ticks) {
        long time = world.getTime();
        time -= time % 24000;
        world.setTime(time + 24000 + ticks);
    }

    /**
     * Sets the world weather.
     *
     * @param world The world to set the weather on.
     * @param type The weather the world should have.
     */
    public static void setWeather(World world, WeatherType type) {
        switch (type) {
            case SUN: {
                world.setThundering(false);
                world.setStorm(false);
                break;
            }
            case RAIN: {
                world.setStorm(true);
                break;
            }
            case STORM: {
                world.setThundering(true);
                break;
            }
        }
    }

    /**
     * Represents different types of weather.
     */
    public static enum WeatherType {

        SUN,
        RAIN,
        STORM;
    }

}

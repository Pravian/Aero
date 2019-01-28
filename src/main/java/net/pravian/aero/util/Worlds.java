package net.pravian.aero.util;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * Represents all World-related utilities.
 */
public class Worlds {

  private Worlds() {
  }

  public static World getWorld(String name) {
    name = name.toLowerCase();
    World world = Bukkit.getWorld(name);

    if (world == null) {
      try {
        UUID uuid = UUID.fromString(name);
        world = Bukkit.getWorld(uuid);
      } catch (Exception ignored) {
      }
    }

    if (world == null) {
      for (World loopWorld : Bukkit.getWorlds()) {
        if (loopWorld.getName().toLowerCase().equalsIgnoreCase(name)) {
          world = loopWorld;
          break;
        }
      }
    }

    if (world == null) {
      for (World loopWorld : Bukkit.getWorlds()) {
        if (loopWorld.getName().toLowerCase().startsWith(name)) {
          world = loopWorld;
          break;
        }
      }
    }

    return world;
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
  public enum WeatherType {

    SUN,
    RAIN,
    STORM;
  }

}

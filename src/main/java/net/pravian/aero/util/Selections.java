package net.pravian.aero.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;

public class Selections {

  private Selections() {
  }

  /**
   * Returns a list of all the blocks in a circle within a certain radius of a location.
   * <p>
   * <p>
   * Author: ArthurMaker</p>
   *
   * @param centerLoc center Location
   * @param radius radius of the circle
   * @return list of blocks
   */
  public static List<Location> getAllBlocksInCircle(Location centerLoc, int radius) {
    final List<Location> circle = new ArrayList<Location>();
    final World world = centerLoc.getWorld();
    int x = 0;
    int z = radius;
    int error;
    int d = 2 - 2 * radius;
    while (z >= 0) {
      circle.add(new Location(world, centerLoc.getBlockX() + x, centerLoc.getY(),
          centerLoc.getBlockZ() + z));
      circle.add(new Location(world, centerLoc.getBlockX() - x, centerLoc.getY(),
          centerLoc.getBlockZ() + z));
      circle.add(new Location(world, centerLoc.getBlockX() - x, centerLoc.getY(),
          centerLoc.getBlockZ() - z));
      circle.add(new Location(world, centerLoc.getBlockX() + x, centerLoc.getY(),
          centerLoc.getBlockZ() - z));
      error = 2 * (d + z) - 1;
      if ((d < 0) && (error <= 0)) {
        x++;
        d += 2 * x + 1;
      } else {
        error = 2 * (d - x) - 1;
        if ((d > 0) && (error > 0)) {
          z--;
          d += 1 - 2 * z;
        } else {
          x++;
          d += 2 * (x - z);
          z--;
        }
      }
    }
    return circle;
  }

  /**
   * Returns a list of all the blocks in a cuboid.
   * <p>
   * <p>
   * Authors: ArthurMaker, CaptainBern</p>
   *
   * @param position1 first position
   * @param position2 second position
   * @return list of blocks
   */
  public static List<Location> getAllBlocksInCuboid(Location position1, Location position2) {

    if (!position1.getWorld().getName().equals(position2.getWorld().getName())) {
      return null;
    }

    final List<Location> cube = new ArrayList<Location>();

    final int minX = (int) Math.min(position1.getX(), position2.getX());
    final int maxX = (int) Math.max(position1.getX(), position2.getX());
    final int minY = (int) Math.min(position1.getY(), position2.getY());
    final int maxY = (int) Math.max(position1.getY(), position2.getY());
    final int minZ = (int) Math.min(position1.getZ(), position2.getZ());
    final int maxZ = (int) Math.max(position1.getZ(), position2.getZ());

    for (int x = minX; x <= maxX; x++) {
      for (int y = minY; y <= maxY; y++) {
        for (int z = minZ; z <= maxZ; z++) {
          cube.add(new Location(position1.getWorld(), x, y, z));
        }
      }
    }
    return cube;
  }

  /**
   * Returns a list of all the blocks in a diagonal line between two locations.
   * <p>
   * <p>
   * ArthurMaker</p>
   *
   * @param position1 first position
   * @param position2 second position
   * @return list of blocks
   */
  public static List<Location> getLine(Location position1, Location position2) {
    List<Location> line = new ArrayList<Location>();
    int dx = Math.max(position1.getBlockX(), position2.getBlockX()) - Math
        .min(position1.getBlockX(), position2.getBlockX());
    int dy = Math.max(position1.getBlockY(), position2.getBlockY()) - Math
        .min(position1.getBlockY(), position2.getBlockY());
    int dz = Math.max(position1.getBlockZ(), position2.getBlockZ()) - Math
        .min(position1.getBlockZ(), position2.getBlockZ());
    int x1 = position1.getBlockX();
    int x2 = position2.getBlockX();
    int y1 = position1.getBlockY();
    int y2 = position2.getBlockY();
    int z1 = position1.getBlockZ();
    int z2 = position2.getBlockZ();
    int x;
    int y;
    int z;
    int i;
    int d;
    switch (getHighest(dx, dy, dz)) {
      case 1:
        i = 0;
        d = 1;
        if (x1 > x2) {
          d = -1;
        }
        x = position1.getBlockX();
        do {
          i++;
          y = y1 + (x - x1) * (y2 - y1) / (x2 - x1);
          z = z1 + (x - x1) * (z2 - z1) / (x2 - x1);
          line.add(new Location(position1.getWorld(), x, y, z));
          x += d;
        }
        while (i <= Math.max(x1, x2) - Math.min(x1, x2));
        break;
      case 2:
        i = 0;
        d = 1;
        if (y1 > y2) {
          d = -1;
        }
        y = position1.getBlockY();
        do {
          i++;
          x = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
          z = z1 + (y - y1) * (z2 - z1) / (y2 - y1);
          line.add(new Location(position1.getWorld(), x, y, z));
          y += d;
        }
        while (i <= Math.max(y1, y2) - Math.min(y1, y2));
        break;
      case 3:
        i = 0;
        d = 1;
        if (z1 > z2) {
          d = -1;
        }
        z = position1.getBlockZ();
        do {
          i++;
          y = y1 + (z - z1) * (y2 - y1) / (z2 - z1);
          x = x1 + (z - z1) * (x2 - x1) / (z2 - z1);
          line.add(new Location(position1.getWorld(), x, y, z));
          z += d;
        }
        while (i <= Math.max(z1, z2) - Math.min(z1, z2));
    }

    return line;
  }

  // Support
  private static int getHighest(int x, int y, int z) {
    if ((x >= y) && (x >= z)) {
      return 1;
    }
    if ((y >= x) && (y >= z)) {
      return 2;
    }
    return 3;
  }
}

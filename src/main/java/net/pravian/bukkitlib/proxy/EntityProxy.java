package net.pravian.bukkitlib.proxy;

import java.util.List;
import java.util.UUID;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class EntityProxy implements Entity {

    private final Entity entity;

    public EntityProxy(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Location getLocation() {
        return entity.getLocation();
    }

    @Override
    public Location getLocation(Location lctn) {
        return entity.getLocation(lctn);
    }

    @Override
    public void setVelocity(Vector vector) {
        entity.setVelocity(vector);
    }

    @Override
    public Vector getVelocity() {
        return entity.getVelocity();
    }

    @Override
    public boolean isOnGround() {
        return entity.isOnGround();
    }

    @Override
    public World getWorld() {
        return entity.getWorld();
    }

    @Override
    public boolean teleport(Location lctn) {
        return entity.teleport(lctn);
    }

    @Override
    public boolean teleport(Location lctn, PlayerTeleportEvent.TeleportCause tc) {
        return entity.teleport(lctn, tc);
    }

    @Override
    public boolean teleport(Entity entity) {
        return this.entity.teleport(entity);
    }

    @Override
    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause tc) {
        return this.entity.teleport(entity, tc);
    }

    @Override
    public List<Entity> getNearbyEntities(double d, double d1, double d2) {
        return entity.getNearbyEntities(d, d1, d2);
    }

    @Override
    public int getEntityId() {
        return entity.getEntityId();

    }

    @Override
    public int getFireTicks() {
        return entity.getFireTicks();
    }

    @Override
    public int getMaxFireTicks() {
        return entity.getMaxFireTicks();
    }

    @Override
    public void setFireTicks(int i) {
        entity.setFireTicks(i);
    }

    @Override
    public void remove() {
        entity.remove();
    }

    @Override
    public boolean isDead() {
        return entity.isDead();
    }

    @Override
    public boolean isValid() {
        return entity.isValid();
    }

    @Override
    public Server getServer() {
        return entity.getServer();
    }

    @Override
    public Entity getPassenger() {
        return entity.getPassenger();
    }

    @Override
    public boolean setPassenger(Entity entity) {
        return this.entity.setPassenger(entity);
    }

    @Override
    public boolean isEmpty() {
        return entity.isEmpty();
    }

    @Override
    public boolean eject() {
        return entity.eject();
    }

    @Override
    public float getFallDistance() {
        return entity.getFallDistance();
    }

    @Override
    public void setFallDistance(float f) {
        entity.setFallDistance(f);
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent ede) {
        entity.setLastDamageCause(ede);
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return entity.getLastDamageCause();
    }

    @Override
    public UUID getUniqueId() {
        return entity.getUniqueId();
    }

    @Override
    public int getTicksLived() {
        return entity.getTicksLived();
    }

    @Override
    public void setTicksLived(int i) {
        entity.setTicksLived(i);
    }

    @Override
    public void playEffect(EntityEffect ee) {
        entity.playEffect(ee);
    }

    @Override
    public EntityType getType() {
        return entity.getType();
    }

    @Override
    public boolean isInsideVehicle() {
        return entity.isInsideVehicle();
    }

    @Override
    public boolean leaveVehicle() {
        return entity.leaveVehicle();
    }

    @Override
    public Entity getVehicle() {
        return entity.getVehicle();
    }

    @Override
    public void setMetadata(String string, MetadataValue mv) {
        entity.setMetadata(string, mv);
    }

    @Override
    public List<MetadataValue> getMetadata(String string) {
        return entity.getMetadata(string);
    }

    @Override
    public boolean hasMetadata(String string) {
        return entity.hasMetadata(string);
    }

    @Override
    public void removeMetadata(String string, Plugin plugin) {
        entity.removeMetadata(string, plugin);
    }
}

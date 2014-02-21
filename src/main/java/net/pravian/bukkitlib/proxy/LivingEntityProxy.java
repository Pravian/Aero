package net.pravian.bukkitlib.proxy;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LivingEntityProxy extends EntityProxy implements LivingEntity {

    private final LivingEntity entity;

    public LivingEntityProxy(LivingEntity entity) {
        super(entity);

        this.entity = entity;
    }

    @Override
    public double getEyeHeight() {
        return entity.getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean bln) {
        return entity.getEyeHeight(bln);
    }

    @Override
    public Location getEyeLocation() {
        return entity.getEyeLocation();
    }

    @Override
    public List<Block> getLineOfSight(HashSet<Byte> hs, int i) {
        return entity.getLineOfSight(hs, i);
    }

    @Override
    public Block getTargetBlock(HashSet<Byte> hs, int i) {
        return entity.getTargetBlock(hs, i);
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> hs, int i) {
        return entity.getLastTwoTargetBlocks(hs, i);
    }

    @Override
    public Egg throwEgg() {
        return entity.throwEgg();
    }

    @Override
    public Snowball throwSnowball() {
        return entity.throwSnowball();
    }

    @Override
    public Arrow shootArrow() {
        return entity.shootArrow();
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> type) {
        return entity.launchProjectile(type);
    }

    @Override
    public int getRemainingAir() {
        return entity.getRemainingAir();
    }

    @Override
    public void setRemainingAir(int i) {
        entity.setRemainingAir(i);
    }

    @Override
    public int getMaximumAir() {
        return entity.getMaximumAir();
    }

    @Override
    public void setMaximumAir(int i) {
        entity.setMaximumAir(i);
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return entity.getMaximumNoDamageTicks();
    }

    @Override
    public void setMaximumNoDamageTicks(int i) {
        entity.setMaximumNoDamageTicks(i);
    }

    @Override
    public double getLastDamage() {
        return entity.getLastDamage();
    }

    @Override
    public void setLastDamage(double d) {
        entity.setLastDamage(d);
    }

    @Override
    public int getNoDamageTicks() {
        return entity.getNoDamageTicks();
    }

    @Override
    public void setNoDamageTicks(int i) {
        entity.setNoDamageTicks(i);
    }

    @Override
    public Player getKiller() {
        return entity.getKiller();
    }

    @Override
    public boolean addPotionEffect(PotionEffect pe) {
        return entity.addPotionEffect(pe);
    }

    @Override
    public boolean addPotionEffect(PotionEffect pe, boolean bln) {
        return entity.addPotionEffect(pe, bln);
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> clctn) {
        return entity.addPotionEffects(clctn);
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType pet) {
        return entity.hasPotionEffect(pet);
    }

    @Override
    public void removePotionEffect(PotionEffectType pet) {
        entity.removePotionEffect(pet);
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return entity.getActivePotionEffects();
    }

    @Override
    public boolean hasLineOfSight(Entity entity) {
        return this.entity.hasLineOfSight(entity);
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return entity.getRemoveWhenFarAway();
    }

    @Override
    public void setRemoveWhenFarAway(boolean bln) {
        entity.setRemoveWhenFarAway(bln);
    }

    @Override
    public EntityEquipment getEquipment() {
        return entity.getEquipment();
    }

    @Override
    public void setCanPickupItems(boolean bln) {
        entity.setCanPickupItems(bln);
    }

    @Override
    public boolean getCanPickupItems() {
        return entity.getCanPickupItems();
    }

    @Override
    public void setCustomName(String string) {
        entity.setCustomName(string);
    }

    @Override
    public String getCustomName() {
        return entity.getCustomName();
    }

    @Override
    public void setCustomNameVisible(boolean bln) {
        entity.setCustomNameVisible(bln);
    }

    @Override
    public boolean isCustomNameVisible() {
        return entity.isCustomNameVisible();
    }

    @Override
    public boolean isLeashed() {
        return entity.isLeashed();
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        return entity.getLeashHolder();
    }

    @Override
    public boolean setLeashHolder(Entity entity) {
        return this.entity.setLeashHolder(entity);
    }

    @Override
    public void damage(double d) {
        entity.damage(d);
    }

    @Override
    public void damage(double d, Entity entity) {
        this.damage(d, entity);
    }

    @Override
    public void setHealth(double d) {
        entity.setHealth(d);
    }

    @Override
    public void setMaxHealth(double d) {
        entity.setMaxHealth(d);
    }

    @Override
    public void resetMaxHealth() {
        entity.resetMaxHealth();
    }

    @Override
    public int _INVALID_getLastDamage() {
        return entity._INVALID_getLastDamage();
    }

    @Override
    public void _INVALID_setLastDamage(int i) {
        entity._INVALID_damage(i, entity);
    }

    @Override
    public void _INVALID_damage(int i) {
        entity._INVALID_damage(i);
    }

    @Override
    public void _INVALID_damage(int i, Entity entity) {
        this.entity._INVALID_damage(i, entity);
    }

    @Override
    public double getHealth() {
        return entity.getHealth();
    }

    @Override
    public int _INVALID_getHealth() {
        return entity._INVALID_getHealth();
    }

    @Override
    public void _INVALID_setHealth(int i) {
        entity._INVALID_setHealth(i);
    }

    @Override
    public double getMaxHealth() {
        return entity.getMaxHealth();
    }

    @Override
    public int _INVALID_getMaxHealth() {
        return entity._INVALID_getMaxHealth();
    }

    @Override
    public void _INVALID_setMaxHealth(int i) {
        entity._INVALID_setMaxHealth(i);
    }
}

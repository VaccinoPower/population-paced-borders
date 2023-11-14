package com.example.addon.extender.listener;

import com.example.addon.extender.ExtenderConfig;
import com.example.addon.extender.ExtenderStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class MobDeathListener implements Listener {
    private static final Random RANDOM = new Random();
    private final ExtenderConfig config;

    public MobDeathListener(ExtenderConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getKiller() == null) {
            return;
        }
        String mobName = entity.getType().getName();
        if (areGoodChance(mobName)) {
            ItemStack item = ExtenderStack.createRandom(config.getMaterial(), config.getExpansionSizes(), config.getWeights());
            event.getDrops().add(item);
        }
    }

    public boolean areGoodChance(String mobName) {
        double dropChance = config.getMobDropChance(mobName);
        if (Math.abs(dropChance - 1.0) < 1e-6) {
            return true;
        }
        return RANDOM.nextDouble() < dropChance;
    }
}

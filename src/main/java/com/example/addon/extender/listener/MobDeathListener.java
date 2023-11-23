package com.example.addon.extender.listener;

import com.example.addon.extender.ExtenderConfig;
import com.example.addon.extender.ExtenderStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import java.util.Random;
import java.util.logging.Level;

public class MobDeathListener implements Listener {
    private static final Random RANDOM = new Random();
    private final ExtenderConfig config;

    public MobDeathListener(ExtenderConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        if (killer == null) {
            return;
        }
        String mobName = entity.getType().getName();
        if (areGoodChance(mobName)) {
            ExtenderStack extender = ExtenderStack.createRandom(config.getMaterial(), config.getExpansionSizes(), config.getWeights());
            event.getDrops().add(extender);
            log(killer, entity, extender);
        }
    }

    private void log(Player player, LivingEntity entity, ExtenderStack extender) {
        String logPattern = "{0} knocked out extender ({1} level) from {2}";
        Object[] logParams = {player.getName(), extender.getExpansionSize(), entity.getType().getKey().asString()};
        config.getLogger().log(Level.INFO, logPattern, logParams);
    }

    private boolean areGoodChance(String mobName) {
        double dropChance = config.getMobDropChance(mobName);
        if (Math.abs(dropChance - 1.0) < 1e-6) {
            return true;
        }
        return RANDOM.nextDouble() < dropChance;
    }
}

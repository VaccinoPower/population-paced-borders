package com.example.eventlistener;

import com.example.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import static com.example.ExpressionCalculator.evaluateExpression;

public class PlayerJoinListener implements Listener {
    private final ConfigManager config;
    private static final String ERROR_SUFFIX = "\nWorld border remained unchanged.";
    private static final String ERROR_CONFIG = "Config contains invalid data.";
    private static final String ERROR_WORLD_404 = "An error occurred while working with WorldBorder." +
            "World name \"%s\"doesn't exist.";
    private static final String ERROR_FORMULA = "There was an error in the formula:\"%s\".";

    public PlayerJoinListener(ConfigManager config) {
        this.config = config;
        if (config.validate()) {
            for (String worldName : config.getWorlds()) {
                updateWorldSize(config.getMaxOnline(), worldName);
            }
        } else {
            Bukkit.getLogger().severe(ERROR_CONFIG + ERROR_SUFFIX);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final int online = Bukkit.getServer().getOnlinePlayers().size();
        boolean validate = config.validate();
        if (validate && online > config.getMaxOnline()) {
            for (String worldName : config.getWorlds()) {
                updateWorldSize(online, worldName);
            }
        }
        if (!validate) {
            Bukkit.getLogger().severe(ERROR_CONFIG + ERROR_SUFFIX);
        }
        config.updateMaxOnline(online);
    }

    private void updateWorldSize(int online, String worldName) {
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) {
            Bukkit.getLogger().severe(String.format(ERROR_WORLD_404 + ERROR_SUFFIX, worldName));
            return;
        }
        WorldBorder worldBorder = world.getWorldBorder();
        String formula = config.getBarrierFormula(worldName);
        double calculatedSize;
        try {
            calculatedSize = evaluateExpression(formula, online) * config.getChunkSize();
        } catch (Exception exception) {
            Bukkit.getLogger().severe(String.format(ERROR_FORMULA + ERROR_SUFFIX, formula));
            return;
        }
        final double EPSILON = 1e-6;
        if (Math.abs(worldBorder.getSize() - calculatedSize) > EPSILON) {
            worldBorder.setSize(calculatedSize);
        }
    }
}

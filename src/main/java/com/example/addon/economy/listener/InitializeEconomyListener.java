package com.example.addon.economy.listener;

import com.example.addon.economy.EconomyConfig;
import com.example.config.ConfigManager;
import com.example.config.WorldConfig;
import com.example.event.InitializeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class InitializeEconomyListener implements Listener {
    private final String extraBlocksKey;
    private final EconomyConfig economyConfig;
    private final WorldConfig worldConfig;

    public InitializeEconomyListener(ConfigManager configManager, String extraBlocksKey) {
        this.economyConfig = configManager.getEconomyConfig();
        this.worldConfig = configManager.getWorldConfig();
        this.extraBlocksKey = extraBlocksKey;
    }

    @EventHandler
    private void onInitialize(InitializeEvent event) {
        Map<String, Double> worldSizesMap = economyConfig.getWorldSizesMap(worldConfig.getWorlds());
        worldConfig.resizeWorlds(extraBlocksKey, worldSizesMap);
    }
}

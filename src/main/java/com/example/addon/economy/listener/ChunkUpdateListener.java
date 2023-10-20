package com.example.addon.economy.listener;

import com.example.config.ConfigManager;
import com.example.config.WorldConfig;
import com.example.event.BorderChangeEvent;
import com.example.addon.economy.EconomyConfig;
import com.example.exeption.InvalidFormulaException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.Map;


public class ChunkUpdateListener implements Listener {
    private final String extraBlocksKey;
    private final EconomyConfig economyConfig;
    private final WorldConfig worldConfig;

    public ChunkUpdateListener(ConfigManager configManager, String extraBlocksKey) {
        this.economyConfig = configManager.getEconomyConfig();
        this.worldConfig = configManager.getWorldConfig();
        this.extraBlocksKey = extraBlocksKey;
    }

    @EventHandler
    private void onBorderChange(BorderChangeEvent event) {
        int addedBlocks = event.getAddedBlocks();
        if (addedBlocks <= 0) {
            return;
        }
        economyConfig.subBlocksLevel(addedBlocks);
        try {
            economyConfig.calculateExpansive();
        } catch (InvalidFormulaException e) {
            economyConfig.getLogger().warning(e::getMessage);
        }
        Map<String, Double> worldSizesMap = economyConfig.getWorldSizesMap(worldConfig.getWorlds());
        worldConfig.resizeWorlds(extraBlocksKey, worldSizesMap);
    }
}

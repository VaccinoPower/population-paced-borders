package com.example.addon.maxonline.listener;

import com.example.config.ConfigManager;
import com.example.config.ServerLimitsConfig;
import com.example.config.WorldConfig;
import com.example.event.InitializeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class InitializeMaxOnlineListener implements Listener {
    private final String extraBlocksKey;
    private final WorldConfig worldConfig;
    private final ServerLimitsConfig serverLimitsConfig;

    public InitializeMaxOnlineListener(ConfigManager configManager, String extraBlocksKey) {
        this.worldConfig = configManager.getWorldConfig();
        this.serverLimitsConfig = configManager.getServerLimitsConfig();
        this.extraBlocksKey = extraBlocksKey;
    }

    @EventHandler
    private void onInitialize(InitializeEvent event) {
        Map<String, Double> worldSizesMap = worldConfig.getWorldSizesMap(serverLimitsConfig.getMaxOnline());
        worldConfig.resizeWorlds(extraBlocksKey, worldSizesMap);
    }
}

package com.example.addon.maxonline.listener;

import com.example.config.ConfigManager;
import com.example.config.ServerLimitsConfig;
import com.example.event.BorderChangeEvent;
import com.example.event.InitializeEvent;
import com.example.event.MaxOnlineChangeEvent;
import com.example.config.WorldConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class MaxOnlineUpdateListener implements Listener {
    private final String extraBlocksKey;
    private final WorldConfig worldConfig;

    public MaxOnlineUpdateListener(ConfigManager configManager, String extraBlocksKey) {
        this.worldConfig = configManager.getWorldConfig();
        this.extraBlocksKey =  extraBlocksKey;
    }

    @EventHandler
    public void onMaxOnlineUpdate(MaxOnlineChangeEvent event) {
        Map<String, Double> worldSizesMap = worldConfig.getWorldSizesMap(event.getNewMaxOnline());
        double prevBlocks = getBorderRadius("world");
        worldConfig.resizeWorlds(extraBlocksKey, worldSizesMap);
        double curBlocks = getBorderRadius("world");
        Bukkit.getServer().getPluginManager().callEvent(new BorderChangeEvent((int)((curBlocks - prevBlocks)/2)));
    }

    private static double getBorderRadius(String worldName) {
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) {
            return 0;
        }
        return world.getWorldBorder().getSize();
    }
}

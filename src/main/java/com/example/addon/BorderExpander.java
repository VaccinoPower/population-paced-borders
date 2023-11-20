package com.example.addon;

import com.example.config.WorldConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BorderExpander {
    private final String extraBlocksKey;
    protected final WorldConfig worldConfig;

    protected BorderExpander(String extraBlocksKey, WorldConfig worldConfig) {
        this.extraBlocksKey = extraBlocksKey;
        this.worldConfig = worldConfig;
    }

    public final void expand() {
        Map<String, Integer> worldSizesMap = getWorldSizesMap();
        worldConfig.resizeWorlds(extraBlocksKey, worldSizesMap);
    }

    public final Logger getLogger() {
        return worldConfig.getLogger();
    }

    public final Set<String> getWorlds() {
        return worldConfig.getWorlds();
    }

    public final void logWorldSizes() {
        getWorlds().forEach(this::logWorldSize);
    }

    private void logWorldSize(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            String logErrorPattern = "{0} not found. Try editing configuration.";
            Object[] logErrorParams = {worldName};
            getLogger().log(Level.WARNING, logErrorPattern, logErrorParams);
            return;
        }
        String logAboutWorldPattern = "{0} radius is {1} blocks now.";
        Object[] logAboutWorldParams = {worldName, Math.round(world.getWorldBorder().getSize() / 2)};
        getLogger().log(Level.INFO, logAboutWorldPattern, logAboutWorldParams);
    }

    protected final Map<String, Integer> getWorldSizesMap() {
        Map<String, Integer> worldSizesMap = new HashMap<>();
        getWorlds().forEach(v -> worldSizesMap.put(v, getWorldSize(v)));
        return worldSizesMap;
    }

    protected abstract Integer getWorldSize(String worldName);
}

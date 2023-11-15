package com.example.addon;

import com.example.config.WorldConfig;
import java.util.HashMap;
import java.util.Map;

public abstract class BorderExpander {
    private final String extraBlocksKey;
    protected final WorldConfig worldConfig;

    protected BorderExpander(String extraBlocksKey, WorldConfig worldConfig) {
        this.extraBlocksKey = extraBlocksKey;
        this.worldConfig = worldConfig;
    }

    public final void expand() {
        Map<String, Double> worldSizesMap = getWorldSizesMap(worldConfig.getWorlds());
        worldConfig.resizeWorlds(extraBlocksKey, worldSizesMap);
    }

    protected final Map<String, Double> getWorldSizesMap(Iterable<String> worlds) {
        Map<String, Double> worldSizesMap = new HashMap<>();
        worlds.forEach(v -> worldSizesMap.put(v, getWorldSize(v)));
        return worldSizesMap;
    }

    protected abstract Double getWorldSize(String worldName);
}

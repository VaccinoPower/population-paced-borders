package com.example.config;

import com.example.util.WorldBorderUtil;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.config.ConfigKey.*;

public class WorldConfig extends AbstractConfig {
    private final WorldBorderUtil worldBorderUtil;

    public WorldConfig(Configurator configurator, WorldBorderUtil worldBorderUtil) {
        super(configurator);
        this.worldBorderUtil = worldBorderUtil;
    }

    public void resizeWorldBorder(Map<String, String> worldFormulaMap, int parameter) {
        worldBorderUtil.updateWorldSizes(worldFormulaMap, parameter);
    }

    public Map<String, String> getWorldFormulaMap() {
        ConfigurationSection worldSection = getWorldsSection();
        if (worldSection == null) {
            return Collections.emptyMap();
        }
        HashMap<String, String> worldFormulaMap = new HashMap<>();
        for (String world : getWorlds()) {
            String barrierFormulaPath = world + "." + BARRIER_FORMULA.key;
            String formula = "(" + worldSection.getString(barrierFormulaPath, getFormula()) + ")*" + (2 * getChunkSize());
            worldFormulaMap.put(world, formula);
        }
        return worldFormulaMap;
    }

    public int getChunkSize() {
        return getInt(CHUNK_SIZE);
    }

    private ConfigurationSection getWorldsSection() {
        return getConfigurationSection(WORLDS);
    }

    private String getFormula() {
        return getString(BARRIER_FORMULA);
    }

    private Set<String> getWorlds() {
        ConfigurationSection worldSection = getWorldsSection();
        return worldSection == null ? Collections.emptySet() : worldSection.getKeys(false);
    }
}

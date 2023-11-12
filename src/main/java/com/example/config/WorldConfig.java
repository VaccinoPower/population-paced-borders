package com.example.config;

import com.example.util.WorldBorderUtil;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.config.ConfigKey.*;
import static com.example.config.ConfigKey.MAX_ONLINE;

public class WorldConfig extends AbstractConfig {
    private final Map<String, Map<String, Double>> extraBlocks = new HashMap<>();
    private final WorldBorderUtil worldBorderUtil;

    public WorldConfig(Configurator configurator) {
        super(configurator);
        this.worldBorderUtil = new WorldBorderUtil(this::getLogger);
    }

    public Set<String> getWorlds() {
        ConfigurationSection worldSection = getWorldsSection();
        return worldSection == null ? Collections.emptySet() : worldSection.getKeys(false);
    }

    public void resizeWorlds(String extraKey, Map<String, Double> worldSizesMap) {
        Map<String, Double> sumSizes = new HashMap<>();
        Map<String, Double> initBorder = new HashMap<>();
        getWorlds().forEach(worldName -> initBorder.put(worldName, getInitBorder()));
        extraBlocks.put(INITIAL_BARRIER_SIZE.key, initBorder);
        extraBlocks.put(extraKey, worldSizesMap);
        extraBlocks.values().forEach(expansive -> expansive.forEach((key, value) -> sumSizes.merge(key, value, Double::sum)));
        worldBorderUtil.updateWorlds(sumSizes);
    }

    public int getChunkSize() {
        return getInt(CHUNK_SIZE);
    }

    private double getInitBorder() {
        return getDouble(INITIAL_BARRIER_SIZE);
    }

    private ConfigurationSection getWorldsSection() {
        return getConfigurationSection(WORLDS);
    }

    public String getFormula(String worldName) {
        return "(" + getString(WORLDS.key + "." + worldName, BARRIER_FORMULA) + ")";
    }

    public int getMaxOnline() {
        return getInt(MAX_ONLINE);
    }

    public void setMaxOnline(int online) {
        setValue(MAX_ONLINE, online);
    }
}

package com.example.config;

import com.example.exeption.InvalidFormulaException;
import com.example.util.ExpressionCalculator;
import com.example.util.WorldBorderUtil;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.config.ConfigKey.BARRIER_FORMULA;
import static com.example.config.ConfigKey.CHUNK_SIZE;
import static com.example.config.ConfigKey.INITIAL_BARRIER_SIZE;
import static com.example.config.ConfigKey.WORLDS;

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

    public Map<String, Double> getWorldSizesMap(int newMaxOnline) {
        HashMap<String, Double> worldSizesMap = new HashMap<>();
        for (String world : getWorlds()) {
            try {
                String formula = getFormula(world) + "*" + (2 * getChunkSize());
                double borderSize = ExpressionCalculator.evaluateExpression(formula, newMaxOnline);
                worldSizesMap.put(world, borderSize);
            } catch (InvalidFormulaException e) {
                getLogger().warning(e::getMessage);
            }
        }
        return worldSizesMap.size() != 0 ? worldSizesMap : Collections.emptyMap();
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

    private int getChunkSize() {
        return getInt(CHUNK_SIZE);
    }

    private double getInitBorder() {
        return getDouble(INITIAL_BARRIER_SIZE);
    }

    private String getDefaultFormula() {
        return getString(BARRIER_FORMULA);
    }

    private ConfigurationSection getWorldsSection() {
        return getConfigurationSection(WORLDS);
    }

    private String getFormula(String worldName) {
        ConfigurationSection worldSection = getWorldsSection();
        return "(" + worldSection.getString(worldName + "." + BARRIER_FORMULA.key, getDefaultFormula()) + ")";
    }
}

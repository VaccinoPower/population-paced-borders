package com.example.config;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.config.ConfigKey.*;
import static com.example.config.ConfigKey.MAX_ONLINE;

public class WorldConfig extends AbstractConfig {
    private final Map<String, Map<String, Double>> extraBlocks = new HashMap<>();

    public WorldConfig(Configurator configurator) {
        super(configurator);
    }

    public void setMaxOnline(int online) {
        setValue(MAX_ONLINE, online);
    }

    private double getInitBorder() {
        return getDouble(INITIAL_BARRIER_SIZE);
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
        sumSizes.forEach(this::updateWorld);
    }
    public int getChunkSize() {
        return getInt(CHUNK_SIZE);
    }

    public String getFormula(String worldName) {
        return "(" + getString(WORLDS.key + "." + worldName, BARRIER_FORMULA) + ")";
    }

    public int getMaxOnline() {
        return getInt(MAX_ONLINE);
    }
    private ConfigurationSection getWorldsSection() {
        return getConfigurationSection(WORLDS);
    }

    private void updateWorld(String worldName, Double size) {
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) {
            String message = "An error occurred while working with WorldBorder. World name \"" + worldName + "\" doesn't exist.";
            getLogger().warning(message);
            return;
        }
        if (Math.abs(world.getWorldBorder().getSize() - size) > 1e-6) {
            world.getWorldBorder().setSize(size);
        }
    }
}

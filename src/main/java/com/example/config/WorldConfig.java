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
    private final Map<String, Map<String, Integer>> extraBlocks = new HashMap<>();

    public WorldConfig(Configurator configurator) {
        super(configurator);
    }

    public void setMaxOnline(int online) {
        setValue(MAX_ONLINE, online);
    }

    private int getInitBorder() {
        return getInt(INITIAL_BARRIER_SIZE);
    }

    public Set<String> getWorlds() {
        ConfigurationSection worldSection = getWorldsSection();
        return worldSection == null ? Collections.emptySet() : worldSection.getKeys(false);
    }

    public void resizeWorlds(String extraKey, Map<String, Integer> worldSizesMap) {
        Map<String, Integer> sumSizes = new HashMap<>();
        Map<String, Integer> initBorder = new HashMap<>();
        getWorlds().forEach(worldName -> initBorder.put(worldName, getInitBorder()));
        extraBlocks.put(INITIAL_BARRIER_SIZE.key, initBorder);
        extraBlocks.put(extraKey, worldSizesMap);
        extraBlocks.values().forEach(expansive -> expansive.forEach((key, value) -> sumSizes.merge(key, value, Integer::sum)));
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

    private void updateWorld(String worldName, int size) {
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) {
            String message = "An error occurred while working with WorldBorder. World name \"" + worldName + "\" doesn't exist.";
            getLogger().warning(message);
            return;
        }
        long worldSize = Math.round(world.getWorldBorder().getSize());
        if (worldSize != size) {
            world.getWorldBorder().setSize(size);
        }
    }
}

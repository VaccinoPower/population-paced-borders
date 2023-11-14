package com.example.addon.extender;

import com.example.config.AbstractConfig;
import com.example.config.Configurator;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.config.ConfigKey.*;

public class ExtenderConfig extends AbstractConfig {
    public ExtenderConfig(Configurator configurator) {
        super(configurator);
    }

    public double getMobDropChance(String name) {
        return getDouble(ALLOWED_MOBS.key + "." + name, DROP_CHANCE);
    }

    public Material getMaterial() {
        return Material.getMaterial(getString(MATERIAL));
    }

    public int getWorldExpandedBy() {
        return getInt(WORLD_EXPANDED_BY);
    }

    public void setWorldExpanded(int amount) {
        setValue(WORLD_EXPANDED_BY, amount);
    }

    public void addWorldExpanded(Integer size) {
        setWorldExpanded(getWorldExpandedBy() + size);
    }

    public Map<String, Double> getWeights() {
        Map<String, Double> map = new HashMap<>();
        getItems().forEach(item -> map.put(item, getDropChance(item)));
        return map;
    }

    public Map<String, Integer> getExpansionSizes() {
        Map<String, Integer> map = new HashMap<>();
        getItems().forEach(item -> map.put(item, getExpansionSize(item)));
        return map;
    }

    private int getExpansionSize(String name) {
        return getInt(ITEMS.key + "." + name, EXPANSION_SIZE);
    }

    private double getDropChance(String name) {
        return Math.abs(getDouble(ITEMS.key + "." + name, DROP_CHANCE));
    }

    private Set<String> getItems() {
        ConfigurationSection items = getConfigurationSection(ITEMS);
        if (items == null) {
            return Collections.emptySet();
        }
        return items.getKeys(false);
    }
}

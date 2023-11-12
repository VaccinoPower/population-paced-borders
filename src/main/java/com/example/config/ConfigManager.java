package com.example.config;

import com.example.addon.economy.EconomyConfig;
import com.example.addon.motd.MotdConfig;

public class ConfigManager {
    private final MotdConfig motdConfig;
    private final EconomyConfig economyConfig;
    private final WorldConfig worldConfig;

    public ConfigManager(Configurator configurator) {
        this.motdConfig = new MotdConfig(configurator);
        this.economyConfig = new EconomyConfig(configurator);
        this.worldConfig = new WorldConfig(configurator);
    }

    public MotdConfig getMotdConfig() {
        return motdConfig;
    }

    public EconomyConfig getEconomyConfig() {
        return economyConfig;
    }

    public WorldConfig getWorldConfig() {
        return worldConfig;
    }

    public void reload() {
        motdConfig.reload();
        economyConfig.reload();
        worldConfig.reload();
    }

    public void save() {
        motdConfig.save();
        economyConfig.save();
        worldConfig.save();
    }
}

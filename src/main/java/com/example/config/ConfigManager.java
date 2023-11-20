package com.example.config;

import com.example.addon.economy.EconomyConfig;
import com.example.addon.extender.ExtenderConfig;
import com.example.addon.motd.MotdConfig;

import java.util.logging.Logger;

public class ConfigManager {
    private final MotdConfig motdConfig;
    private final EconomyConfig economyConfig;
    private final ExtenderConfig extenderConfig;
    private final WorldConfig worldConfig;

    public ConfigManager(Configurator configurator) {
        this.motdConfig = new MotdConfig(configurator);
        this.economyConfig = new EconomyConfig(configurator);
        this.extenderConfig = new ExtenderConfig(configurator);
        this.worldConfig = new WorldConfig(configurator);
    }

    public MotdConfig getMotdConfig() {
        return motdConfig;
    }

    public EconomyConfig getEconomyConfig() {
        return economyConfig;
    }

    public ExtenderConfig getExtenderConfig() {
        return extenderConfig;
    }

    public WorldConfig getWorldConfig() {
        return worldConfig;
    }

    public void reload() {
        motdConfig.reload();
        economyConfig.reload();
        extenderConfig.reload();
        worldConfig.reload();
    }

    public void save() {
        motdConfig.save();
        economyConfig.save();
        extenderConfig.save();
        worldConfig.save();
    }

    public Logger getLogger() {
        return worldConfig.getLogger();
    }
}

package com.example.config;

import com.example.exeption.InvalidFormulaException;
import com.example.util.WorldBorderUtil;
import net.kyori.adventure.text.Component;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class ConfigManager {
    private final MotdConfig motdConfig;
    private final EconomyConfig economyConfig;
    private final ServerLimitsConfig serverLimitsConfig;
    private final WorldConfig worldConfig;
    private final Supplier<Logger> logger;

    public ConfigManager(Configurator configurator, WorldBorderUtil worldBorderUtil, Supplier<Logger> logger) {
        this.motdConfig = new MotdConfig(configurator);
        this.economyConfig = new EconomyConfig(configurator);
        this.serverLimitsConfig = new ServerLimitsConfig(configurator);
        this.worldConfig = new WorldConfig(configurator, worldBorderUtil);
        this.logger = logger;
    }

    public void resizeBorder(int online) {
        if (online <= serverLimitsConfig.getMaxOnline()) {
            return;
        }
        serverLimitsConfig.setMaxOnline(online);
        reloadEconomy();
        resizeWorldBorder();
    }

    public void reload() {
        motdConfig.reload();
        economyConfig.reload();
        serverLimitsConfig.reload();
        worldConfig.reload();
        resizeWorldBorder();
    }

    public void save() {
        motdConfig.save();
        economyConfig.save();
        serverLimitsConfig.save();
        worldConfig.save();
    }

    public void reloadEconomy() {
        economyConfig.recalculate(worldConfig.getChunkSize());
        try {
            calculateExpansive(0);
        } catch (InvalidFormulaException e) {
            logger.get().severe(e::getMessage);
        }
    }

    public String getAboutBalance() {
        return economyConfig.aboutBalance();
    }

    public void resizeWorldBorder() {
        worldConfig.resizeWorldBorder(getFormulas(), getExpansionParameter());
    }

    public void pay(Number money)  throws InvalidFormulaException {
        calculateExpansive(money.intValue());
        resizeWorldBorder();
    }

    public void calculateExpansive(int money) throws InvalidFormulaException {
        economyConfig.calculateExpansive(money);
    }

    private String getEconomyFormula() {
        return String.valueOf(2*economyConfig.getBankLevel());
    }

    private int getExpansionParameter() {
        return serverLimitsConfig.getMaxOnline();
    }

    private Map<String, String> getFormulas() {
        Map<String, String> worldFormulaMap =  worldConfig.getWorldFormulaMap();
        worldFormulaMap.replaceAll((key, value) -> value + "+" + getEconomyFormula());
        return worldFormulaMap;
    }

    public boolean shouldChangeMotd() {
        return motdConfig.shouldChangeMotd();
    }

    public Component getMotdMessage() {
        return motdConfig.getMotdMessage();
    }
}

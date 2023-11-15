package com.example.addon.economy;

import com.example.addon.BorderExpander;
import com.example.config.WorldConfig;

public class EconomyBorderExpander extends BorderExpander {
    private static final String EXTRA_BLOCKS_KEY = "economy";
    private final EconomyConfig economyConfig;

    public EconomyBorderExpander(EconomyConfig economyConfig, WorldConfig worldConfig) {
        super(EXTRA_BLOCKS_KEY, worldConfig);
        this.economyConfig = economyConfig;
    }

    @Override
    protected Double getWorldSize(String worldName) {
        return 2.0 * economyConfig.getBlocksLevel();
    }
}

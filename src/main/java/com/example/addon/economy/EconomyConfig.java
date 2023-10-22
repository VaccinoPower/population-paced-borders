package com.example.addon.economy;

import com.example.config.AbstractConfig;
import com.example.config.Configurator;

import static com.example.config.ConfigKey.EXPANSION_BALANCE;
import static com.example.config.ConfigKey.EXPANSION_BANK_LEVEL;
import static com.example.config.ConfigKey.EXPANSION_BLOCKS_LEVEL;
import static com.example.config.ConfigKey.EXPANSION_FORMULA;

public class EconomyConfig extends AbstractConfig {
    public EconomyConfig(Configurator configurator) {
        super(configurator);
    }

    public int getBalance() {
        return getInt(EXPANSION_BALANCE);
    }

    public String getExpansionFormula() {
        return getString(EXPANSION_FORMULA);
    }

    public int getBankLevel() {
        return getInt(EXPANSION_BANK_LEVEL);
    }

    public int getBlocksLevel() {
        return getInt(EXPANSION_BLOCKS_LEVEL);
    }

    public void setBalance(int balance) {
        setValue(EXPANSION_BALANCE, balance);
    }

    public void setBankLevel(int level) {
        setValue(EXPANSION_BANK_LEVEL, level);
    }

    public void setBlocksLevel(int level) {
        setValue(EXPANSION_BLOCKS_LEVEL, level);
    }
}

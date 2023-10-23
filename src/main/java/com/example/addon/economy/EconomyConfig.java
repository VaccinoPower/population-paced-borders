package com.example.addon.economy;

import com.example.config.AbstractConfig;
import com.example.config.Configurator;
import java.util.UUID;

import static com.example.config.ConfigKey.*;

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

    public int getLevelLowering(UUID uuid) {
        return getInt(PLAYERS.key + "." + uuid.toString(), LEVEL_LOWERING);
    }

    public void setLevelLowering(UUID uuid, String nickname, int level) {
        setValue(PLAYERS.key + "." + uuid + "." + LEVEL_LOWERING.key, level);
        setValue(PLAYERS.key + "." + uuid + "." + NICKNAME.key, nickname);
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

package com.example.addon.economy;

import com.example.exeption.InvalidFormulaException;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Bank {
    private final EconomyConfig config;

    public Bank(EconomyConfig economyConfig) {
        this.config = economyConfig;
    }

    public String aboutBalance() {
        String expansionPrice;
        try {
            expansionPrice = String.valueOf(config.getExpansionPrice());
        } catch (InvalidFormulaException e) {
            expansionPrice = "???";
        }
        return String.format("Bank level: %d%n$%d / $%s for expansion.", config.getBankLevel(), config.getBalance(), expansionPrice);
    }

    public void levelDown(int level) {
        config.subtractBankLevel(level);
    }

    public void levelDown(Player player, int level) {
        UUID uuid = player.getUniqueId();
        int playerLevels = config.getLevelLowering(uuid);
        if (playerLevels >= level && level > 0) {
            int bankLevel = config.getBankLevel();
            int newBankLevel = Math.max(0, bankLevel - level);
            int newLevelLowering = playerLevels - Math.min(level, bankLevel);
            String nickname = player.getName();
            config.setBankLevel(newBankLevel);
            config.setLevelLowering(uuid, nickname, newLevelLowering);
        }
    }

    public void reset() {
        config.setBankLevel(0);
        config.setBalance(0);
    }

    public void calculateExpansive() throws InvalidFormulaException {
        calculateExpansive(0);
    }

    public void calculateExpansive(int money) throws InvalidFormulaException {
        int bankMoney = config.getBalance() + money;
        int price = config.getExpansionPrice();
        if (price > bankMoney){
            config.setBalance(bankMoney);
            return;
        }
        int prevBankLevel = config.getBankLevel();
        int bankLevel = prevBankLevel;
        while (price <= bankMoney) {
            if (price <= 0) {
                throw new InvalidFormulaException("Expansion price wasn't positive.");
            }
            bankMoney -= price;
            bankLevel++;
            price = config.getExpansionPrice(bankLevel);
        }
        int addedLevels = bankLevel - prevBankLevel;
        addBankLevel(addedLevels);
        config.setBalance(bankMoney);
    }

    public void subtractBankLevel(int level) {
        addBankLevel(-level);
    }

    private void addBankLevel(int levels) {
        config.addBlocksLevel(levels);
        config.addBankLevel(levels);
    }
}

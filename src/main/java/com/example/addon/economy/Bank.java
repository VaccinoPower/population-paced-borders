package com.example.addon.economy;

import com.example.exeption.InvalidFormulaException;
import org.bukkit.entity.Player;
import java.util.logging.Logger;

public class Bank {
    private final EconomyConfig config;

    public Bank(EconomyConfig economyConfig) {
        this.config = economyConfig;
    }

    public String aboutBalance() {
        try {
            return getBalanceMessage(String.valueOf(config.getExpansionPrice()));
        } catch (InvalidFormulaException e) {
            return getBalanceMessage( "???");
        }
    }

    public int getLevel() {
        return config.getBankLevel();
    }

    public int getBalance() {
        return config.getBalance();
    }

    public int getLevelLowering(Player player) {
        return config.getLevelLowering(player.getUniqueId());
    }

    public void addLowering(Player player, int level) {
        config.addLowering(player.getUniqueId(), player.getName(), level);
    }

    public void levelDown(int level) {
        config.subtractBankLevel(level);
    }

    public void levelDown(Player player, int level) {
        int playerLevels = getLevelLowering(player);
        if (playerLevels < level || level <= 0) {
            return;
        }
        int bankLevel = getLevel();
        int newLevelLowering = playerLevels - Math.min(level, bankLevel);
        config.setBankLevel(Math.max(0, bankLevel - level));
        config.setLevelLowering(player.getUniqueId(), player.getName(), newLevelLowering);
    }

    public void reset() {
        config.setBankLevel(0);
        config.setBalance(0);
    }

    public void recalculate() throws InvalidFormulaException {
        recalculate(0);
    }

    public void recalculate(int money) throws InvalidFormulaException {
        int bankMoney = getBalance() + money;
        int price = config.getExpansionPrice();
        if (price > bankMoney){
            config.setBalance(bankMoney);
            return;
        }
        int prevBankLevel = getLevel();
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

    public Logger getLogger() {
        return config.getLogger();
    }

    private void addBankLevel(int levels) {
        config.addBlocksLevel(levels);
        config.addBankLevel(levels);
    }

    private String getBalanceMessage(String expansionPrice) {
        return "Bank Level: " + getLevel() + "\n" + getBalance() + " / " + expansionPrice + " for expansion.";
    }
}

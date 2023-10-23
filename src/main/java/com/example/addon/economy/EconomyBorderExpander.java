package com.example.addon.economy;

import com.example.config.WorldConfig;
import com.example.exeption.InvalidFormulaException;
import com.example.util.ExpressionCalculator;
import org.bukkit.entity.Player;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class EconomyBorderExpander {
    private static final String EXTRA_BLOCKS_KEY = "economy";
    private final EconomyConfig economyConfig;
    private final  WorldConfig worldConfig;

    public EconomyBorderExpander(EconomyConfig economyConfig, WorldConfig worldConfig) {
        this.economyConfig = economyConfig;
        this.worldConfig = worldConfig;
    }

    public void expand(BigDecimal payment) throws InvalidFormulaException {
        expandIteratively(payment.intValue());
    }

    public void expand(int addedBlocks) {
        if (addedBlocks > 0) {
            addBankLevel(-addedBlocks);
            expandIteratively();
        }
    }

    public void expand() {
        Map<String, Double> worldSizesMap = getWorldSizesMap(worldConfig.getWorlds());
        worldConfig.resizeWorlds(EXTRA_BLOCKS_KEY, worldSizesMap);
    }

    public void resetBank() {
        economyConfig.setBankLevel(0);
        economyConfig.setBalance(0);
    }

    public void bankLevelDown(int level) {
        economyConfig.setBankLevel(Math.max(0, economyConfig.getBankLevel() - level));
        expandIteratively();
    }

    public void bankLevelDown(Player player, int level) {
        UUID uuid = player.getUniqueId();
        int playerLevels = economyConfig.getLevelLowering(uuid);
        if (playerLevels >= level && level > 0) {
            int bankLevel = economyConfig.getBankLevel();
            int newBankLevel = Math.max(0, bankLevel - level);
            int newLevelLowering = playerLevels - Math.min(level, bankLevel);
            String nickname = player.getName();
            economyConfig.setBankLevel(newBankLevel);
            economyConfig.setLevelLowering(uuid, nickname, newLevelLowering);
            expandIteratively();
        }
    }

    public void addLowering(Player player, int level) {
        if (level > 0) {
            UUID uuid = player.getUniqueId();
            level += economyConfig.getLevelLowering(uuid);
            economyConfig.setLevelLowering(uuid, player.getName(), level);
        }
    }

    public String aboutBalance() {
        String expansionPrice;
        try {
            expansionPrice = String.valueOf(getExpansionPrice());
        } catch (InvalidFormulaException e) {
            expansionPrice = "???";
        }
        return String.format("Bank level: %d%n$%d / $%s for expansion.", economyConfig.getBankLevel(), economyConfig.getBalance(), expansionPrice);
    }

    private Logger getLogger() {
        return economyConfig.getLogger();
    }

    private void addBankLevel(int levels) {
        economyConfig.setBlocksLevel(Math.max(0, economyConfig.getBlocksLevel() + levels));
        economyConfig.setBankLevel(Math.max(0, economyConfig.getBankLevel() + levels));
    }

    private int getExpansionPrice() throws InvalidFormulaException {
        return (int)ExpressionCalculator.evaluateExpression(economyConfig.getExpansionFormula(), economyConfig.getBankLevel());
    }

    private void calculateExpansive(int money) throws InvalidFormulaException {
        int bankMoney = economyConfig.getBalance() + money;
        int price = getExpansionPrice();
        if (price > bankMoney){
            economyConfig.setBalance(bankMoney);
            return;
        }
        while (price <= bankMoney) {
            if (price <= 0) {
                throw new InvalidFormulaException("Expansion price wasn't positive.");
            }
            bankMoney -= price;
            addBankLevel(1);
            price = getExpansionPrice();
        }
        economyConfig.setBalance(bankMoney);
    }

    private Map<String, Double> getWorldSizesMap(Iterable<String> worlds) {
        Map<String, Double> worldSizesMap = new HashMap<>();
        worlds.forEach(worldName -> worldSizesMap.put(worldName, 2.0 * economyConfig.getBlocksLevel()));
        return worldSizesMap;
    }

    private void expandIteratively() {
        expandIteratively(0);
    }

    private void expandIteratively(int money) {
        final int prevBlocksLevel = economyConfig.getBlocksLevel();
        try {
            calculateExpansive(money);
        } catch (InvalidFormulaException e) {
            getLogger().warning(e::getMessage);
        }
        if (prevBlocksLevel != economyConfig.getBlocksLevel()) {
            expand();
        }
    }
}

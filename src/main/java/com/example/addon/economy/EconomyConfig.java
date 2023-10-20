package com.example.addon.economy;

import com.example.config.AbstractConfig;
import com.example.config.Configurator;
import com.example.exeption.InvalidFormulaException;
import com.example.util.ExpressionCalculator;
import java.util.HashMap;
import java.util.Map;

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

    public void subBlocksLevel(int levels) {
        if (levels > 0) {
            setBlocksLevel(Math.max(0, getBlocksLevel() - levels));
            setBankLevel(Math.max(0, getBankLevel() - levels));
        }
    }

    public void addBankLevel(int levels) {
        if (levels > 0) {
            setBankLevel(getBankLevel() + levels);
            setBlocksLevel(getBlocksLevel() + levels);
        }
    }

    public void resetBank() {
        setBankLevel(0);
        setBalance(0);
    }

    public int getBlocksLevel() {
        return getInt(EXPANSION_BLOCKS_LEVEL);
    }

    public int getExpansionPrice() throws InvalidFormulaException {
        return (int)ExpressionCalculator.evaluateExpression(getExpansionFormula(), getBankLevel());
    }

    public String aboutBalance() {
        String expansionPrice;
        try {
            expansionPrice = String.valueOf(getExpansionPrice());
        } catch (InvalidFormulaException e) {
            expansionPrice = "???";
        }
        return String.format("Bank level: %d%n$%d / $%s for expansion.", getBankLevel(), getBalance(), expansionPrice);
    }

    public void calculateExpansive() throws InvalidFormulaException {
        calculateExpansive(0);
    }
    public void calculateExpansive(int money) throws InvalidFormulaException {
        int bankMoney = getBalance() + money;
        int price = getExpansionPrice();
        if (price > bankMoney){
            setBalance(bankMoney);
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
        setBalance(bankMoney);
    }

    public Map<String, Double> getWorldSizesMap(Iterable<String> worlds) {
        Map<String, Double> worldSizesMap = new HashMap<>();
        worlds.forEach(worldName -> worldSizesMap.put(worldName, 2.0 * getBlocksLevel()));
        return worldSizesMap;
    }

    private void setBalance(int balance) {
        setValue(EXPANSION_BALANCE, balance);
    }

    private void setBankLevel(int level) {
        setValue(EXPANSION_BANK_LEVEL, level);
    }

    private void setBlocksLevel(int level) {
        setValue(EXPANSION_BLOCKS_LEVEL, level);
    }
}

package com.example.config;

import com.example.exeption.InvalidFormulaException;
import com.example.util.ExpressionCalculator;

import static com.example.config.ConfigKey.EXPANSION_BALANCE;
import static com.example.config.ConfigKey.EXPANSION_BANK_LEVEL;
import static com.example.config.ConfigKey.EXPANSION_FORMULA;

public class EconomyConfig extends AbstractConfig {
    protected EconomyConfig(Configurator configurator) {
        super(configurator);
    }

    public int getBalance() {
        return getInt(EXPANSION_BALANCE);
    }

    public String getExpansionFormula() {
        return getString(EXPANSION_FORMULA);
    }

    public void setBalance(int balance) {
        setValue(EXPANSION_BALANCE, balance);
    }

    public int getBankLevel() {
        return getInt(EXPANSION_BANK_LEVEL);
    }

    public void setBankLevel(int level) {
        setValue(EXPANSION_BANK_LEVEL, level);
    }

    public int getExpansionPrice() throws InvalidFormulaException {
        return (int)ExpressionCalculator.evaluateExpression(getExpansionFormula(), getBankLevel());
    }

    public void recalculate(int chunkSize) {
        final int bankLevel = getBankLevel();
        final int updatedBankLevel = bankLevel <= chunkSize ? 0 : ((bankLevel - chunkSize) % chunkSize);
        setBankLevel(updatedBankLevel);
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

    public void calculateExpansive(int money) throws InvalidFormulaException {
        int bankMoney = getBalance() + money;
        int price = getExpansionPrice();
        if (price > bankMoney){
            setBalance(bankMoney);
            return;
        }
        int bankLevel = getBankLevel();
        while (price <= bankMoney) {
            if (price <= 0) {
                throw new InvalidFormulaException("Expansion price wasn't positive.");
            }
            bankLevel++;
            bankMoney -= price;
            setBankLevel(bankLevel);
            price = getExpansionPrice();
        }
        setBalance(bankMoney);
    }
}

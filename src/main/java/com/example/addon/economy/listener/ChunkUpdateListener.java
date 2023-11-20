package com.example.addon.economy.listener;

import com.example.addon.economy.Bank;
import com.example.addon.economy.EconomyBorderExpander;
import com.example.event.BorderChangeEvent;
import com.example.exeption.InvalidFormulaException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Level;


public class ChunkUpdateListener implements Listener {
    private final EconomyBorderExpander borderExpander;
    private final Bank bank;

    public ChunkUpdateListener(Bank bank, EconomyBorderExpander borderExpander) {
        this.bank = bank;
        this.borderExpander = borderExpander;
    }

    @EventHandler
    private void onBorderChange(BorderChangeEvent event) {
        int addedBlocks = event.getAddedBlocks();
        if (addedBlocks <= 0) {
            return;
        }
        int bankLevel = bank.getLevel();
        bank.subtractBankLevel(addedBlocks);
        if (bankLevel == bank.getLevel()) {
            return;
        }
        try {
            bank.calculateExpansive();
            borderExpander.expand();
            bank.getLogger().log(Level.INFO, "Bank blocks absorbed and bank level decreased to {0} (after recalculation).", bank.getLevel());
        } catch (InvalidFormulaException e) {
            bank.getLogger().log(Level.WARNING, e.getMessage());
        }
    }
}

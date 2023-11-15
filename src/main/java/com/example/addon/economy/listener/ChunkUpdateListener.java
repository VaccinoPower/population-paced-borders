package com.example.addon.economy.listener;

import com.example.addon.economy.Bank;
import com.example.addon.economy.EconomyBorderExpander;
import com.example.event.BorderChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class ChunkUpdateListener implements Listener {
    private final EconomyBorderExpander borderExpander;
    private final Bank bank;

    public ChunkUpdateListener(Bank bank, EconomyBorderExpander borderExpander) {
        this.bank = bank;
        this.borderExpander = borderExpander;
    }

    @EventHandler
    private void onBorderChange(BorderChangeEvent event) {
        try {
            int addedBlocks = event.getAddedBlocks();
            if (addedBlocks > 0) {
                bank.subtractBankLevel(addedBlocks);
                bank.calculateExpansive();
                borderExpander.expand();
            }
        } catch (RuntimeException e) {

        }
    }
}

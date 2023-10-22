package com.example.addon.economy.listener;

import com.example.addon.economy.EconomyBorderExpander;
import com.example.event.InitializeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InitializeEconomyListener implements Listener {
    private final EconomyBorderExpander borderExpander;

    public InitializeEconomyListener(EconomyBorderExpander borderExpander) {
        this.borderExpander = borderExpander;
    }

    @EventHandler
    private void onInitialize(InitializeEvent event) {
        borderExpander.expand();
    }
}

package com.example.addon.economy.listener;

import com.example.addon.BorderExpander;
import com.example.event.InitializeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InitializeEconomyListener implements Listener {
    private final BorderExpander borderExpander;

    public InitializeEconomyListener(BorderExpander borderExpander) {
        this.borderExpander = borderExpander;
    }

    @EventHandler
    private void onInitialize(InitializeEvent event) {
        borderExpander.expand();
    }
}

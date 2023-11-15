package com.example.addon.extender.listener;

import com.example.addon.BorderExpander;
import com.example.event.InitializeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InitializeExtenderListener implements Listener {
    private final BorderExpander borderExpander;

    public InitializeExtenderListener(BorderExpander borderExpander) {
        this.borderExpander = borderExpander;
    }

    @EventHandler
    private void onInitialize(InitializeEvent event) {
        borderExpander.expand();
    }
}

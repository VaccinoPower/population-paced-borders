package com.example.addon.economy.listener;

import com.example.addon.economy.EconomyBorderExpander;
import com.example.event.BorderChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class ChunkUpdateListener implements Listener {
    private final EconomyBorderExpander borderExpander;

    public ChunkUpdateListener(EconomyBorderExpander borderExpander) {
        this.borderExpander = borderExpander;
    }

    @EventHandler
    private void onBorderChange(BorderChangeEvent event) {
        int addedBlocks = event.getAddedBlocks();
        borderExpander.expand(addedBlocks);
    }
}

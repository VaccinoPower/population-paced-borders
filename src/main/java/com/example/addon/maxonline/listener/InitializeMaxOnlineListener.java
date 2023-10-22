package com.example.addon.maxonline.listener;

import com.example.addon.maxonline.MaxOnlineBorderExpander;
import com.example.event.InitializeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InitializeMaxOnlineListener implements Listener {
    private final MaxOnlineBorderExpander borderExpander;

    public InitializeMaxOnlineListener(MaxOnlineBorderExpander borderExpander) {
        this.borderExpander = borderExpander;
    }

    @EventHandler
    private void onInitialize(InitializeEvent event) {
        borderExpander.expand();
    }
}

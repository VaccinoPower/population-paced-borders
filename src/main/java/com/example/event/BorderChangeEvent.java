package com.example.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BorderChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final int addedBlocks;

    public BorderChangeEvent(int addedBlocks) {
        this.addedBlocks = addedBlocks;
    }

    public int getAddedBlocks() {
        return addedBlocks;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return getHandlerList();
    }
}

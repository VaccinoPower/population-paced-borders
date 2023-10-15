package com.example.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MaxOnlineChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final int newMaxOnline;

    public MaxOnlineChangeEvent(int newMaxOnline) {
        this.newMaxOnline = newMaxOnline;
    }

    public int getNewMaxOnline() {
        return newMaxOnline;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return getHandlerList();
    }
}

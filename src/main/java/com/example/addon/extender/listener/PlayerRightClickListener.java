package com.example.addon.extender.listener;

import com.example.addon.extender.ExtenderBorderExpander;
import com.example.addon.extender.ExtenderStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerRightClickListener implements Listener {
    private final ExtenderBorderExpander borderExpander;
    public PlayerRightClickListener(ExtenderBorderExpander borderExpander) {
        this.borderExpander = borderExpander;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (!event.getAction().isRightClick() || item == null) {
            return;
        }
        ExtenderStack extender = ExtenderStack.create(item);
        if (extender != null) {
            item.subtract();
            borderExpander.expand(extender.getExpansionSize());
        }
    }
}

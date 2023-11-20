package com.example.addon.extender.listener;

import com.example.addon.extender.ExtenderBorderExpander;
import com.example.addon.extender.ExtenderStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.logging.Level;

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
        if (extender == null) {
            return;
        }
        Player player = event.getPlayer();
        item.subtract();
        int expansionSize = extender.getExpansionSize();
        if (expansionSize > 0) {
            borderExpander.expand(expansionSize);
            sendOk(MessageFormat.format("World border expanded by {0} blocks.", expansionSize));
            logPositiveExpansion(player, expansionSize);
        } else if (expansionSize < 0) {
            borderExpander.reduce(-expansionSize);
            sendOk(MessageFormat.format("World border reduced by {0} blocks.", expansionSize));
            logNegativeExpansion(player, expansionSize);
        } else {
            sendOk(player, "World border has not changed.");
            logBlankExpansion(player);
        }
        borderExpander.logWorldSizes();
    }

    private void logPositiveExpansion(Player player, int expansionSize) {
        String logPattern = "{0} has increased world border by {1}.";
        Object[] logParams = {player.getName(), expansionSize};
        borderExpander.getLogger().log(Level.INFO, logPattern, logParams);
    }

    private void logNegativeExpansion(Player player, int expansionSize) {
        String logPattern = "{0} has reduced world border by {1}.";
        Object[] logParams = {player.getName(), expansionSize};
        borderExpander.getLogger().log(Level.INFO, logPattern, logParams);
    }

    private void logBlankExpansion(Player player) {
        String logPattern = "{0} activated extender with level 0. Worlds sizes should not have changed.";
        Object[] logParams = {player.getName()};
        borderExpander.getLogger().log(Level.INFO, logPattern, logParams);
    }

    private static void sendOk(String msg) {
        Bukkit.broadcast(Component.text(msg, NamedTextColor.GOLD));
    }

    private static void sendOk(Player player, String msg) {
        player.sendMessage(Component.text(msg, NamedTextColor.GREEN));
    }
}

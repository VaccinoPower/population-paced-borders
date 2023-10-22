package com.example.addon.maxonline.listener;

import com.example.addon.maxonline.MaxOnlineBorderExpander;
import com.example.event.BorderChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static org.bukkit.Bukkit.getServer;

public class PlayerJoinListener implements Listener {
    private final MaxOnlineBorderExpander borderExpander;

    public PlayerJoinListener(MaxOnlineBorderExpander borderExpander) {
        this.borderExpander = borderExpander;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        final int online = getServer().getOnlinePlayers().size();
        double prevBlocks = getBorderRadius("world");
        borderExpander.expand(online);
        double curBlocks = getBorderRadius("world");
        Bukkit.getServer().getPluginManager().callEvent(new BorderChangeEvent((int)((curBlocks - prevBlocks)/2)));
    }

    private static double getBorderRadius(String worldName) {
        World world = Bukkit.getServer().getWorld(worldName);
        return world != null ? world.getWorldBorder().getSize() : 0;
    }
}

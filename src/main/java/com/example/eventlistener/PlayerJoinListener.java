package com.example.eventlistener;

import com.example.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final ConfigManager config;

    public PlayerJoinListener(ConfigManager config) {
        this.config = config;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final int online = Bukkit.getServer().getOnlinePlayers().size();
        config.resizeBorder(online);
    }
}

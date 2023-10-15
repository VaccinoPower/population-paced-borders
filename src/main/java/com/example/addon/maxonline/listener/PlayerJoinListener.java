package com.example.addon.maxonline.listener;

import com.example.config.ConfigManager;
import com.example.event.MaxOnlineChangeEvent;
import com.example.config.ServerLimitsConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static org.bukkit.Bukkit.getServer;

public class PlayerJoinListener implements Listener {
    private final ServerLimitsConfig serverLimitsConfig;

    public PlayerJoinListener(ConfigManager configManager) {
        this.serverLimitsConfig = configManager.getServerLimitsConfig();
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        final int online = getServer().getOnlinePlayers().size();
        if (online <= serverLimitsConfig.getMaxOnline()) {
            return;
        }
        serverLimitsConfig.setMaxOnline(online);
        getServer().getPluginManager().callEvent(new MaxOnlineChangeEvent(online));
    }

}

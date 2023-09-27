package com.example.eventlistener;

import com.example.config.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener implements Listener {

    private final ConfigManager config;

    public ServerListPingListener(ConfigManager config) {
        this.config = config;
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        if (!config.shouldChangeMotd()) {
            return;
        }
        event.motd(config.getMotdMessage());
    }


}

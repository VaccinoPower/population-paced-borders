package com.example.eventlistener;

import com.example.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Set;

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
        final String welcomeMessage = config.getWelcomeMessage();
        final String defaultWorld = config.getMotdWorld();
        final World motdWorld = chooseMotdWorld(defaultWorld);
        final int borderRadius = (int)(motdWorld.getWorldBorder().getSize() / 16);
        final String worldBorderMessage = "\n"+ config.getWorldBorderMessage()
                .replaceAll("%chunk_radius%", Integer.toString(borderRadius));
        final Component endMessage = Component.text(worldBorderMessage, NamedTextColor.GRAY)
                .decoration(TextDecoration.BOLD, true);
        Component worldBorderInfo = Component.text()
                .content(welcomeMessage)
                .append(endMessage)
                .build();
        event.motd(worldBorderInfo);
    }

    private World chooseMotdWorld(String defaultWorld) {
        Set<String> configWorlds = config.getWorlds();
        World motdWorld = Bukkit.getServer().getWorld(defaultWorld);
        if (motdWorld == null || !configWorlds.contains(defaultWorld)) {
            for (String world : configWorlds) {
                motdWorld = Bukkit.getServer().getWorld(world);
                if (motdWorld != null) {
                    return motdWorld;
                }
            }
            return Bukkit.getServer().getWorlds().get(0);
        }
        return motdWorld;
    }
}

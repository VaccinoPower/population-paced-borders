package com.example.addon.motd.listener;

import com.example.config.ConfigManager;
import com.example.addon.motd.MotdConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import java.util.function.Supplier;

import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public class ServerListPingListener implements Listener {
    private static final String CHUNK_RADIUS = "%chunk_radius%";
    private static final int CHUNK_SIZE = 16;
    private final MotdConfig motdConfig;

    public ServerListPingListener(ConfigManager configManager) {
        this.motdConfig = configManager.getMotdConfig();
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        if (!motdConfig.shouldChangeMotd()) {
            return;
        }
        event.motd(getMotdMessage());
    }

    private Component getMotdMessage() {
        String defaultMotdWorld = motdConfig.getMotdWorld();
        World motdWorld = chooseMotdWorld(defaultMotdWorld);
        int borderRadius = getWorldRadius(motdWorld);
        String topMessage = replaceRegex(motdConfig::getTopMessage, borderRadius);
        String bottomMessage = replaceRegex(motdConfig::getBottomMessage, borderRadius);
        return Component.text(topMessage + "\n", YELLOW)
                .append(Component.text(bottomMessage, GRAY).decoration(BOLD, true));
    }

    private static int getWorldRadius(World motdWorld) {
        return (int)(motdWorld.getWorldBorder().getSize() / CHUNK_SIZE / 2);
    }

    private static String replaceRegex(Supplier<String> message, int radius) {
        return message.get().replace(CHUNK_RADIUS, String.valueOf(radius));
    }

    private static World chooseMotdWorld(String defaultWorld) {
        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getName().equals(defaultWorld)) {
                return world;
            }
        }
        return Bukkit.getServer().getWorlds().get(0);
    }
}

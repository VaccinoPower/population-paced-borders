package com.example;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.WorldBorder;

public class PopulationPacedBordersPlugin extends JavaPlugin implements Listener {
    private final int CHUNK_SIZE = 16;
    private final String WORLD_NAME = "world";

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        loadConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final int online = getServer().getOnlinePlayers().size();
        try {
            final WorldBorder worldBorder = getServer().getWorld(WORLD_NAME).getWorldBorder();
            final double worldBorderSize = worldBorder.getSize();
            if (worldBorderSize < online * CHUNK_SIZE) {
                worldBorder.setSize(online * CHUNK_SIZE);
            }
        } catch (Exception npe) {
            getLogger().severe("An error occurred while working with WorldBorder. " +
                    "Try changing the world name to \"world\": " + npe.getMessage());
            npe.printStackTrace();
        }
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

}

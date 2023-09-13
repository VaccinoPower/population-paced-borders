package com.example;

import com.example.eventlistener.PlayerJoinListener;
import com.example.eventlistener.ServerListPingListener;
import org.bukkit.plugin.java.JavaPlugin;

public class PopulationPacedBordersPlugin extends JavaPlugin {
    private final ConfigManager config = new ConfigManager(this, true);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(config), this);
        getServer().getPluginManager().registerEvents(new ServerListPingListener(config), this);
    }

}

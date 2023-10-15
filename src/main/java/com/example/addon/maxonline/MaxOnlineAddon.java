package com.example.addon.maxonline;

import com.example.addon.PluginAddon;
import com.example.addon.maxonline.listener.InitializeMaxOnlineListener;
import com.example.config.ConfigManager;
import com.example.addon.maxonline.listener.MaxOnlineUpdateListener;
import com.example.addon.maxonline.listener.PlayerJoinListener;
import com.example.command.PPBCommand;
import org.bukkit.event.Listener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MaxOnlineAddon implements PluginAddon {
    private final PlayerJoinListener playerJoinListener;
    private final MaxOnlineUpdateListener maxOnlineUpdateListener;
    private final InitializeMaxOnlineListener initializeEconomyListener;

    public MaxOnlineAddon(ConfigManager configManager, String extraBlocksKey) {
        this.initializeEconomyListener = new InitializeMaxOnlineListener(configManager, extraBlocksKey);
        this.maxOnlineUpdateListener = new MaxOnlineUpdateListener(configManager, extraBlocksKey);
        this.playerJoinListener = new PlayerJoinListener(configManager);
    }

    @Override
    public List<PPBCommand> getCommandHandlers() {
        return Collections.emptyList();
    }

    @Override
    public List<Listener> getListeners() {
        return Arrays.asList(initializeEconomyListener, maxOnlineUpdateListener, playerJoinListener);
    }
}

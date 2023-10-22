package com.example.addon.maxonline;

import com.example.addon.PluginAddon;
import com.example.addon.maxonline.listener.InitializeMaxOnlineListener;
import com.example.config.ConfigManager;
import com.example.addon.maxonline.listener.PlayerJoinListener;
import java.util.Arrays;

public class MaxOnlineAddon extends PluginAddon {
    public MaxOnlineAddon(ConfigManager configManager) {
        MaxOnlineBorderExpander borderExpander = new MaxOnlineBorderExpander(configManager.getServerLimitsConfig(), configManager.getWorldConfig());
        this.listeners = Arrays.asList(
            new InitializeMaxOnlineListener(borderExpander),
            new PlayerJoinListener(borderExpander)
        );
    }
}

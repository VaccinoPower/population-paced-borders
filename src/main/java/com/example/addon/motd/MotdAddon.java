package com.example.addon.motd;

import com.example.addon.PluginAddon;
import com.example.config.ConfigManager;
import com.example.addon.motd.listener.ServerListPingListener;
import java.util.Collections;

public class MotdAddon extends PluginAddon {
    public MotdAddon(ConfigManager configManager) {
        this.listeners = Collections.singletonList(new ServerListPingListener(configManager));
    }
}

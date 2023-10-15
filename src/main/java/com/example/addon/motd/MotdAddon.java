package com.example.addon.motd;

import com.example.addon.PluginAddon;
import com.example.config.ConfigManager;
import com.example.addon.motd.listener.ServerListPingListener;
import com.example.command.PPBCommand;
import org.bukkit.event.Listener;
import java.util.Collections;
import java.util.List;

public class MotdAddon implements PluginAddon {
    private final ServerListPingListener serverListPingListener;

    public MotdAddon(ConfigManager configManager) {
        serverListPingListener = new ServerListPingListener(configManager);
    }

    @Override
    public List<PPBCommand> getCommandHandlers() {
        return Collections.emptyList();
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.singletonList(serverListPingListener);
    }
}

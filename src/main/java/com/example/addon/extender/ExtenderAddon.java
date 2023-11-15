package com.example.addon.extender;

import com.example.addon.PluginAddon;
import com.example.addon.extender.command.GiveExtenderCommand;
import com.example.addon.extender.listener.InitializeExtenderListener;
import com.example.addon.extender.listener.MobDeathListener;
import com.example.addon.extender.listener.PlayerRightClickListener;
import com.example.config.ConfigManager;
import java.util.Arrays;
import java.util.Collections;

public class ExtenderAddon extends PluginAddon {
    public ExtenderAddon(ConfigManager configManager) {
        ExtenderBorderExpander borderExpander = new ExtenderBorderExpander(configManager.getExtenderConfig(), configManager.getWorldConfig());
        listeners = Arrays.asList(
                new MobDeathListener(configManager.getExtenderConfig()),
                new PlayerRightClickListener(borderExpander),
                new InitializeExtenderListener(borderExpander)
        );
        commands = Collections.singletonList(new GiveExtenderCommand(configManager.getExtenderConfig()));
    }
}

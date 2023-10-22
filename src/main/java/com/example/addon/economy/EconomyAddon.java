package com.example.addon.economy;

import com.example.addon.PluginAddon;
import com.example.addon.economy.command.ResetEconomyCommand;
import com.example.addon.economy.listener.InitializeEconomyListener;
import com.example.config.ConfigManager;
import com.example.addon.economy.command.BalanceCommand;
import com.example.addon.economy.command.SendMoneyCommand;
import com.example.addon.economy.listener.ChunkUpdateListener;
import java.util.Arrays;

public class EconomyAddon extends PluginAddon {
    public EconomyAddon(ConfigManager configManager) {
        EconomyBorderExpander borderExpander = new EconomyBorderExpander(configManager.getEconomyConfig(), configManager.getWorldConfig());
        this.commands = Arrays.asList(
                new BalanceCommand(borderExpander),
                new SendMoneyCommand(borderExpander),
                new ResetEconomyCommand(borderExpander)
        );
        this.listeners = Arrays.asList(
                new InitializeEconomyListener(borderExpander),
                new ChunkUpdateListener(borderExpander)
        );
    }
}

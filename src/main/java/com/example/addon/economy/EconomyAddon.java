package com.example.addon.economy;

import com.example.addon.PluginAddon;
import com.example.addon.economy.command.BalanceCommand;
import com.example.addon.economy.command.GrantLevelLoweringCommand;
import com.example.addon.economy.command.LowerBankLevelCommand;
import com.example.addon.economy.command.ResetEconomyCommand;
import com.example.addon.economy.command.SendMoneyCommand;
import com.example.addon.economy.listener.InitializeEconomyListener;
import com.example.config.ConfigManager;
import com.example.addon.economy.listener.ChunkUpdateListener;
import java.util.Arrays;

public class EconomyAddon extends PluginAddon {
    public EconomyAddon(ConfigManager configManager) {
        EconomyBorderExpander borderExpander = new EconomyBorderExpander(configManager.getEconomyConfig(), configManager.getWorldConfig());
        Bank bank = new Bank(configManager.getEconomyConfig());
        this.commands = Arrays.asList(
                new BalanceCommand(bank),
                new SendMoneyCommand(bank, borderExpander),
                new ResetEconomyCommand(bank),
                new LowerBankLevelCommand(bank, borderExpander),
                new GrantLevelLoweringCommand(configManager.getEconomyConfig())
        );
        this.listeners = Arrays.asList(
                new InitializeEconomyListener(borderExpander),
                new ChunkUpdateListener(bank, borderExpander)
        );
    }
}

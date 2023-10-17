package com.example.addon.economy;

import com.example.addon.PluginAddon;
import com.example.addon.economy.command.ResetEconomyCommand;
import com.example.addon.economy.listener.InitializeEconomyListener;
import com.example.config.ConfigManager;
import com.example.addon.economy.command.BalanceCommand;
import com.example.addon.economy.command.SendMoneyCommand;
import com.example.addon.economy.listener.ChunkUpdateListener;
import com.example.command.PPBCommand;
import org.bukkit.event.Listener;
import java.util.Arrays;
import java.util.List;

public class EconomyAddon implements PluginAddon {
    private final BalanceCommand balanceCommand;
    private final SendMoneyCommand sendMoneyCommand;
    private final ChunkUpdateListener chunkUpdateListener;
    private final InitializeEconomyListener initializeEconomyListener;
    private final ResetEconomyCommand resetEconomyCommand;

    public EconomyAddon(ConfigManager configManager, String extraBlocksKey) {
        this.balanceCommand = new BalanceCommand(configManager);
        this.sendMoneyCommand = new SendMoneyCommand(configManager, extraBlocksKey);
        this.resetEconomyCommand = new ResetEconomyCommand(configManager);
        this.initializeEconomyListener = new InitializeEconomyListener(configManager, extraBlocksKey);
        this.chunkUpdateListener = new ChunkUpdateListener(configManager, extraBlocksKey);
    }

    @Override
    public List<PPBCommand> getCommandHandlers() {
        return Arrays.asList(balanceCommand, sendMoneyCommand, resetEconomyCommand);
    }

    @Override
    public List<Listener> getListeners() {
        return Arrays.asList(initializeEconomyListener, chunkUpdateListener);
    }
}

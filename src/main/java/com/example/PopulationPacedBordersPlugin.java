package com.example;

import com.example.command.BalanceCommand;
import com.example.command.CommandExecutorHandler;
import com.example.command.ReloadCommand;
import com.example.command.SendMoneyCommand;
import com.example.config.Configurator;
import com.example.config.ConfigManager;
import com.example.eventlistener.PlayerJoinListener;
import com.example.eventlistener.ServerListPingListener;
import com.example.util.WorldBorderUtil;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public class PopulationPacedBordersPlugin extends JavaPlugin {
    private final Configurator configurator = new Configurator(super::saveConfig, this::reloadConfig, this::getConfig);
    private final WorldBorderUtil worldBorderUtil = new WorldBorderUtil(this::getLogger, this::getWorldByName);
    private final ConfigManager configManager = new ConfigManager(configurator, worldBorderUtil, this::getLogger);
    private final CommandExecutorHandler commandExecutorHandler = new CommandExecutorHandler();

    @Override
    public void onEnable() {
        configManager.resizeWorldBorder();
        PluginCommand ppbCommand = Objects.requireNonNull(getCommand("ppb"));
        ppbCommand.setExecutor(commandExecutorHandler);
        registerEvent(new PlayerJoinListener(configManager));
        registerEvent(new ServerListPingListener(configManager));
        registerCommand(new ReloadCommand(configManager));
        registerCommand(new SendMoneyCommand(configManager));
        registerCommand(new BalanceCommand(configManager));
    }

    @Override
    public void saveConfig() {
        configManager.save();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    private World getWorldByName(String worldName) {
        return getServer().getWorld(worldName);
    }

    private void registerEvent(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private void registerCommand(Command command) {
        commandExecutorHandler.addCommand(command);
    }
}

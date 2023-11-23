package com.example;

import com.example.addon.PluginAddon;
import com.example.addon.economy.EconomyAddon;
import com.example.addon.extender.ExtenderAddon;
import com.example.addon.maxonline.MaxOnlineAddon;
import com.example.addon.motd.MotdAddon;
import com.example.command.CommandExecutorHandler;
import com.example.command.PPBCommand;
import com.example.command.ReloadCommand;
import com.example.config.Configurator;
import com.example.config.ConfigManager;
import com.example.event.InitializeEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PopulationPacedBordersPlugin extends JavaPlugin {
    private final Configurator configurator = new Configurator(super::saveConfig, this::reloadConfig, this::getConfig, this::getLogger);
    private final CommandExecutorHandler commandExecutorHandler = new CommandExecutorHandler();
    private final ConfigManager configManager = new ConfigManager(configurator);

    @Override
    public void onEnable() {
        PluginCommand ppbCommand = Objects.requireNonNull(getCommand("ppb"));
        ppbCommand.setExecutor(commandExecutorHandler);
        ppbCommand.setTabCompleter(commandExecutorHandler);
        registerAddons(Arrays.asList(
                new MaxOnlineAddon(configManager),
                new EconomyAddon(configManager),
                new MotdAddon(configManager),
                new ExtenderAddon(configManager))
        );
        registerCommand(new ReloadCommand(configManager));
        Bukkit.getPluginManager().callEvent(new InitializeEvent());
    }

    @Override
    public void saveConfig() {
        configManager.save();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    private void registerAddons(List<PluginAddon> addons) {
        addons.forEach((PluginAddon addon) -> {
            addon.getListeners().forEach(this::registerEvent);
            addon.getCommandHandlers().forEach(this::registerCommand);
        });
    }

    private void registerEvent(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private void registerCommand(PPBCommand command) {
        commandExecutorHandler.addCommand(command);
    }
}

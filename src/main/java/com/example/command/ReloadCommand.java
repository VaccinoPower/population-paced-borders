package com.example.command;

import com.example.config.ConfigManager;
import com.example.event.InitializeEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.logging.Level;

public class ReloadCommand extends PPBCommand {
    private final ConfigManager configManager;

    public ReloadCommand(ConfigManager configManager) {
        super("reload");
        super.setPermission("ppb.command.reload");
        this.description = "Reload Plugin.";
        this.usageMessage = "Usage: /ppb reload";
        this.configManager = configManager;
        addArgsRule(args -> args.length == 0, super.getUsage());
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        configManager.reload();
        Bukkit.getPluginManager().callEvent(new InitializeEvent());
        sendOk(sender, "Configuration reloaded.");
        configManager.getLogger().log(Level.INFO, MessageFormat.format("{0} reloaded configuration.", sender.getName()));
    }
}

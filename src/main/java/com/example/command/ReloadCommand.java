package com.example.command;

import com.example.config.ConfigManager;
import com.example.event.InitializeEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends PPBCommand {
    private final ConfigManager configManager;

    public ReloadCommand(ConfigManager configManager) {
        super("reload");
        super.setPermission("ppb.command.reload");
        this.description = "Reload Plugin.";
        this.usageMessage = "Usage: /ppb reload";
        this.configManager = configManager;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args) {
        if (!isCommand(command)) {
            return false;
        }
        if (!isAvailable(sender, args, 0)) {
            return true;
        }
        configManager.reload();
        Bukkit.getPluginManager().callEvent(new InitializeEvent());
        return true;
    }
}

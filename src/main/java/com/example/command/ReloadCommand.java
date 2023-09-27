package com.example.command;

import com.example.config.ConfigManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends PPBCommand {
    private final ConfigManager config;

    public ReloadCommand(ConfigManager config) {
        super("reload");
        super.setPermission("ppb.command.reload");
        this.description = "Reload Plugin.";
        this.usageMessage = "Usage: /ppb reload";
        this.config = config;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args) {
        if (!isCommand(command)) {
            return false;
        }
        if (!isAvailable(sender, args, 0)) {
            return true;
        }
        config.reload();
        return true;
    }
}

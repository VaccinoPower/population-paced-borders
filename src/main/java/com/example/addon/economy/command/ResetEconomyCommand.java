package com.example.addon.economy.command;

import com.example.addon.economy.EconomyConfig;
import com.example.command.PPBCommand;
import com.example.config.ConfigManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ResetEconomyCommand extends PPBCommand {
    private final EconomyConfig economyConfig;

    public ResetEconomyCommand(ConfigManager configManager) {
        super("balreset");
        super.setPermission("ppb.command.balreset");
        this.description = "View balance for expansion.";
        this.usageMessage = "Usage:\n/ppb balreset";
        this.economyConfig = configManager.getEconomyConfig();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args) {
        if (!isCommand(command)) {
            return false;
        }
        if (!isAvailable(sender, args, 0)) {
            return true;
        }
        economyConfig.setBankLevel(0);
        economyConfig.setBalance(0);
        return true;
    }
}

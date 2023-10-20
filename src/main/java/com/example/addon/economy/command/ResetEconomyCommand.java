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
        addArgsRule(args -> args.length == 0, super.getUsage());
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        economyConfig.resetBank();
    }
}

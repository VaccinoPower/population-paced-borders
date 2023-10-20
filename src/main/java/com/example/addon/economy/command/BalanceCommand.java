package com.example.addon.economy.command;

import com.example.config.ConfigManager;
import com.example.addon.economy.EconomyConfig;
import com.example.command.PPBCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;

import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public class BalanceCommand extends PPBCommand {
    private final EconomyConfig economyConfig;

    public BalanceCommand(ConfigManager configManager) {
        super("balance");
        super.setPermission("ppb.command.balance");
        super.setAliases(Collections.singletonList("bal"));
        this.description = "View balance for expansion.";
        this.usageMessage = "Usage:\n/ppb balance\n/ppb bal";
        this.economyConfig = configManager.getEconomyConfig();
        addArgsRule(args -> args.length == 0, super.getUsage());
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        String message = economyConfig.aboutBalance();
        sender.sendMessage(Component.text(message, YELLOW));
    }
}

package com.example.addon.economy.command;

import com.example.addon.economy.EconomyBorderExpander;
import com.example.command.PPBCommand;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LowerBankLevelCommand extends PPBCommand {
    private final EconomyBorderExpander borderExpander;

    public LowerBankLevelCommand(EconomyBorderExpander borderExpander) {
        super("bank");
        super.setPermission("ppb.command.bank-down");
        this.description = "View balance for expansion.";
        this.usageMessage = "Usage:\n/ppb bank down\n/ppb bank down <amount>";
        this.borderExpander = borderExpander;
        addArgsRule(args -> args.length > 0, super.getUsage());
        addArgsRule(args -> args.length < 3, super.getUsage());
        addArgsRule(args -> "down".equals(args[0]), super.getUsage());
        addArgsRule(args -> args.length < 2 || NumberUtils.isDigits(args[1]), super.getUsage());
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {

    }
}

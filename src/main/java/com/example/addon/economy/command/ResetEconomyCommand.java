package com.example.addon.economy.command;

import com.example.addon.economy.Bank;
import com.example.command.PPBCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ResetEconomyCommand extends PPBCommand {
    private final Bank bank;

    public ResetEconomyCommand(Bank bank) {
        super("balreset");
        super.setPermission("ppb.command.balreset");
        this.description = "View balance for expansion.";
        this.usageMessage = "Usage:\n/ppb balreset";
        this.bank = bank;
        addArgsRule(args -> args.length == 0, super.getUsage());
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        bank.reset();
    }
}

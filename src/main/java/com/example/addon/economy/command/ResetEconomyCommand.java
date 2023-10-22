package com.example.addon.economy.command;

import com.example.addon.economy.EconomyBorderExpander;
import com.example.command.PPBCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ResetEconomyCommand extends PPBCommand {
    private final EconomyBorderExpander borderExpander;

    public ResetEconomyCommand(EconomyBorderExpander borderExpander) {
        super("balreset");
        super.setPermission("ppb.command.balreset");
        this.description = "View balance for expansion.";
        this.usageMessage = "Usage:\n/ppb balreset";
        this.borderExpander = borderExpander;
        addArgsRule(args -> args.length == 0, super.getUsage());
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        borderExpander.resetBank();
    }
}

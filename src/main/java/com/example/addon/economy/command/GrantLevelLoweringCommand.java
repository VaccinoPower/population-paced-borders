package com.example.addon.economy.command;

import com.example.addon.economy.EconomyBorderExpander;
import com.example.command.PPBCommand;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GrantLevelLoweringCommand extends PPBCommand {
    private final EconomyBorderExpander borderExpander;

    public GrantLevelLoweringCommand(EconomyBorderExpander borderExpander) {
        super("give-permission");
        super.setPermission("ppb.command.give-permission.level-lowering");
        this.description = "View balance for expansion.";
        this.usageMessage = "Usage:\n/ppb give-permission level-lowering <nickname> <amount>\n";
        this.borderExpander = borderExpander;
        addArgsRule(args -> args.length == 4, super.getUsage());
        addArgsRule(args -> "level-lowering".equals(args[0]), super.getUsage());
        addArgsRule(args -> NumberUtils.isDigits(args[2]), "Amount of level-lowering must be positive number.");
        
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {

    }
}

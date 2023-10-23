package com.example.addon.economy.command;

import com.example.addon.economy.EconomyBorderExpander;
import com.example.command.PPBCommand;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class GrantLevelLoweringCommand extends PPBCommand {
    private final EconomyBorderExpander borderExpander;

    public GrantLevelLoweringCommand(EconomyBorderExpander borderExpander) {
        super("permission");
        super.setPermission("ppb.command.permission.add.bank-down");
        super.setAliases(Collections.singletonList("perm"));
        this.description = "View balance for expansion.";
        this.usageMessage = "Usage:\n" +
                "/ppb permission add bank_down <nickname> <amount>\n" +
                "/ppb perm add bank_down <nickname> <amount>";
        this.borderExpander = borderExpander;
        addArgsRule(args -> args.length == 4, super.getUsage());
        addArgsRule(args -> "add".equals(args[0]), super.getUsage());
        addArgsRule(args -> "bank_down".equals(args[1]), super.getUsage());
        addArgsRule(args -> NumberUtils.isDigits(args[3]), "Amount of level-lowering must be not negative number.");
        addArgsRule(args -> getLevel(args) >= 0, "Amount of level-lowering must be not negative number.");
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        Player player = getPlayer(args);
        if (player != null) {
            borderExpander.addLowering(player, getLevel(args));
        }
    }

    private static Player getPlayer(String[] args) {
        return Bukkit.getPlayer(args[2]);
    }

    private static int getLevel(String[] args) {
        return Integer.parseInt(args[3]);
    }
}

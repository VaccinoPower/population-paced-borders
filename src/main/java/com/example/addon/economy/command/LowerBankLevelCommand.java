package com.example.addon.economy.command;

import com.example.addon.economy.Bank;
import com.example.addon.economy.EconomyBorderExpander;
import com.example.command.PPBCommand;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LowerBankLevelCommand extends PPBCommand {
    private static final String BANK_DOWN_ADMIN_PERMISSION = "ppb.command.bank-down.admin";
    private final Bank bank;
    private final EconomyBorderExpander borderExpander;

    public LowerBankLevelCommand(Bank bank, EconomyBorderExpander borderExpander) {
        super("bank");
        super.setPermission("ppb.command.bank-down;" + BANK_DOWN_ADMIN_PERMISSION);
        this.description = "View balance for expansion.";
        this.usageMessage = "Usage:\n/ppb bank down\n/ppb bank down <amount>";
        this.bank = bank;
        this.borderExpander = borderExpander;
        addArgsRule(args -> args.length > 0, super.getUsage());
        addArgsRule(args -> args.length < 3, super.getUsage());
        addArgsRule(args -> "down".equals(args[0]), super.getUsage());
        addArgsRule(args -> args.length < 2 || NumberUtils.isDigits(args[1]), super.getUsage());
        addArgsRule(args -> getLevels(args) > 0, "Amount of level-lowering must be positive number.");
        addSenderRule(sender -> isAdmin(sender) || getPlayer(sender) != null, "This user cannot use the command.");
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        if (isAdmin(sender)) {
            bank.levelDown(getLevels(args));
        } else {
            bank.levelDown(getPlayer(sender), getLevels(args));
        }
        bank.calculateExpansive();
        borderExpander.expand();
    }

    private static Player getPlayer(CommandSender sender) {
        return sender instanceof Player ? (Player)sender : null;
    }

    private static int getLevels(String[] args) {
        return args.length == 1 ? 1 : Integer.parseInt(args[1]);
    }

    private static boolean isAdmin(CommandSender sender) {
        return sender.hasPermission(BANK_DOWN_ADMIN_PERMISSION) || sender.isOp() || sender instanceof ConsoleCommandSender;
    }
}

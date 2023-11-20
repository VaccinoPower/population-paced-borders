package com.example.addon.economy.command;

import com.example.addon.economy.Bank;
import com.example.addon.economy.EconomyBorderExpander;
import com.example.command.PPBCommand;
import com.example.exeption.InvalidFormulaException;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.logging.Level;

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
        addArgsRule(LowerBankLevelCommand::isPositiveLevel, "Amount of level-lowering must be positive number.");
        addSenderRule(LowerBankLevelCommand::isValidUser, "This user cannot use the command.");
        addRule(this::hasLevel, "Not enough levels to decreased bank level.");
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        try {
            int bankLevel = bank.getLevel();
            bankDown(sender, args);
            if (bankLevel == bank.getLevel()) {
                sendOk(sender, "Bank level not changed: already minimum level.");
                return;
            }
            log(sender);
            bankLevel = bank.getLevel();
            bank.calculateExpansive();
            if (bankLevel == bank.getLevel()) {
                sendOk(MessageFormat.format("Bank level has decreased to {0}.", bankLevel));
            } else {
                sendOk(MessageFormat.format("Bank level has decreased to {0} and recalculated to {1}.", bankLevel, bank.getLevel()));
                borderExpander.logWorldSizes();
            }
            borderExpander.expand();
        } catch (InvalidFormulaException e) {
            bank.getLogger().log(Level.WARNING, e.getMessage());
        }
    }

    private void bankDown(@NotNull CommandSender sender, @NotNull String[] args) {
        if (isAdmin(sender)) {
            bank.levelDown(getLevel(args));
        } else {
            bank.levelDown(getPlayer(sender), getLevel(args));
        }
    }

    private void log(CommandSender sender) {
        String logPattern = "{0} decreased bank level to {1} (after recalculation). {0} has {2} level-lowering remaining.";
        Object[] logParams = {sender.getName(), bank.getLevel(), isAdmin(sender) ? "infinite" : bank.getLevelLowering(getPlayer(sender))};
        bank.getLogger().log(Level.INFO, logPattern, logParams);
    }

    private boolean hasLevel(CommandSender sender, String[] args) {
        if (isAdmin(sender)) {
            return true;
        }
        Player player = getPlayer(sender);
        return player != null && bank.getLevelLowering(player) >= getLevel(args);
    }

    private static Player getPlayer(CommandSender sender) {
        return sender instanceof Player ? (Player)sender : null;
    }

    private static int getLevel(String[] args) {
        return args.length == 1 ? 1 : Integer.parseInt(args[1]);
    }

    private static boolean isAdmin(CommandSender sender) {
        return sender.hasPermission(BANK_DOWN_ADMIN_PERMISSION) || sender.isOp() || sender instanceof ConsoleCommandSender;
    }

    private static boolean isValidUser(CommandSender sender) {
        return isAdmin(sender) || getPlayer(sender) != null;
    }

    private static boolean isPositiveLevel(String[] args) {
        return (args.length < 2 || NumberUtils.isDigits(args[1])) && getLevel(args) > 0;
    }
}

package com.example.addon.economy.command;

import com.example.addon.economy.Bank;
import com.example.addon.economy.EconomyBorderExpander;
import com.example.command.PPBCommand;
import com.example.exeption.InvalidFormulaException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.text.MessageFormat;
import java.util.logging.Level;

public class LowerBankLevelCommand extends PPBCommand {
    private static final String COMMAND_NAME = "bank";
    private static final String BANK_DOWN_ADMIN_PERMISSION = "ppb.command.bank-down.admin";
    private final Bank bank;
    private final EconomyBorderExpander borderExpander;

    public LowerBankLevelCommand(Bank bank, EconomyBorderExpander borderExpander) {
        super(COMMAND_NAME, "ppb.command.bank-down;" + BANK_DOWN_ADMIN_PERMISSION, "Decrease bank level.");
        this.bank = bank;
        this.borderExpander = borderExpander;
        addArgsTemplate(new String[]{COMMAND_NAME, "down"}, "Decreases 1 bank level.");
        addArgsTemplate(new String[]{COMMAND_NAME, "down", AMOUNT_ARGUMENT}, "Decreases specified amount of the bank level.");
        addArgsRule(LowerBankLevelCommand::hasKeywords, getHelpMessage());
        addArgsRule(LowerBankLevelCommand::isPositiveLevel, Component.text("Amount of level-lowering must be positive number.", NamedTextColor.RED));
        addSenderRule(LowerBankLevelCommand::isValidUser, Component.text("This user cannot use the command.", NamedTextColor.RED));
        addRule(this::hasLevel, Component.text("Not enough levels to decreased bank level.", NamedTextColor.RED));
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
            bankLevel = bank.getLevel();
            bank.recalculate();
            borderExpander.expand();
            if (bankLevel == bank.getLevel()) {
                sendOk(MessageFormat.format("Bank level has decreased to {0}.", bankLevel));
            } else {
                sendOk(MessageFormat.format("Bank level has decreased to {0} and recalculated to {1}.", bankLevel, bank.getLevel()));
                borderExpander.logWorldSizes();
            }
            log(sender);
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

    private static boolean isValidLength(String[] args) {
        return args.length == 1 || args.length == 2;
    }

    private static boolean hasKeywords(String[] args) {
        return isValidLength(args) && "down".equals(args[0]);
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

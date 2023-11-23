package com.example.addon.economy.command;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.example.addon.economy.Bank;
import com.example.addon.economy.EconomyBorderExpander;
import com.example.command.PPBCommand;
import com.example.exeption.InvalidFormulaException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.logging.Level;

import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class SendMoneyCommand extends PPBCommand {
    private static final String COMMAND_NAME = "send";
    private final Bank bank;
    private final EconomyBorderExpander borderExpander;

    public SendMoneyCommand(Bank bank, EconomyBorderExpander borderExpander) {
        super(COMMAND_NAME, "ppb.command.send", "Send money for expansion.");
        this.bank = bank;
        this.borderExpander = borderExpander;
        addArgsTemplate(new String[]{COMMAND_NAME, AMOUNT_ARGUMENT}, "Sends specified amount of the money to the bank balance.");
        addArgsTemplate(new String[]{COMMAND_NAME}, "Sends all money to the bank balance.");
        addArgsRule(SendMoneyCommand::hasKeyWords, getHelpMessage());
        addRule(SendMoneyCommand::hasMoney, Component.text("Not enough money.", NamedTextColor.RED));
        addRule(SendMoneyCommand::isPositiveMoney, Component.text("Amount to pay must be positive.", NamedTextColor.RED));
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        try {
            BigDecimal money = getMoney(sender, args);
            int bankLevel = bank.getLevel();
            bank.recalculate(money.longValue());
            getUser(sender).takeMoney(money);
            log(sender, money);
            if (bankLevel != bank.getLevel()) {
                sendOk(MessageFormat.format("Bank level increased to {0}.", bank.getLevel()));
                borderExpander.expand();
                borderExpander.logWorldSizes();
            }
        } catch (InvalidFormulaException e) {
            sender.sendMessage(Component.text("[ERROR] Invalid formula. Please contact an admin.", RED));
        }
    }

    private void log(CommandSender sender, BigDecimal money) {
        String logPattern = "{0} send {1}. Bank level: {2}. Bank balance: {3}";
        Object[] logParams = {sender.getName(), money, bank.getLevel(), bank.getBalance()};
        bank.getLogger().log(Level.INFO, logPattern, logParams);
    }

    private static boolean isValidLength(String[] args) {
        return args.length == 0 || args.length == 1;
    }
    
    private static boolean hasKeyWords(String[] args) {
        return isValidLength(args) && (args.length == 0 || NumberUtils.isCreatable(args[0]));
    }

    private static boolean hasMoney(CommandSender sender, String[] args) {
        return getUser(sender).getMoney().subtract(getMoney(sender, args)).signum() != -1;
    }

    private static boolean isPositiveMoney(CommandSender sender, String[] args) {
        return getMoney(sender, args).signum() == 1;
    }

    private static User getUser(CommandSender sender) {
        return JavaPlugin.getPlugin(Essentials.class).getUser(sender.getName());
    }

    private static BigDecimal getMoney(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return getUser(sender).getMoney();
        }
        try {
            return new BigDecimal(args[0]);
        } catch (NumberFormatException e) {
            return BigDecimal.valueOf(-1);
        }
    }
}

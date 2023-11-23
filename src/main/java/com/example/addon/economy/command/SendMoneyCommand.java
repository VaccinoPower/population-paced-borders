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
        addArgsRule(SendMoneyCommand::hasKeyWords, getHelpMessage());
        addRule(SendMoneyCommand::hasMoney, Component.text("Not enough money.", NamedTextColor.RED));
        addArgsRule(SendMoneyCommand::isPositiveMoney, Component.text("Amount to pay must be positive.", NamedTextColor.RED));
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        try {
            BigDecimal money = getMoney(args);
            int bankLevel = bank.getLevel();
            bank.recalculate(money.intValue());
            getUser(sender).takeMoney(money);
            log(sender, args);
            if (bankLevel != bank.getLevel()) {
                sendOk(MessageFormat.format("Bank level increased to {0}.", bank.getLevel()));
                borderExpander.expand();
                borderExpander.logWorldSizes();
            }
        } catch (InvalidFormulaException e) {
            sender.sendMessage(Component.text("[ERROR] Invalid formula. Please contact an admin.", RED));
        }
    }

    private void log(CommandSender sender, String[] args) {
        String logPattern = "{0} send {1}. Bank level: {2}. Bank balance: {3}";
        Object[] logParams = {sender.getName(), getMoney(args), bank.getLevel(), bank.getBalance()};
        bank.getLogger().log(Level.INFO, logPattern, logParams);
    }

    private static boolean isValidLength(String[] args) {
        return args.length == 1;
    }
    
    private static boolean hasKeyWords(String[] args) {
        return isValidLength(args) && NumberUtils.isCreatable(args[0]);
    }

    private static boolean hasMoney(CommandSender sender, String[] args) {
        return getUser(sender).getMoney().subtract(getMoney(args)).signum() != -1;
    }

    private static boolean isPositiveMoney(String[] args) {
        return getMoney(args).signum() == 1;
    }

    private static User getUser(CommandSender sender) {
        return JavaPlugin.getPlugin(Essentials.class).getUser(sender.getName());
    }

    private static BigDecimal getMoney(String[] args) {
        if (args.length == 0) {
            return BigDecimal.valueOf(-1);
        }
        try {
            return new BigDecimal(args[0]);
        } catch (NumberFormatException e) {
            return BigDecimal.valueOf(-1);
        }
    }
}

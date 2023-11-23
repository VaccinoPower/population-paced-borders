package com.example.addon.economy.command;

import com.example.addon.economy.Bank;
import com.example.command.PPBCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;

import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public class BalanceCommand extends PPBCommand {
    private static final String COMMAND_NAME = "balance";
    private static final String COMMAND_ALIAS = "bal";
    private static final String COMMAND_DESCRIPTION = "States the current level and balance of the bank.";
    private final Bank bank;

    public BalanceCommand(Bank bank) {
        super(COMMAND_NAME, "ppb.command.balance", "Current state of the bank.");
        super.setAliases(Collections.singletonList(COMMAND_ALIAS));
        this.bank = bank;
        addArgsTemplate(new String[]{COMMAND_NAME}, COMMAND_DESCRIPTION);
        addArgsTemplate(new String[]{COMMAND_ALIAS}, COMMAND_DESCRIPTION);
        addArgsRule(BalanceCommand::isValidLength, getHelpMessage());
    }

    private static boolean isValidLength(String[] args) {
        return args.length == 0;
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        String message = bank.aboutBalance();
        sender.sendMessage(Component.text(message, YELLOW));
    }
}

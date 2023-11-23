package com.example.addon.economy.command;

import com.example.addon.economy.Bank;
import com.example.command.PPBCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.logging.Level;

public class ResetEconomyCommand extends PPBCommand {
    private static final String COMMAND_NAME = "balreset";
    private final Bank bank;

    public ResetEconomyCommand(Bank bank) {
        super(COMMAND_NAME, "ppb.command.balreset", "Bank level reset (price only).");
        this.bank = bank;
        addArgsTemplate(new String[]{COMMAND_NAME}, "Resets bank level.");
        addArgsRule(ResetEconomyCommand::isValidLength, getHelpMessage());
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        bank.reset();
        sendOk("Bank level and balance reset to 0.");
        log(sender);
    }

    private void log(CommandSender sender) {
        String logPattern = "{0} reset bank level. Bank level: {1}. Bank balance: {2}";
        Object[] logParams = {sender.getName(), bank.getLevel(), bank.getBalance()};
        bank.getLogger().log(Level.INFO, logPattern, logParams);
    }

    private static boolean isValidLength(String[] args) {
        return args.length == 0;
    }
}

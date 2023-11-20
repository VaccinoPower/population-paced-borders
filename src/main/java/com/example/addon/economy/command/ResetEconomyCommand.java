package com.example.addon.economy.command;

import com.example.addon.economy.Bank;
import com.example.command.PPBCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.logging.Level;

public class ResetEconomyCommand extends PPBCommand {
    private final Bank bank;

    public ResetEconomyCommand(Bank bank) {
        super("balreset");
        super.setPermission("ppb.command.balreset");
        this.description = "View balance for expansion.";
        this.usageMessage = "Usage:\n/ppb balreset";
        this.bank = bank;
        addArgsRule(args -> args.length == 0, super.getUsage());
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
}

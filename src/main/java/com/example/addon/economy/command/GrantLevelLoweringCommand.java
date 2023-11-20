package com.example.addon.economy.command;

import com.example.addon.economy.Bank;
import com.example.command.PPBCommand;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.logging.Level;

public class GrantLevelLoweringCommand extends PPBCommand {
    private final Bank bank;

    public GrantLevelLoweringCommand(Bank bank) {
        super("permission");
        super.setPermission("ppb.command.permission.add.bank-down");
        super.setAliases(Collections.singletonList("perm"));
        this.description = "View balance for expansion.";
        this.usageMessage = "Usage:\n" +
                "/ppb permission add bank_down <nickname> <amount>\n" +
                "/ppb perm add bank_down <nickname> <amount>";
        this.bank = bank;
        addArgsRule(args -> args.length == 4, super.getUsage());
        addArgsRule(args -> "add".equals(args[0]), super.getUsage());
        addArgsRule(args -> "bank_down".equals(args[1]), super.getUsage());
        addArgsRule(GrantLevelLoweringCommand::isPositiveLevel, "Amount of level-lowering must be positive number.");
        addArgsRule(args -> getTargetPlayer(args) != null, "Player not found");
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        Player targetPlayer = getTargetPlayer(args);
        bank.addLowering(targetPlayer, getLevel(args));
        int level = bank.getLevelLowering(targetPlayer);
        sendOk(sender, MessageFormat.format("{0} has {1} level-lowering now.", targetPlayer.getName(), level));
        sendOk(targetPlayer, MessageFormat.format("Your level-lowering has increased to {0}.", level));
        log(sender, args);
    }

    private void log(CommandSender sender, String[] args) {
        Player targetPlayer = getTargetPlayer(args);
        String logPattern = "{0} increased amount of level-lowering for {1}. {1} has {2} level-lowering remaining.";
        Object[] logParams = {sender.getName(), targetPlayer.getName(), bank.getLevelLowering(targetPlayer)};
        bank.getLogger().log(Level.INFO, logPattern, logParams);
    }

    private static Player getTargetPlayer(String[] args) {
        return Bukkit.getPlayer(args[2]);
    }

    private static int getLevel(String[] args) {
        return Integer.parseInt(args[3]);
    }

    private static boolean isPositiveLevel(String[] args) {
        return args.length > 3 && NumberUtils.isDigits(args[3]) && getLevel(args) > 0;
    }
}

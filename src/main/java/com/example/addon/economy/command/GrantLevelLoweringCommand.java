package com.example.addon.economy.command;

import com.example.addon.economy.Bank;
import com.example.command.PPBCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.logging.Level;

public class GrantLevelLoweringCommand extends PPBCommand {
    private static final String COMMAND_NAME = "permission";
    private static final String COMMAND_ALIAS = "perm";
    private static final String COMMAND_DESCRIPTION = "Grants the target player the specified amount of the permission for decrease bank level.";
    private final Bank bank;

    public GrantLevelLoweringCommand(Bank bank) {
        super(COMMAND_NAME, "ppb.command.permission.add.bank-down", "Grant permission for decrease bank level.");
        super.setAliases(Collections.singletonList(COMMAND_ALIAS));
        this.bank = bank;
        addArgsTemplate(new String[]{COMMAND_NAME, "add", "bank_down", PLAYER_ARGUMENT, AMOUNT_ARGUMENT}, COMMAND_DESCRIPTION);
        addArgsTemplate(new String[]{COMMAND_ALIAS, "add", "bank_down", PLAYER_ARGUMENT, AMOUNT_ARGUMENT}, COMMAND_DESCRIPTION);
        addArgsRule(GrantLevelLoweringCommand::hasKeywords, getHelpMessage());
        addArgsRule(GrantLevelLoweringCommand::isPositiveLevel, Component.text("Amount of level-lowering must be positive number.", NamedTextColor.RED));
        addArgsRule(GrantLevelLoweringCommand::isValidPlayer, Component.text("Player not found", NamedTextColor.RED));
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

    private static boolean isValidLength(String[] args) {
        return args.length == 4;
    }

    private static boolean hasKeywords(String[] args) {
        return isValidLength(args) && "add".equals(args[0]) && "bank_down".equals(args[1]);
    }

    private static boolean isValidPlayer(String[] args) {
        return getTargetPlayer(args) != null;
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

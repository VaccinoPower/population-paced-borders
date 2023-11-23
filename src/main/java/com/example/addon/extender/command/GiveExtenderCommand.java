package com.example.addon.extender.command;

import com.example.addon.extender.ExtenderConfig;
import com.example.addon.extender.ExtenderStack;
import com.example.command.PPBCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.logging.Level;

public class GiveExtenderCommand extends PPBCommand {
    private static final String COMMAND_NAME = "extender";
    private final ExtenderConfig config;

    public GiveExtenderCommand(ExtenderConfig config) {
        super(COMMAND_NAME, "ppb.command.give.extender", "Give border extender.");
        this.config = config;
        addArgsTemplate(new String[]{COMMAND_NAME, AMOUNT_ARGUMENT}, "Gives 1 extender of the specified level.");
        addArgsTemplate(new String[]{COMMAND_NAME, AMOUNT_ARGUMENT, AMOUNT_ARGUMENT}, "Gives extender of the specified level the specified amount (up to 64).");
        addArgsRule(GiveExtenderCommand::isValidLength, getHelpMessage());
        addArgsRule(GiveExtenderCommand::isValidLevel, Component.text("Extender level must be positive integer number.", NamedTextColor.RED));
        addArgsRule(GiveExtenderCommand::isValidExtendersAmount, Component.text("Extenders amount must be positive integer number up to 64.", NamedTextColor.RED));
        addSenderRule(Player.class::isInstance, Component.text("Only players are allowed to use this command.", NamedTextColor.RED));
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        Player player = (Player)sender;
        ItemStack extenders = new ExtenderStack(config.getMaterial(), getLevels(args), getExtendersAmount(args));
        HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(extenders);
        remainingItems.values().forEach(item -> player.getWorld().dropItem(player.getLocation(), item));
        sendOk(sender, MessageFormat.format("{0} extenders of level {1} given out.", getExtendersAmount(args), getLevels(args)));
        log(sender, args);
    }

    private void log(CommandSender sender, String[] args) {
        String logPattern = "{0} gave himself {1} extenders of level {2}";
        Object[] logParams = {sender.getName(), getExtendersAmount(args), getLevels(args)};
        config.getLogger().log(Level.INFO, logPattern, logParams);
    }

    private static boolean isValidLength(String[] args) {
        return args.length == 1 || args.length == 2;
    }

    private static int getLevels(String[] args) {
        return Integer.parseInt(args[0]);
    }

    private static int getExtendersAmount(String[] args) {
        return args.length == 1 ? 1 : Integer.parseInt(args[1]);
    }

    private static boolean isValidLevel(String[] args) {
        return NumberUtils.isDigits(args[0]) && getLevels(args) > 0;
    }

    private static boolean isValidExtendersAmount(String[] args) {
        if (args.length < 2) {
            return true;
        }
        if (!NumberUtils.isDigits(args[1])) {
            return false;
        }
        int amount = getExtendersAmount(args);
        return amount > 0 && amount < 65;
    }
}

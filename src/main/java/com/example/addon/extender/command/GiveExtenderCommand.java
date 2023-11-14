package com.example.addon.extender.command;

import com.example.addon.extender.ExtenderConfig;
import com.example.addon.extender.ExtenderStack;
import com.example.command.PPBCommand;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GiveExtenderCommand extends PPBCommand {
    private final ExtenderConfig config;

    public GiveExtenderCommand(ExtenderConfig config) {
        super("extender");
        super.setPermission("ppb.command.give.extender");
        this.description = "Give border extender.";
        this.usageMessage = "Usage:\n/ppb extender <lvl>\n/ppb extender <lvl> <amount>";
        this.config = config;
        addArgsRule(args -> args.length > 0, super.getUsage());
        addArgsRule(args -> args.length < 3, super.getUsage());
        addArgsRule(GiveExtenderCommand::isValidLevel, "Extender level must be positive number.");
        addArgsRule(GiveExtenderCommand::isValidExtendersAmount, "Extenders amount must be positive number up to 64.");
        addSenderRule(Player.class::isInstance, "Only players are allowed to use this command.");
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        Player player = (Player)sender;
        ItemStack extenders = new ExtenderStack(config.getMaterial(), getLevels(args), getExtendersAmount(args));
        HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(extenders);
        remainingItems.values().forEach(item -> player.getWorld().dropItem(player.getLocation(), item));
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

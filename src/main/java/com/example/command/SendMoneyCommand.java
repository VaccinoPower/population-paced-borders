package com.example.command;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.example.config.ConfigManager;
import com.example.exeption.InvalidFormulaException;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;

import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class SendMoneyCommand extends PPBCommand {
    private final ConfigManager config;

    public SendMoneyCommand(ConfigManager config) {
        super("send");
        super.setPermission("ppb.command.send");
        this.description = "Send money for expansion.";
        this.usageMessage = "Usage: /ppb send <amount>";
        this.config = config;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args) {
        if (!isCommand(command)) {
            return false;
        }
        if (!isAvailable(sender, args, 1)) {
            return true;
        }
        final BigDecimal money;
        try {
            money = new BigDecimal(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text(getUsage(), RED));
            return true;
        }
        final User user = JavaPlugin.getPlugin(Essentials.class).getUser(sender.getName());
        if (!checkMoney(user, money)) {
            return true;
        }
        try {
            config.pay(money);
            user.takeMoney(money);
        } catch (InvalidFormulaException e) {
            sender.sendMessage(Component.text("[ERROR] Invalid formula. Please contact an admin.", RED));
            return true;
        }
        return true;
    }

    private static boolean checkMoney(User user, BigDecimal money) {
        if (user.getMoney().subtract(money).signum() != 1) {
            user.sendMessage("Not enough money.");
            return false;
        }
        if (money.signum() != 1) {
            user.sendMessage("Amount to pay must be positive.");
            return false;
        }
        return true;
    }
}

package com.example.addon.economy.command;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.example.config.ConfigManager;
import com.example.addon.economy.EconomyConfig;
import com.example.command.PPBCommand;
import com.example.config.WorldConfig;
import com.example.exeption.InvalidFormulaException;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;
import java.util.Map;

import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class SendMoneyCommand extends PPBCommand {
    private final String extraBlocksKey;
    private final EconomyConfig economyConfig;
    private final WorldConfig worldConfig;

    public SendMoneyCommand(ConfigManager configManager, String extraBlocksKey) {
        super("send");
        super.setPermission("ppb.command.send");
        this.description = "Send money for expansion.";
        this.usageMessage = "Usage: /ppb send <amount>";
        this.economyConfig = configManager.getEconomyConfig();
        this.worldConfig = configManager.getWorldConfig();
        this.extraBlocksKey = extraBlocksKey;
        addArgsRule(args -> args.length == 1, super.getUsage());
        addArgsRule(args -> NumberUtils.isCreatable(args[0]), super.getUsage());
        addRule((sender, args) -> getUser(sender).getMoney().subtract(getMoney(args[0])).signum() != -1, "Not enough money.");
        addArgsRule(args -> getMoney(args[0]).signum() == 1, "Amount to pay must be positive.");
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        final BigDecimal money = getMoney(args[0]);
        final User user = getUser(sender);
        try {
            pay(money);
            user.takeMoney(money);
        } catch (InvalidFormulaException e) {
            sender.sendMessage(Component.text("[ERROR] Invalid formula. Please contact an admin.", RED));
        }
    }

    private static User getUser(CommandSender sender) {
        return JavaPlugin.getPlugin(Essentials.class).getUser(sender.getName());
    }

    private static BigDecimal getMoney(String number) {
        try {
            return new BigDecimal(number);
        } catch (NumberFormatException e) {
            return BigDecimal.valueOf(-1);
        }
    }

    private void pay(Number money) throws InvalidFormulaException {
        economyConfig.calculateExpansive(money.intValue());
        Map<String, Double> worldSizesMap = economyConfig.getWorldSizesMap(worldConfig.getWorlds());
        worldConfig.resizeWorlds(extraBlocksKey, worldSizesMap);
    }
}

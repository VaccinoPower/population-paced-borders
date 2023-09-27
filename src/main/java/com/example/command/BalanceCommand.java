package com.example.command;

import com.example.config.ConfigManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public class BalanceCommand extends PPBCommand {
    private final ConfigManager config;

    public BalanceCommand(ConfigManager config) {
        super("balance");
        super.setPermission("ppb.command.balance");
        super.setAliases(Collections.singletonList("bal"));
        this.description = "View balance for expansion.";
        this.usageMessage = "Usage:\n/ppb balance\n/ppb bal";
        this.config = config;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args) {
        if (!isCommand(command)) {
            return false;
        }
        if (!isAvailable(sender, args, 0)) {
            return true;
        }
        String message = config.getAboutBalance();

        sender.sendMessage(Component.text(message, YELLOW));
        return true;
    }
}

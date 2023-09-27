package com.example.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.format.NamedTextColor.RED;
public abstract class PPBCommand extends Command {
    protected PPBCommand(@NotNull String name) {
        super(name);
    }

    public boolean isCommand(String commandName) {
        return commandName.equals(getName()) || getAliases().contains(commandName);
    }

    public boolean isAvailable(CommandSender sender, String[] args, int expectedLength) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length != expectedLength) {
            sender.sendMessage(Component.text(getUsage(), RED));
            return false;
        }
        return true;
    }
}

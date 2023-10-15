package com.example.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class CommandExecutorHandler implements CommandExecutor {
    private final List<Command> subcommands = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!"ppb".equalsIgnoreCase(command.getName())) {
            return false;
        }
        String subcommandName = (args.length == 0) ? "" : args[0];
        String[] subcommandArgs = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
        for (Command subcommand : subcommands) {
            if (subcommand.execute(sender, subcommandName, subcommandArgs)) {
                return true;
            }
        }
        sender.sendMessage(Component.text("Unknown command.", RED));
        return true;
    }

    public void addCommand(Command command) {
        subcommands.add(command);
    }
}

package com.example.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class CommandExecutorHandler implements CommandExecutor, TabCompleter {
    private final List<PPBCommand> subcommands = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!"ppb".equalsIgnoreCase(command.getName())) {
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(getHelpMessage());
            return true;
        }
        String subcommandName = args[0];
        String[] subcommandArgs = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
        for (Command subcommand : subcommands) {
            if (subcommand.execute(sender, subcommandName, subcommandArgs)) {
                return true;
            }
        }
        sender.sendMessage(Component.text("Unknown command. Try /ppb", RED));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!"ppb".equalsIgnoreCase(command.getName())) {
            return null;
        }
        return subcommands.stream()
                .map(s -> s.tabComplete(sender, args))
                .flatMap(Collection::stream)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public void addCommand(PPBCommand command) {
        subcommands.add(command);
    }

    private Component getHelpMessage() {
        Component header = Component.text("Command Help: ", NamedTextColor.GOLD)
                .append(Component.text("/ppb", NamedTextColor.WHITE));
        Component description = Component.text("Description: ", NamedTextColor.GOLD)
                .append(Component.text("PopulationPacedBorders help.", NamedTextColor.WHITE));
        Component usage = Component.text("Usage(s):", NamedTextColor.GOLD);
        List<Component> usages = subcommands.stream()
                .flatMap(subcommand -> subcommand.getUsages().stream())
                .collect(Collectors.toList());
        for (Component u : usages) {
            usage = usage.appendNewline().append(u);
        }
        return header.appendNewline().append(description).appendNewline().append(usage);
    }
}

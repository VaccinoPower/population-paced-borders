package com.example.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static net.kyori.adventure.text.format.NamedTextColor.RED;
public abstract class PPBCommand extends Command {
    private final Validator<CommandSender, String[]> validator = new Validator<>();

    protected PPBCommand(@NotNull String name) {
        super(name);
        addSenderRule(this::testPermission, "");
    }

    @Override
    public final boolean execute(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args) {
        if (!isCommand(command)) {
            return false;
        }
        if (!validate(sender, args)) {
            return true;
        }
        action(sender, args);
        return true;
    }

    protected abstract void action(@NotNull CommandSender sender, @NotNull String[] args);

    protected static void sendOk(CommandSender sender, String msg) {
        sender.sendMessage(Component.text(msg, NamedTextColor.GREEN));
    }

    protected static void sendOk(String msg) {
        Bukkit.broadcast(Component.text(msg, NamedTextColor.GOLD));
    }

    protected final boolean isCommand(String commandName) {
        return commandName.equals(getName()) || getAliases().contains(commandName);
    }

    protected final void addSenderRule(Predicate<CommandSender> validationFunction, String errorMessage) {
        addRule((sender, ignored) -> validationFunction.test(sender), errorMessage);
    }

    protected final void addArgsRule(Predicate<String[]> validationFunction, String errorMessage) {
        addRule((ignored, args) -> validationFunction.test(args), errorMessage);
    }

    protected final void addRule(BiPredicate<CommandSender, String[]> validationFunction, String errorMessage) {
        validator.addRule(validationFunction, errorMessage);
    }

    private boolean validate(CommandSender sender, String[] args) {
        if (!validator.validate(sender, args)) {
            String errorMessage = validator.getErrorMessage();
            if (!(errorMessage == null || "".equals(errorMessage))) {
                sender.sendMessage(Component.text(errorMessage, RED));
            }
            return false;
        }
        return true;
    }
}

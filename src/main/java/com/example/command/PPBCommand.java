package com.example.command;

import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class PPBCommand extends Command {
    protected static final String PLAYER_ARGUMENT = "<player>";
    protected static final String AMOUNT_ARGUMENT = "<amount>";
    private static final List<String> AMOUNT_TEMPLATE = ImmutableList.of("1", "10", "100", "1000");
    private final Set<String[]> argsTemplate = new HashSet<>();
    private final Validator<CommandSender, String[]> validator = new Validator<>();
    private final List<Component> usages = new ArrayList<>();

    protected PPBCommand(@NotNull String name, @NotNull String permission, @NotNull String description) {
        super(name);
        super.setPermission(permission);
        super.setDescription(description);
        addSenderRule(this::testPermission, Component.empty());
    }

    public final List<Component> getUsages() {
        return new ArrayList<>(usages);
    }

    public final @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 0 || argsTemplate.isEmpty() || !testPermissionSilent(sender)) {
            return ImmutableList.of();
        }
        int position = args.length - 1;
        String lastWord = args[position];
        String[] startWords = Arrays.copyOfRange(args, 0, position);
        return argsTemplate.stream()
                .filter(template -> template.length >= args.length)
                .filter(template -> arePrevArgsSame(template, startWords, position))
                .flatMap(template -> getHints(sender, lastWord, template[position]).stream())
                .collect(Collectors.toList());
    }

    public final Component getHelpMessage() {
        Component header = Component.text("Command Help: ", NamedTextColor.GOLD)
                .append(Component.text("/ppb" + getName(), NamedTextColor.WHITE));
        Component description = Component.text("Description: ", NamedTextColor.GOLD)
                .append(Component.text(getDescription(), NamedTextColor.WHITE));
        Component usage = Component.text("Usage(s):", NamedTextColor.GOLD);
        for (Component u : usages) {
            usage = usage.appendNewline().append(u);
        }
        return header.appendNewline().append(description).appendNewline().append(usage);
    }

    @Override
    public final boolean execute(@NotNull CommandSender sender, @NotNull String commandName, @NotNull String[] args) {
        if (!isCommand(commandName)) {
            return false;
        }
        if (!validate(sender, args)) {
            return true;
        }
        action(sender, args);
        return true;
    }

    protected abstract void action(@NotNull CommandSender sender, @NotNull String[] args);

    protected final void addArgsTemplate(String[] template, String templateDescription) {
        if (template == null) {
            return;
        }
        argsTemplate.add(template);
        Component u = Component.text("/ppb ", NamedTextColor.WHITE);
        for (String t : template) {
            NamedTextColor color = PLAYER_ARGUMENT.equals(t) || AMOUNT_ARGUMENT.equals(t) ? NamedTextColor.YELLOW : NamedTextColor.WHITE;
            u = u.append(Component.text(t, color)).appendSpace();
        }
        usages.add(u.append(Component.text("- " + templateDescription, NamedTextColor.GOLD)));
    }

    protected static void sendOk(CommandSender sender, String msg) {
        sender.sendMessage(Component.text(msg, NamedTextColor.GREEN));
    }

    protected static void sendOk(String msg) {
        Bukkit.broadcast(Component.text(msg, NamedTextColor.GOLD));
    }

    protected final boolean isCommand(String commandName) {
        return commandName.equals(getName()) || getAliases().contains(commandName);
    }

    protected final void addSenderRule(Predicate<CommandSender> validationFunction, Component errorMessage) {
        addRule((sender, ignored) -> validationFunction.test(sender), errorMessage);
    }

    protected final void addArgsRule(Predicate<String[]> validationFunction, Component errorMessage) {
        addRule((ignored, args) -> validationFunction.test(args), errorMessage);
    }

    protected final void addRule(BiPredicate<CommandSender, String[]> validationFunction, Component errorMessage) {
        validator.addRule(validationFunction, errorMessage);
    }

    private boolean validate(CommandSender sender, String[] args) {
        if (validator.validate(sender, args)) {
            return true;
        }
        Component errorMessage = validator.getErrorMessage();
        if (!errorMessage.equals(Component.empty())) {
            sender.sendMessage(errorMessage);
        }
        return false;
    }

    private static List<String> getHints(CommandSender sender, String lastWord, String templateLastWord) {
        if (PLAYER_ARGUMENT.equals(templateLastWord)) {
            return getPlayers(sender);
        }
        if (AMOUNT_ARGUMENT.equals(templateLastWord)) {
            return AMOUNT_TEMPLATE;
        }
        if (templateLastWord.startsWith(lastWord)) {
            return  ImmutableList.of(templateLastWord);
        }
        return ImmutableList.of();
    }

    private static List<String> getPlayers(CommandSender sender) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> sender instanceof Player && ((Player)sender).canSee(player))
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    private static boolean arePrevArgsSame(String[] template, String[] startWords, int position) {
        String[] templateStartWords = Arrays.copyOfRange(template, 0, position);
        for (int i = 0; i < position; i++) {
            if (PLAYER_ARGUMENT.equals(templateStartWords[i])) {
                continue;
            }
            if (AMOUNT_ARGUMENT.equals(templateStartWords[i]) && !NumberUtils.isDigits(startWords[i])) {
                return false;
            }
            if (!AMOUNT_ARGUMENT.equals(templateStartWords[i]) && !templateStartWords[i].equals(startWords[i])) {
                return false;
            }
        }
        return true;
    }
}

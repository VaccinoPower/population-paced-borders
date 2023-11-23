package com.example.command;

import com.example.config.ConfigManager;
import com.example.event.InitializeEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.logging.Level;

public class ReloadCommand extends PPBCommand {
    private static final String COMMAND_NAME = "reload";
    private final ConfigManager configManager;

    public ReloadCommand(ConfigManager configManager) {
        super(COMMAND_NAME, "ppb.command.reload", "Reload this plugin.");
        this.configManager = configManager;
        addArgsTemplate(new String[]{COMMAND_NAME}, "Reloads this plugin.");
        addArgsRule(ReloadCommand::isValidLength, getHelpMessage());
    }

    @Override
    protected void action(@NotNull CommandSender sender, @NotNull String[] args) {
        configManager.reload();
        Bukkit.getPluginManager().callEvent(new InitializeEvent());
        sendOk(sender, "Configuration reloaded.");
        configManager.getLogger().log(Level.INFO, "{0} reloaded configuration.", sender.getName());
    }

    private static boolean isValidLength(String[] args) {
        return args.length == 0;
    }
}

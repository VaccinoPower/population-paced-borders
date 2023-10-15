package com.example.addon;

import com.example.command.PPBCommand;
import org.bukkit.event.Listener;

import java.util.List;

public interface PluginAddon {
    List<PPBCommand> getCommandHandlers();
    List<Listener> getListeners();
}

package com.example.addon;

import com.example.command.PPBCommand;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.List;

public abstract class PluginAddon {
    protected List<PPBCommand> commands = Collections.emptyList();
    protected List<Listener> listeners = Collections.emptyList();

    public final List<PPBCommand> getCommandHandlers() {
        return commands;
    }

    public final List<Listener> getListeners() {
        return listeners;
    }
}

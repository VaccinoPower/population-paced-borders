package com.example.config;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.function.Supplier;

public class Configurator {
    private final Savable configSaver;
    private final Loadable configLoader;
    private final Supplier<FileConfiguration> configSupplier;

    public Configurator(Savable configSaver, Loadable configLoader, Supplier<FileConfiguration> configSupplier) {
        this.configSaver = configSaver;
        this.configLoader = configLoader;
        this.configSupplier = configSupplier;
    }

    public void save() {
        configSaver.save();
    }

    public void load() {
        configLoader.load();
    }

    public FileConfiguration get() {
        return configSupplier.get();
    }
}

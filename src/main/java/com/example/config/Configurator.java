package com.example.config;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class Configurator {
    private final Savable configSaver;
    private final Loadable configLoader;
    private final Supplier<FileConfiguration> configSupplier;
    private final Supplier<Logger> logger;

    public Configurator(Savable configSaver, Loadable configLoader, Supplier<FileConfiguration> configSupplier, Supplier<Logger> logger) {
        this.configSaver = configSaver;
        this.configLoader = configLoader;
        this.configSupplier = configSupplier;
        this.logger = logger;
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

    public Supplier<Logger> getLogger() {
        return logger;
    }
}

package com.example.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

public abstract class AbstractConfig {
    private final Map<String, Object> cache = new HashMap<>();
    private final Configurator configurator;

    protected AbstractConfig(Configurator configurator) {
        this.configurator = configurator;
    }

    public final void save() {
        cache.forEach(get()::set);
        clearCache();
        configurator.save();
    }

    public final void reload() {
        clearCache();
        configurator.load();
    }

    public final Logger getLogger() {
        return configurator.getLogger().get();
    }

    protected final void setValue(ConfigKey configKey, Object value) {
        cache.put(configKey.key, value);
        save();
    }

    protected final boolean getBoolean(ConfigKey configKey) {
        return getValue(configKey, () -> get().getBoolean(configKey.key), Boolean.valueOf(configKey.defaultValue), Boolean.class);
    }

    protected final String getString(ConfigKey configKey) {
        return getValue(configKey, () -> get().getString(configKey.key), configKey.defaultValue, String.class);
    }

    protected final int getInt(ConfigKey configKey) {
        return getValue(configKey, () -> get().getInt(configKey.key), Integer.valueOf(configKey.defaultValue), Integer.class);
    }

    protected final double getDouble(ConfigKey configKey) {
        return getValue(configKey, () -> get().getDouble(configKey.key), Double.valueOf(configKey.defaultValue), Double.class);
    }

    protected final ConfigurationSection getConfigurationSection(ConfigKey configKey) {
        return getValue(configKey, () -> get().getConfigurationSection(configKey.key), null, ConfigurationSection.class);
    }

    private FileConfiguration get() {
        return configurator.get();
    }

    private void clearCache() {
        cache.clear();
    }

    private <T> T getValue(ConfigKey configKey, Supplier<Object> valueSupplier, T def, Class<T> clazz) {
        Object value = cache.computeIfAbsent(configKey.key, v -> valueSupplier.get());
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        return clazz.isInstance(configKey.defaultValue) ? clazz.cast(configKey.defaultValue) : def;
    }
}

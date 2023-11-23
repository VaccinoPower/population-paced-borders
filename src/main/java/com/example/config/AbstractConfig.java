package com.example.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
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

    protected final void setValue(String path, Object value) {
        cache.put(path, value);
        save();
    }

    protected final int getInt(String section, ConfigKey targetConfigKey) {
        String path = section + "." + targetConfigKey.key;
        return getValue(path, get()::getInt, Integer.valueOf(targetConfigKey.defaultValue), Integer.class);
    }

    protected final boolean getBoolean(ConfigKey configKey) {
        return getValue(configKey, get()::getBoolean, Boolean.valueOf(configKey.defaultValue), Boolean.class);
    }

    protected final String getString(String section, ConfigKey targetConfigKey) {
        String path = section + "." + targetConfigKey.key;
        return getValue(path,  get()::getString, targetConfigKey.defaultValue, String.class);
    }

    protected final String getString(ConfigKey configKey) {
        return getValue(configKey, get()::getString, configKey.defaultValue, String.class);
    }

    protected final int getInt(ConfigKey configKey) {
        return getValue(configKey, get()::getInt, Integer.valueOf(configKey.defaultValue), Integer.class);
    }

    protected final long getLong(ConfigKey configKey) {
        return getValue(configKey, get()::getLong, Long.valueOf(configKey.defaultValue), Long.class);
    }

    protected final double getDouble(String section, ConfigKey targetConfigKey) {
        String path = section + "." + targetConfigKey.key;
        return getValue(path,  get()::getDouble, Double.valueOf(targetConfigKey.defaultValue), Double.class);
    }

    protected final double getDouble(ConfigKey configKey) {
        return getValue(configKey, get()::getDouble, Double.valueOf(configKey.defaultValue), Double.class);
    }

    protected final ConfigurationSection getConfigurationSection(ConfigKey configKey) {
        return getValue(configKey, get()::getConfigurationSection, null, ConfigurationSection.class);
    }

    private FileConfiguration get() {
        return configurator.get();
    }

    private void clearCache() {
        cache.clear();
    }

    private <T> T getValue(ConfigKey configKey, Function<String, Object> getFromConfig, T def, Class<T> clazz) {
        return getValue(configKey.key, getFromConfig, def, clazz);
    }

    private <T> T getValue(String path, Function<String, Object> getFromConfig, T def, Class<T> clazz) {
        Object value = cache.computeIfAbsent(path, getFromConfig);
        return clazz.isInstance(value) ? clazz.cast(value) : def;
    }
}

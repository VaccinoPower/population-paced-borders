package com.example;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Set;

public class ConfigManager {
    private static final String KEY_MAX_ONLINE = "max_online";
    private static final String KEY_CHUNK_SIZE = "chunk_size";
    private static final String KEY_WORLDS = "worlds";
    private static final String KEY_DEFAULT_FORMULA = "default_formula";
    private static final String KEY_CHANGE_MOTD = "change_motd";
    private static final String KEY_WELCOME_MESSAGE = "welcome_message";
    private static final String KEY_WORLD_BORDER_MESSAGE = "world_border_message";
    private static final String KEY_BARRIER_FORMULA = "barrier_formula";
    private static final String ERROR_CONFIG_CHUNK_SIZE = "chunk size must be positive. chunk_size is: %s.";
    private static final String ERROR_CONFIG_WORLD_SECTION = "No worlds to change border.";
    private static final String KEY_MOTD_WORLD = "motd_world";
    private final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin, boolean useCopyDefaults) {
        this.plugin = plugin;
        getConfig().options().copyDefaults(useCopyDefaults);
        reloadConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public int getChunkSize() {
        return getConfig().getInt(KEY_CHUNK_SIZE, 16);
    }

    public Set<String> getWorlds() {
        return getWorldsSection().getKeys(false);
    }

    public String getBarrierFormula(String worldName) {
        return getWorldsSection().getString(String.format("%s.%s", worldName, KEY_BARRIER_FORMULA), getDefaultFormula());
    }

    public int getMaxOnline() {
        return getConfig().getInt(KEY_MAX_ONLINE, 1);
    }

    public void updateMaxOnline(int online) {
        if (online > getMaxOnline()) {
            plugin.getConfig().set(KEY_MAX_ONLINE, online);
            plugin.saveConfig();
        }
    }
    public boolean shouldChangeMotd() {
        return getConfig().getBoolean(KEY_CHANGE_MOTD, true);
    }

    public String getWelcomeMessage() {
        return getConfig().getString(KEY_WELCOME_MESSAGE, "More players = bigger world.");
    }

    public String getWorldBorderMessage() {
        return getConfig().getString(KEY_WORLD_BORDER_MESSAGE, "World border: %chunk_radius% chunks.");
    }

    public String getMotdWorld() {
        return getConfig().getString(KEY_MOTD_WORLD, "world");
    }
    public boolean validate() {
        if (getChunkSize() <= 0) {
            plugin.getLogger().severe(String.format(ERROR_CONFIG_CHUNK_SIZE, getChunkSize()));
            return false;
        }
        if (getWorldsSection() == null) {
            plugin.getLogger().severe(ERROR_CONFIG_WORLD_SECTION);
            return false;
        }
        return true;
    }

    private ConfigurationSection getWorldsSection() {
        return getConfig().getConfigurationSection(KEY_WORLDS);
    }

    private String getDefaultFormula() {
        return getConfig().getString(KEY_DEFAULT_FORMULA, "x");
    }
}

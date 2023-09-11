package com.example;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class PopulationPacedBordersPlugin extends JavaPlugin implements Listener {
    private int chunkSize;
    String defaultFormula;
    private ConfigurationSection worldsSection;
    private int maxOnline;
    private double borderRadius;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        loadConfig();
        if (!validate()) {
            return;
        }
        for (String worldName : worldsSection.getKeys(false)) {
            updateWorldSize(maxOnline, worldName);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        loadConfig();
        final int online = getServer().getOnlinePlayers().size();
        if (!validate() || online <= maxOnline) {
            updateMaxOnline(online);
            return;
        }
        updateMaxOnline(online);
        for (String worldName : worldsSection.getKeys(false)) {
            updateWorldSize(online, worldName);
        }
    }
    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        Component worldBorderInfo = Component.text()
                .content("More players = bigger world. World border: ")
                .append(Component.text((int)borderRadius, NamedTextColor.GOLD)
                        .decoration(TextDecoration.BOLD, true))
                .append(Component.text(" chunks.", NamedTextColor.GRAY))
                .build();
        event.motd(worldBorderInfo);
    }

    private boolean validate() {
        if (chunkSize <= 0) {
            getLogger().severe("chunk size must be positive. chunk_size is: \"" + chunkSize + "\". " +
                    "World border remained unchanged.");
            return false;
        }
        if (worldsSection == null) {
            getLogger().severe("No worlds to change border" +
                    "World border remained unchanged.");
            return false;
        }
        return true;
    }

    private void updateWorldSize(int online, String worldName) {
        World world = getServer().getWorld(worldName);
        if (world == null) {
            getLogger().severe("An error occurred while working with WorldBorder. " +
                    "World name \"" + worldName + "\"" + "doesn't exist.");
            return;
        }
        WorldBorder worldBorder = world.getWorldBorder();
        String formula = worldsSection.getString(worldName + ".barrier_formula", defaultFormula);
        double calculatedSize;
        try {
            calculatedSize = evaluateExpression(formula, online) * chunkSize;
        } catch (Exception exception) {
            getLogger().severe("There was an error in the formula:\"" + formula + "\". " +
                    "World border remained unchanged.\n" + exception.getMessage());
            return;
        }
        final double EPSILON = 1e-6;
        if (Math.abs(worldBorder.getSize() - calculatedSize) > EPSILON) {
            worldBorder.setSize(calculatedSize);
            if (worldName.equals("world")) {
                borderRadius = calculatedSize;
            }
        }
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        maxOnline = config.getInt("max_online", 1);
        chunkSize = config.getInt("chunk_size", 16);
        worldsSection = config.getConfigurationSection("worlds");
        defaultFormula = config.getString("default_formula", "x");
    }

    private void updateMaxOnline(int online) {
        if (online > maxOnline) {
            maxOnline = online;
            getConfig().set("max_online", maxOnline);
            saveConfig();
        }
    }

    private double evaluateExpression(String expression, double x) {
        Expression exp = new ExpressionBuilder(expression)
                .variables("x")
                .build()
                .setVariable("x", x);
        return exp.evaluate();
    }
}

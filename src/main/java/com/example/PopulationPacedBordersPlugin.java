package com.example;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import java.util.Set;

public class PopulationPacedBordersPlugin extends JavaPlugin implements Listener {
    private int chunkSize;
    String defaultFormula;
    private ConfigurationSection worldsSection;
    private int maxOnline;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
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
        final String startMessage = "More players = bigger world.\nWorld border: ";
        String defaultWorld = "world";
        World motdWorld = getMotdWorld(defaultWorld);
        int borderRadius = (int)(motdWorld.getWorldBorder().getSize() / 16);
        final Component chunkRadius = Component.text(borderRadius, NamedTextColor.GOLD)
                .decoration(TextDecoration.BOLD, true);
        final Component endMessage = Component.text(" chunks.", NamedTextColor.GRAY);
        Component worldBorderInfo = Component.text()
                .content(startMessage)
                .append(chunkRadius)
                .append(endMessage)
                .build();
        event.motd(worldBorderInfo);
    }

    private World getMotdWorld(String defaultWorld) {
        Set<String> configWorlds = worldsSection.getKeys(false);
        World motdWorld = getServer().getWorld(defaultWorld);
        if (motdWorld == null || !configWorlds.contains(defaultWorld)) {
            for (String world : configWorlds) {
                motdWorld = getServer().getWorld(world);
                if (motdWorld != null) {
                    return motdWorld;
                }
            }
        }
        return getServer().getWorlds().get(0);
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
        }
    }

    private void loadConfig() {
        maxOnline = getConfig().getInt("max_online", 1);
        chunkSize = getConfig().getInt("chunk_size", 16);
        worldsSection = getConfig().getConfigurationSection("worlds");
        defaultFormula = getConfig().getString("default_formula", "x");
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

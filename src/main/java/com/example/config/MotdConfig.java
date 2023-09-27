package com.example.config;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import java.util.function.Supplier;
import static com.example.config.ConfigKey.*;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public class MotdConfig extends AbstractConfig {
    private static final String CHUNK_RADIUS = "%chunk_radius%";
    private static final int CHUNK_SIZE = 16;

    public MotdConfig(Configurator configurator) {
        super(configurator);
    }

    public boolean shouldChangeMotd() {
        return getBoolean(CHANGE_MOTD);
    }

    public String getTopMessage() {
        return getString(TOP_MESSAGE);
    }

    public String getBottomMessage() {
        return getString(BOTTOM_MESSAGE);
    }

    public String getMotdWorld() {
        return getString(MOTD_WORLD);
    }
    public Component getMotdMessage() {
        String defaultMotdWorld = getMotdWorld();
        World motdWorld = chooseMotdWorld(defaultMotdWorld);
        int borderRadius = getWorldRadius(motdWorld);
        String topMessage = replaceRegex(this::getTopMessage, borderRadius);
        String bottomMessage = replaceRegex(this::getBottomMessage, borderRadius);
        return Component.text(topMessage + "\n", YELLOW)
                .append(Component.text(bottomMessage, GRAY).decoration(BOLD, true));
    }

    private int getWorldRadius(World motdWorld) {
        return (int)(motdWorld.getWorldBorder().getSize() / CHUNK_SIZE / 2);
    }

    private String replaceRegex(Supplier<String> message, int radius) {
        return message.get().replace(CHUNK_RADIUS, String.valueOf(radius));
    }

    private World chooseMotdWorld(String defaultWorld) {
        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getName().equals(defaultWorld)) {
                return world;
            }
        }
        return Bukkit.getServer().getWorlds().get(0);
    }
}

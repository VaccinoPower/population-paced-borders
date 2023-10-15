package com.example.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class WorldBorderUtil {

    private final Supplier<Logger> logger;

    public WorldBorderUtil(Supplier<Logger> logger){
        this.logger = logger;
    }

    public void updateWorlds(Map<String, Double> worldSizes) {
        worldSizes.forEach(this::updateWorld);
    }

    private void updateWorld(String worldName, Double size) {
        World world = worldProvider(worldName);
        if (world == null) {
            String message = "An error occurred while working with WorldBorder. World name \"" + worldName + "\" doesn't exist.";
            logger.get().warning(message);
            return;
        }
        world.getWorldBorder().setSize(size);
    }

    private static World worldProvider(String worldName) {
        return Bukkit.getServer().getWorld(worldName);
    }
}

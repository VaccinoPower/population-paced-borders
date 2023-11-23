package com.example.addon.maxonline.listener;

import com.example.addon.maxonline.MaxOnlineBorderExpander;
import com.example.event.BorderChangeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Level;

import static org.bukkit.Bukkit.getServer;

public class PlayerJoinListener implements Listener {
    private final MaxOnlineBorderExpander borderExpander;

    public PlayerJoinListener(MaxOnlineBorderExpander borderExpander) {
        this.borderExpander = borderExpander;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        int online = getServer().getOnlinePlayers().size();
        int maxOnline = borderExpander.getMaxOnline();
        double prevBlocks = getBorderRadius("world");
        borderExpander.expand(online);
        double curBlocks = getBorderRadius("world");
        if (maxOnline < borderExpander.getMaxOnline()) {
            log(event.getPlayer());
            sendOk("Server has exceeded the maximum online. In this regard, all worlds receive an additional blocks of world border radius!");
            borderExpander.logWorldSizes();
            Bukkit.getServer().getPluginManager().callEvent(new BorderChangeEvent((int)((curBlocks - prevBlocks) / 2)));
        }
    }

    private void log(Player player) {
        String logPattern = "{0} joined the server and maximum online changed to {1}.";
        Object[] logParams = {player.getName(), borderExpander.getMaxOnline()};
        borderExpander.getLogger().log(Level.INFO, logPattern, logParams);
    }

    private static void sendOk(String msg) {
        Bukkit.broadcast(Component.text(msg, NamedTextColor.GOLD));
    }

    private static double getBorderRadius(String worldName) {
        World world = Bukkit.getServer().getWorld(worldName);
        return world != null ? world.getWorldBorder().getSize() : 0;
    }
}

package com.example.addon.maxonline;

import com.example.addon.BorderExpander;
import com.example.config.WorldConfig;
import com.example.exeption.InvalidFormulaException;
import com.example.util.ExpressionCalculator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import java.util.logging.Logger;

public class MaxOnlineBorderExpander extends BorderExpander {
    private static final String EXTRA_BLOCKS_KEY = "maxOnline";

    public MaxOnlineBorderExpander(WorldConfig worldConfig) {
        super(EXTRA_BLOCKS_KEY, worldConfig);
    }

    public void expand(int online) {
        if (online > worldConfig.getMaxOnline()) {
            worldConfig.setMaxOnline(online);
            expand();
        }
    }

    private Logger getLogger() {
        return worldConfig.getLogger();
    }

    @Override
    protected Double getWorldSize(String worldName) {
        String formula = worldConfig.getFormula(worldName) + "*" + (2 * worldConfig.getChunkSize());
        int maxOnline = worldConfig.getMaxOnline();
        try {
            return ExpressionCalculator.evaluateExpression(formula, maxOnline);
        } catch (InvalidFormulaException e) {
            getLogger().warning("Invalid formula: " + formula);
            World world = Bukkit.getServer().getWorld(worldName);
            return world == null ? 0 : world.getWorldBorder().getSize();
        }
    }
}

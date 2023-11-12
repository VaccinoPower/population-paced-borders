package com.example.addon.maxonline;

import com.example.config.WorldConfig;
import com.example.exeption.InvalidFormulaException;
import com.example.util.ExpressionCalculator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MaxOnlineBorderExpander {
    private static final String EXTRA_BLOCKS_KEY = "maxOnline";
    private final WorldConfig worldConfig;

    public MaxOnlineBorderExpander(WorldConfig worldConfig) {
        this.worldConfig = worldConfig;
    }

    public void expand(int online) {
        if (online > worldConfig.getMaxOnline()) {
            worldConfig.setMaxOnline(online);
            expand();
        }
    }

    public void expand() {
        Map<String, Double> worldSizesMap = getWorldSizesMap();
        worldConfig.resizeWorlds(EXTRA_BLOCKS_KEY, worldSizesMap);
    }

    private Map<String, Double> getWorldSizesMap() {
        int maxOnline = worldConfig.getMaxOnline();
        HashMap<String, Double> worldSizesMap = new HashMap<>();
        for (String world : worldConfig.getWorlds()) {
            try {
                String formula = worldConfig.getFormula(world) + "*" + (2 * worldConfig.getChunkSize());
                double borderSize = ExpressionCalculator.evaluateExpression(formula, maxOnline);
                worldSizesMap.put(world, borderSize);
            } catch (InvalidFormulaException e) {
                getLogger().warning(e::getMessage);
            }
        }
        return worldSizesMap.size() != 0 ? worldSizesMap : Collections.emptyMap();
    }

    private Logger getLogger() {
        return worldConfig.getLogger();
    }
}

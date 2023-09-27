package com.example.util;

import com.example.exeption.InvalidFormulaException;
import com.example.exeption.WorldNotFoundException;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static com.example.util.ExpressionCalculator.evaluateExpression;

public class WorldBorderUtil {
    private static final double EPSILON = 1e-6;

    private final Supplier<Logger> logger;
    private final Function<String, World> worldProvider;

    public WorldBorderUtil(Supplier<Logger> logger, Function<String, World> worldProvider){
        this.logger = logger;
        this.worldProvider = worldProvider;
    }

    public void updateWorldSizes(Map<String, String> worldFormulaMap, int parameter) {
        worldFormulaMap.forEach((String worldName, String formula) -> {
            try {
                updateWorldSize(worldName, formula, parameter);
            } catch (WorldNotFoundException | InvalidFormulaException e) {
                logger.get().warning(e::getMessage);
            }
        });
    }

    private void updateWorldSize(String worldName, String formula, int parameter)
            throws WorldNotFoundException, InvalidFormulaException {
        World world = worldProvider.apply(worldName);
        if (world == null) {
            throw new WorldNotFoundException(
                    "An error occurred while working with WorldBorder. World name \"" + worldName + "\" doesn't exist.");
        }
        double calculatedSize = evaluateExpression(formula, parameter);
        WorldBorder worldBorder = world.getWorldBorder();
        if (Math.abs(worldBorder.getSize() - calculatedSize) > EPSILON) {
            worldBorder.setSize(calculatedSize);
        }
    }
}

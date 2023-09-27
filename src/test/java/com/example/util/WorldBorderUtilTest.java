package com.example.util;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;
import org.bukkit.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class WorldBorderUtilTest {
    private static final int INITIAL_WORLD_SIZE = 2;
    Map<String, String> worldFormulaMap = new HashMap<>();;
    Map<String, World> worlds = new HashMap<>();

    Logger logger = Logger.getLogger("logger");

    WorldBorderUtil worldBorderUtil = new WorldBorderUtil(() -> logger, worlds::get);

    @BeforeEach
    public void setUp() {
        MockBukkit.mock();
        worldBorderUtil = new WorldBorderUtil(() -> logger, worlds::get);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @MethodSource("updateWorldSizesProvider")
    @DisplayName("Test ")
    public void testUpdateWorldSizes(String[] worldNames) {
        String formula = "(x+1)*16";
        int x = 10;
        for (String worldName : worldNames) {
            WorldMock world = new WorldMock();
            world.setName(worldName);
            world.getWorldBorder().setSize(INITIAL_WORLD_SIZE);
            worlds.put(worldName, world);
            worldFormulaMap.put(worldName, formula);
        }
        worldBorderUtil.updateWorldSizes(worldFormulaMap, x);
        for (World world : worlds.values()) {
            Assertions.assertEquals((x + 1) * 16, world.getWorldBorder().getSize(), "");
        }
    }

    private static Stream<Arguments> updateWorldSizesProvider() {
        return Stream.of(
                Arguments.of((Object) new String[0]),
                Arguments.of((Object) new String[]{"worldB", "worldC", "worldA"})
        );
    }

    @Test
    @DisplayName("Test ")
    public void testUpdateWorldSizesWithInvalidFormula() {
        String[] worldNames = {"worldB", "worldC", "worldA"};
        String[] formulas = {"(x+1)*16", "invalid", "(x+1)*2"};
        int x = 10;
        for (int i = 0; i < worldNames.length; i++) {
            WorldMock world = new WorldMock();
            world.setName(worldNames[i]);
            world.getWorldBorder().setSize(INITIAL_WORLD_SIZE);
            worlds.put(worldNames[i], world);
            worldFormulaMap.put(worldNames[i], formulas[i]);
        }
        worldBorderUtil.updateWorldSizes(worldFormulaMap, x);
        Assertions.assertEquals((x + 1) * 16, worlds.get("worldB").getWorldBorder().getSize(), "");
        Assertions.assertEquals(INITIAL_WORLD_SIZE, worlds.get("worldC").getWorldBorder().getSize(), "");
        Assertions.assertEquals((x + 1) * 2, worlds.get("worldA").getWorldBorder().getSize(), "");
    }

    @Test
    @DisplayName("Test ")
    public void testUpdateWorldSizesWithNotExistsWorld() {
        String formula = "(x+1)*16";
        int x = 10;
        String[] worldNames = {"worldB", "worldC", "worldA"};
        for (String worldName : worldNames) {
            WorldMock world = new WorldMock();
            world.setName(worldName);
            world.getWorldBorder().setSize(INITIAL_WORLD_SIZE);
            worlds.put(worldName, world);
            worldFormulaMap.put(worldName, formula);
        }
        String fakeWorldName = "worldD";
        worldFormulaMap.put(fakeWorldName, formula);
        worldBorderUtil.updateWorldSizes(worldFormulaMap, x);
        for (World world : worlds.values()) {
            Assertions.assertEquals((x + 1) * 16, world.getWorldBorder().getSize(), "");
        }
    }

    @Test
    @DisplayName("Test ")
    public void testUpdateWorldSizesIfNewSizeEqualsPreviousSize() {
        String formula = String.valueOf(INITIAL_WORLD_SIZE);
        int x = 10;
        String[] worldNames = {"worldB", "worldC", "worldA"};
        for (String worldName : worldNames) {
            WorldMock world = new WorldMock();
            world.setName(worldName);
            world.getWorldBorder().setSize(INITIAL_WORLD_SIZE);
            worlds.put(worldName, world);
            worldFormulaMap.put(worldName, formula);
        }
        worldBorderUtil.updateWorldSizes(worldFormulaMap, x);
        for (World world : worlds.values()) {
            Assertions.assertEquals(INITIAL_WORLD_SIZE, world.getWorldBorder().getSize(), "");
        }
    }
}

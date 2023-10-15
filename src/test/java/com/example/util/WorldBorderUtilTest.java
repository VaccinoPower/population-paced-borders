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
    Map<String, Double> worldSizesMap = new HashMap<>();;
    Map<String, World> worlds = new HashMap<>();

    Logger logger = Logger.getLogger("getLogger");

    WorldBorderUtil worldBorderUtil;

    @BeforeEach
    public void setUp() {
        MockBukkit.mock();
        worldBorderUtil = new WorldBorderUtil(() -> logger);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @MethodSource("updateWorldSizesProvider")
    @DisplayName("Test ")
    public void testUpdateWorldSizes(String[] worldNames) {
        int x = 10;
        double formula = (x+1)*16;
        for (String worldName : worldNames) {
            WorldMock world = new WorldMock();
            world.setName(worldName);
            world.getWorldBorder().setSize(INITIAL_WORLD_SIZE);
            MockBukkit.getMock().addWorld(world);
            worlds.put(worldName, world);
            worldSizesMap.put(worldName, formula);
        }
        worldBorderUtil.updateWorlds(worldSizesMap);
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
    public void testUpdateWorldSizesWithNotExistsWorld() {
        int x = 10;
        double formula = (x+1)*16;
        String[] worldNames = {"worldB", "worldC", "worldA"};
        for (String worldName : worldNames) {
            WorldMock world = new WorldMock();
            world.setName(worldName);
            world.getWorldBorder().setSize(INITIAL_WORLD_SIZE);
            MockBukkit.getMock().addWorld(world);
            worlds.put(worldName, world);
            worldSizesMap.put(worldName, formula);
        }
        String fakeWorldName = "worldD";
        worldSizesMap.put(fakeWorldName, formula);
        worldBorderUtil.updateWorlds(worldSizesMap);
        for (World world : worlds.values()) {
            Assertions.assertEquals((x + 1) * 16, world.getWorldBorder().getSize(), "");
        }
    }

    @Test
    @DisplayName("Test ")
    public void testUpdateWorldSizesIfNewSizeEqualsPreviousSize() {
        String[] worldNames = {"worldB", "worldC", "worldA"};
        for (String worldName : worldNames) {
            WorldMock world = new WorldMock();
            world.setName(worldName);
            world.getWorldBorder().setSize(INITIAL_WORLD_SIZE);
            MockBukkit.getMock().addWorld(world);
            worlds.put(worldName, world);
            worldSizesMap.put(worldName, (double)INITIAL_WORLD_SIZE);
        }
        worldBorderUtil.updateWorlds(worldSizesMap);
        for (World world : worlds.values()) {
            Assertions.assertEquals(INITIAL_WORLD_SIZE, world.getWorldBorder().getSize(), "");
        }
    }
}

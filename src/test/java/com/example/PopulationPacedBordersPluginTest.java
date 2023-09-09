package com.example;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import org.bukkit.WorldBorder;
import org.junit.jupiter.api.*;

public class PopulationPacedBordersPluginTest {
    private final int CHUNK_SIZE = 16;
    private final int INITIAL_BORDER_SIZE = CHUNK_SIZE;
    private final String WORLD_NAME = "world";
    private ServerMock server;
    private WorldBorder worldBorder;
    private PopulationPacedBordersPlugin plugin;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(PopulationPacedBordersPlugin.class);
        WorldMock world = new WorldMock();
        world.setName(WORLD_NAME);
        server.addWorld(world);
        worldBorder = world.getWorldBorder();
        worldBorder.setSize(INITIAL_BORDER_SIZE);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test border expansion with one player")
    public void testBorderExpansionWithOnePlayer() {
        server.setPlayers(1);
        Assertions.assertEquals(CHUNK_SIZE, worldBorder.getSize(),
                "World border is not expanded correctly.");
    }

    @Test
    @DisplayName("Test border expansion with increasing players")
    public void testBorderExpansionWithIncreasingPlayers() {
        for (int numberOfPlayers = 2; numberOfPlayers < 64; numberOfPlayers++) {
            server.setPlayers(numberOfPlayers);
            Assertions.assertEquals(numberOfPlayers * CHUNK_SIZE, worldBorder.getSize(),
                    "World border is not expanded correctly.");
        }
    }

    @Test
    @DisplayName("Test border expansion with decreasing players")
    public void testBorderExpansionWithDecreasingPlayers() {
        server.setPlayers(64);
        server.setPlayers(63);
        Assertions.assertEquals(64 * CHUNK_SIZE, worldBorder.getSize(),
                "World border is not expanded correctly.");
    }

    @Test
    @DisplayName("Test that the border size remains unchanged with no players")
    public void testBorderSizeRemainsUnchangedWithNoPlayers() {
        server.setPlayers(0);
        Assertions.assertEquals(INITIAL_BORDER_SIZE, worldBorder.getSize(),
                "The border size should not change without players.");
    }
}

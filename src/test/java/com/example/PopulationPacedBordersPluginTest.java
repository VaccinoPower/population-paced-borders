package com.example;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import org.bukkit.plugin.java.JavaPlugin;
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
import java.util.stream.Stream;

public class PopulationPacedBordersPluginTest {
    private static final int CHUNK_SIZE = 16;
    private static final int MAX_PLAYERS = 64;
    private static final int INITIAL_BORDER_SIZE = CHUNK_SIZE;
    private final String[] WORLD_NAMES  = {"world", "world_nether", "world_the_end"};
    private ServerMock server;
    private JavaPlugin plugin;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(PopulationPacedBordersPlugin.class);
        for (String worldName : WORLD_NAMES) {
            plugin.getConfig().createSection("worlds." + worldName);
            plugin.getConfig().set("chunk_size", CHUNK_SIZE);
            WorldMock world = new WorldMock();
            world.setName(worldName);
            server.addWorld(world);
            world.getWorldBorder().setSize(INITIAL_BORDER_SIZE);
        }
        plugin.saveConfig();
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @MethodSource("playerCountProvider")
    @DisplayName("Test border expansion with different player counts")
    public void testBorderExpansionWithDifferentPlayerCounts(int playerCount, double expectedSize) {
        server.setPlayers(playerCount);
        for (String world : WORLD_NAMES) {
            Assertions.assertEquals(expectedSize, server.getWorld(world).getWorldBorder().getSize(),
                    "World border is not expanded correctly.");
        }
    }

    private static Stream<Arguments> playerCountProvider() {
        return Stream.of(
                Arguments.of(1, CHUNK_SIZE),
                Arguments.of(MAX_PLAYERS / 2, MAX_PLAYERS / 2 * CHUNK_SIZE),
                Arguments.of(MAX_PLAYERS, MAX_PLAYERS * CHUNK_SIZE),
                Arguments.of(0, INITIAL_BORDER_SIZE)
        );
    }

    @Test
    @DisplayName("Test border expansion with decreasing players")
    public void testBorderExpansionWithDecreasingPlayers() {
        server.setPlayers(MAX_PLAYERS);
        server.setPlayers(MAX_PLAYERS-1);
        for (String world : WORLD_NAMES) {
            Assertions.assertEquals(MAX_PLAYERS * CHUNK_SIZE, server.getWorld(world).getWorldBorder().getSize(),
                    "World border is not expanded correctly.");
        }
    }

    @ParameterizedTest
    @MethodSource("formulaProvider")
    @DisplayName("Test barrier formula")
    public void testBarrierFormula(String formula, int players, double expectedSize) {
        for (String world : WORLD_NAMES) {
            plugin.getConfig().set("worlds." + world + ".barrier_formula", formula);
        }
        server.setPlayers(players); // Установите желаемое количество игроков
        for (String world : WORLD_NAMES) {
            Assertions.assertEquals(expectedSize * CHUNK_SIZE, server.getWorld(world).getWorldBorder().getSize(),
                    "World border is not expanded correctly.");
        }
    }

    private static Stream<Arguments> formulaProvider() {
        final int PLAYERS = MAX_PLAYERS / 16;
        return Stream.of(
                Arguments.of("1", PLAYERS, 1),
                Arguments.of("1/3 + 1/3 + 1/3", PLAYERS, 1),
                Arguments.of("1.25 * x", PLAYERS, 1.25 * PLAYERS),
                Arguments.of("x * 2", PLAYERS, PLAYERS * 2),
                Arguments.of("x + 5", PLAYERS, PLAYERS + 5),
                Arguments.of("x - 1", PLAYERS, PLAYERS - 1),
                Arguments.of("x / 2", PLAYERS, PLAYERS / 2),
                Arguments.of("x ^ 2", PLAYERS, Math.pow(PLAYERS, 2)),
                Arguments.of("sqrt(x)", PLAYERS, Math.sqrt(PLAYERS)),
                Arguments.of("x * sin(x) * sin(x)", PLAYERS, PLAYERS * Math.sin(PLAYERS) * Math.sin(PLAYERS)),
                Arguments.of("x * cos(x) * cos(x)", PLAYERS, PLAYERS * Math.cos(PLAYERS) * Math.cos(PLAYERS)),
                Arguments.of("tan(x)", PLAYERS, Math.tan(PLAYERS)),
                Arguments.of("log(x)", PLAYERS, Math.log(PLAYERS)),
                Arguments.of("log10(x)", PLAYERS, Math.log10(PLAYERS)),
                Arguments.of("exp(x)", PLAYERS, Math.exp(PLAYERS)),
                Arguments.of("abs(x)", PLAYERS, Math.abs(-PLAYERS)),
                Arguments.of("10 * x * abs(sin(x) + cos(x))", PLAYERS, 10 * PLAYERS * Math.abs(Math.sin(PLAYERS) + Math.cos(PLAYERS)))
        );
    }

    @Test
    @DisplayName("Test barrier formula for different worlds")
    public void testBarrierFormulaForDifferentWorlds() {
        final int PLAYERS = MAX_PLAYERS / 16;

        // Наборы формул для каждого мира
        Map<String, String> formulas = new HashMap<>();

        formulas.put("world", "x * 2");
        formulas.put("world_nether", "x + 5");
        formulas.put("world_the_end", "x / 4");

        Map<String, Double> expectedSizes = new HashMap<>();
        expectedSizes.put("world", PLAYERS * 2.0);
        expectedSizes.put("world_nether", PLAYERS + 5.0);
        expectedSizes.put("world_the_end", PLAYERS / 4.0);

        for (Map.Entry<String, String> entry : formulas.entrySet()) {
            String world = entry.getKey();
            String formula = entry.getValue();
            plugin.getConfig().set("worlds." + world + ".barrier_formula", formula);
        }
        server.setPlayers(PLAYERS);
        for (Map.Entry<String, Double> entry : expectedSizes.entrySet()) {
            String world = entry.getKey();
            double expectedSize = entry.getValue();
            Assertions.assertEquals(expectedSize * CHUNK_SIZE, server.getWorld(world).getWorldBorder().getSize(),
                    "World border is not expanded correctly.");
        }
    }

    @ParameterizedTest
    @MethodSource("chunkSizeProvider")
    @DisplayName("Test border expansion with different chunk size")
    public void testChunkSize(int chunkSize, String formula, int players, double expectedSize) {
        plugin.getConfig().set("chunk_size", chunkSize);
        for (String world : WORLD_NAMES) {
            plugin.getConfig().set("worlds." + world + ".barrier_formula", formula);
        }
        server.setPlayers(players);
        for (String world : WORLD_NAMES) {
            Assertions.assertEquals(expectedSize * chunkSize, server.getWorld(world).getWorldBorder().getSize(),
                    "World border is not expanded correctly.");
        }
    }

    private static Stream<Arguments> chunkSizeProvider() {
        String formula = "2 ^ x";
        final int PLAYERS = MAX_PLAYERS / 16;
        return Stream.of(
                Arguments.of(1, formula, PLAYERS, Math.pow(2, PLAYERS)),
                Arguments.of(8, formula, PLAYERS, Math.pow(2, PLAYERS)),
                Arguments.of(16, formula, PLAYERS, Math.pow(2, PLAYERS))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidFormulaProvider")
    public void testInvalidFormula(String invalidFormula) {
        for (String world : WORLD_NAMES) {
            plugin.getConfig().set("worlds." + world + ".barrier_formula", invalidFormula);
        }
        server.setPlayers(MAX_PLAYERS);
        for (String world : WORLD_NAMES) {
            Assertions.assertEquals(INITIAL_BORDER_SIZE, server.getWorld(world).getWorldBorder().getSize(),
                    "World border is not expanded correctly.");
        }
    }

    private static Stream<Arguments> invalidFormulaProvider() {
        return Stream.of(
                Arguments.of("x * 2 +"),
                Arguments.of(""),
                Arguments.of(")("),
                Arguments.of("4)(4")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidChunkSizeProvider")
    public void testInvalidChunkSize(int invalidChunkSize) {
        plugin.getConfig().set("chunk_size", invalidChunkSize);
        server.setPlayers(MAX_PLAYERS);
        for (String world : WORLD_NAMES) {
            Assertions.assertEquals(INITIAL_BORDER_SIZE, server.getWorld(world).getWorldBorder().getSize(),
                    "World border is not expanded correctly.");
        }
    }

    private static Stream<Arguments> invalidChunkSizeProvider() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of(0)
        );
    }
}

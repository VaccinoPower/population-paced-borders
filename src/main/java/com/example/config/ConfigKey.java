package com.example.config;

public enum ConfigKey {
    MAX_ONLINE("max_online", "1"),
    CHUNK_SIZE("chunk_size", "16"),
    CHANGE_MOTD("change_motd", "false"),
    TOP_MESSAGE("top_message", "More player = bigger world."),
    BOTTOM_MESSAGE("bottom_message", "World border: %chunk_radius% chunks."),
    MOTD_WORLD("motd_world", "world"),
    WORLDS("worlds", null),
    BARRIER_FORMULA("barrier_formula", "x"),
    EXPANSION_BALANCE("expansion_balance", "0"),
    EXPANSION_FORMULA("expansion_formula", "x+1"),
    EXPANSION_BANK_LEVEL("bank_level", "0"),
    EXPANSION_BLOCKS_LEVEL("blocks_level", "0"),
    PLAYERS("players", null),
    NICKNAME("nickname", "???"),
    LEVEL_LOWERING("lowering_level", "0"),
    INITIAL_BARRIER_SIZE("initial_barrier_size", "0"),
    GEMS("gems", null),
    WORLD_EXPANDED_BY("gems.world-expanded-by", "0"),
    MATERIAL("gems.material", "EMERALD"),
    ITEMS("gems.items", null),
    EXPANSION_SIZE("expansion-size", "0"),
    DROP_CHANCE("drop-chance", "0"),
    ALLOWED_MOBS("gems.allowed-mobs", null);

    public final String key;
    public final String defaultValue;

    ConfigKey(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }
}

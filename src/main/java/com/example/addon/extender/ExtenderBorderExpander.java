package com.example.addon.extender;

import com.example.addon.BorderExpander;
import com.example.config.WorldConfig;

public class ExtenderBorderExpander extends BorderExpander {
    private static final String EXTRA_BLOCKS_KEY = "extender";
    private final ExtenderConfig extenderConfig;

    public ExtenderBorderExpander(ExtenderConfig extenderConfig, WorldConfig worldConfig) {
        super(EXTRA_BLOCKS_KEY, worldConfig);
        this.extenderConfig = extenderConfig;
    }

    public void expand(int size) {
        if (size > 0) {
            extenderConfig.addWorldExpanded(size);
            expand();
        }
    }

    @Override
    protected Double getWorldSize(String worldName) {
        return 2.0 * extenderConfig.getWorldExpandedBy();
    }
}

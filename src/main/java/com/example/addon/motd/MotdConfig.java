package com.example.addon.motd;

import com.example.config.AbstractConfig;
import com.example.config.Configurator;

import static com.example.config.ConfigKey.BOTTOM_MESSAGE;
import static com.example.config.ConfigKey.CHANGE_MOTD;
import static com.example.config.ConfigKey.MOTD_WORLD;
import static com.example.config.ConfigKey.TOP_MESSAGE;

public class MotdConfig extends AbstractConfig {
    public MotdConfig(Configurator configurator) {
        super(configurator);
    }

    public boolean shouldChangeMotd() {
        return getBoolean(CHANGE_MOTD);
    }

    public String getTopMessage() {
        return getString(TOP_MESSAGE);
    }

    public String getBottomMessage() {
        return getString(BOTTOM_MESSAGE);
    }

    public String getMotdWorld() {
        return getString(MOTD_WORLD);
    }
}

package com.example.config;

import static com.example.config.ConfigKey.MAX_ONLINE;

public class ServerLimitsConfig extends AbstractConfig {
    public ServerLimitsConfig(Configurator configurator) {
        super(configurator);
    }

    public int getMaxOnline() {
        return getInt(MAX_ONLINE);
    }

    public void setMaxOnline(int online) {
        setValue(MAX_ONLINE, online);
    }
}

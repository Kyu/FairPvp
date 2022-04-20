package me.preciouso.fairpvp;

import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {
    private static Configuration instance;

    private final double pvpTagTime;

    public Configuration(FileConfiguration configuration) {
        this.pvpTagTime = configuration.getDouble("pvp_tag_time");
    }

    public double getPvpTagTime() {
        return pvpTagTime;
    }

}

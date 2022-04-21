package me.preciouso.fairpvp;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ClearPvp extends BukkitRunnable {

    private final FairPvp plugin;

    public ClearPvp(FairPvp plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // Clear old pvp
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<UUID, Long>> iter = plugin.getHits().entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<UUID, Long> entry = iter.next();
            // 20 ticks x 1000 ms x n secs
            // Buggy if time is low
            if (now - entry.getValue() > 1000 * plugin.getPluginConfig().getPvpTagTime() ) {
                plugin.removeFromPvp(entry.getKey());
                iter.remove();
            }
        }

    }

}

package me.preciouso.fairpvp;

import me.preciouso.fairpvp.Utils.PlayerMessager;
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
            if (now - entry.getValue() > 10 * 1000) { // 10 secs
                plugin.removeFromPvp(entry.getKey());
                iter.remove();
            }
        }

    }

}

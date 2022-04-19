package me.preciouso.fairpvp;

import me.preciouso.fairpvp.Listeners.PlayerPvpListener;
import me.preciouso.fairpvp.Utils.PlayerPair;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.UUID;

public class FairPvp extends JavaPlugin {
    // TODO timer to remove from pvp after 60 seconds
    private PlayerPair pvpPlayers;

    @Override
    public void onEnable() {
        pvpPlayers = new PlayerPair();

        getServer().getPluginManager().registerEvents(new PlayerPvpListener(this), this);

        BukkitScheduler scheduler = getServer().getScheduler();
        new ClearPvp(this).runTaskTimer(this, 0, 20);
    }


    @Override
    public void onDisable() {}

    public UUID getPvpPartnerID(UUID playerId) {
        return pvpPlayers.get(playerId);
    }

    public boolean inPvp(UUID playerId) {
        return pvpPlayers.contains(playerId);
    }

    public void addPvpPair(UUID player, UUID partner) {
        pvpPlayers.put(player, partner);
    }

    public void updateHit(UUID id, long when) {
        pvpPlayers.updatePlayerHit(id, when);
    }

    public Long getLastHit(UUID id) {
        return pvpPlayers.getLastPlayerHit(id);
    }

    public void removeFromPvp(UUID id) {
        pvpPlayers.remove(id);
    }

    public HashMap<UUID, Long> getHits() {
        return pvpPlayers.getAllHits();
    }
}

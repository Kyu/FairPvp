package me.preciouso.fairpvp.Utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerPair {
    private final HashMap<UUID, UUID> map;
    private final HashMap<UUID, UUID> invertedMap;
    private final HashMap<UUID, Long> lastHit;

    public PlayerPair() {
        this.map = new HashMap<>();
        this.invertedMap = new HashMap<>();
        lastHit = new HashMap<>();
    }

    public void updatePlayerHit(UUID id, long when) {
        if (! map.containsKey(id) && invertedMap.containsKey(id)) { // First map only
            id = invertedMap.get(id);
            lastHit.put(id, when);
        }  else if (invertedMap.containsKey(id)) {
            id = invertedMap.get(id);
            lastHit.put(id, when);
        }
        // Possible source of big, overredundant?
    }

    public Long getLastPlayerHit(UUID id) {
        if (map.containsKey(id)) {
            return lastHit.get(id);
        } else {
            return lastHit.get(invertedMap.get(id));
        }
    }

    public HashMap<UUID, Long> getAllHits() {
        return lastHit;
    }

    public UUID get(UUID key) {
        UUID first = map.get(key);
        if (first != null) {
            return first;
        }

        return invertedMap.get(key); // null or else
    }

    public void put(UUID key, UUID value) {
        map.put(key, value);
        invertedMap.put(value, key);
    }

    public void remove(UUID key) {
        /*
        lastHit.remove(key);
        lastHit.remove(invertedMap.get(key));
         */

        PlayerMessager pm = new PlayerMessager();
        UUID key1;
        UUID key2;
        if (map.containsKey(key)) {
            key1 = key;
            key2 = map.get(key);
        } else  {
            key1 = invertedMap.get(key);
            key2 = key;
        }

        map.remove(key1);
        invertedMap.remove(key2);
        pm.sendPlayerMessageByUUID(key1, "You have left PvP mode!");
        pm.sendPlayerMessageByUUID(key2, "You have left PvP mode!");
    }



    public boolean contains(UUID key) {
        return map.containsKey(key) || invertedMap.containsKey(key);
    }


    
}

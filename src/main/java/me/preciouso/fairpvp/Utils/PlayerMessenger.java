package me.preciouso.fairpvp.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerMessenger {
    public void sendPlayerMessageByUUID(UUID id, String message) {
        Player p = Bukkit.getPlayer(id);
        if (p != null ) {
            p.sendMessage(message);
        }
    }
}

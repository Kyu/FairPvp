package me.preciouso.fairpvp.Listeners;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.preciouso.fairpvp.FairPvp;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class PlayerPvpListener implements Listener {
    FairPvp plugin;
    private final ArrayList<Material> ARMOR_TYPES = new ArrayList<>(
            Arrays.asList(
                    Material.NETHERITE_BOOTS, Material.NETHERITE_LEGGINGS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_HELMET,
                    Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET,
                    Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET,
                    Material.GOLDEN_BOOTS, Material.GOLDEN_LEGGINGS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET,
                    Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET,
                    Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET,
                    Material.TURTLE_HELMET
            )
    );

    public PlayerPvpListener(FairPvp plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        // If entities aren't Players
        if (!(e.getEntity() instanceof Player partner && e.getDamager() instanceof Player hitter)) {
            return;
        }

        // Check to see if players are partners
        // TODO make this bool check a function
        UUID potential_partner_id = plugin.getPvpPartnerID(hitter.getUniqueId());

        // If hitter and hitee aren't partners
        if (! (potential_partner_id != null && potential_partner_id == partner.getUniqueId())) {
            // If hitter is in pvp or if hitee is in pvp
            if (plugin.inPvp(hitter.getUniqueId()) && plugin.inPvp(partner.getUniqueId())) {
                Bukkit.broadcastMessage(hitter.getDisplayName() + " in pvp: " + plugin.inPvp(hitter.getUniqueId()) + " || " + partner.getDisplayName() + " in pvp: " + plugin.inPvp(partner.getUniqueId()));
                e.setCancelled(true);
            } else { // If neither are in pvp
                // Check with worldguard if hitter is allowed to pvp
                LocalPlayer localPlayerHitter = WorldGuardPlugin.inst().wrapPlayer(hitter);
                LocalPlayer localPlayerPartner = WorldGuardPlugin.inst().wrapPlayer(partner);

                // If hitter bypasses all restrictions
                boolean canPvp = WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayerHitter, localPlayerHitter.getWorld());

                if (!canPvp) {
                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionQuery query = container.createQuery();

                    // Test if player can build or pvp
                    canPvp = query.testState(localPlayerPartner.getLocation(), localPlayerHitter, Flags.PVP, Flags.BUILD);
                }
                // WorldGuard.getInstance().getPlatform().getSessionManager().
                if (canPvp) {
                    plugin.addPvpPair(hitter.getUniqueId(), partner.getUniqueId());
                    hitter.sendMessage("You have now entered PvP mode with " + partner.getDisplayName());
                    partner.sendMessage(hitter.getDisplayName() + " has now tagged you! You have entered PvP mode");
                    // e.setCancelled(false); -- unneeded
                } else {
                    e.setCancelled(true);
                }
            }
        } else {
            e.setCancelled(false);
            Long last = plugin.getLastHit(partner.getUniqueId());
            if (last != null) {
                double diff = ((double) System.currentTimeMillis() - (double) last)/1000;
                // hitter.sendMessage(diff + " seconds since you and " + partner.getDisplayName() + " last pvped!");
            }

            plugin.updateHit(potential_partner_id, System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onArmorDamageEvent(PlayerItemDamageEvent e) {
        if (plugin.inPvp(e.getPlayer().getUniqueId())) {
            e.setCancelled(ARMOR_TYPES.contains(e.getItem().getType()));
        }
    }
}

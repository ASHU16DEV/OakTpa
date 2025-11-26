package oaktpa.listeners;

import oaktpa.OakTpa;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    
    private final OakTpa plugin;
    
    public DeathListener(OakTpa plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        plugin.getDatabaseManager().saveDeathLocation(player, player.getLocation());
    }
}

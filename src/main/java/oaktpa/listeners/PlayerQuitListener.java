package oaktpa.listeners;

import oaktpa.OakTpa;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    
    private final OakTpa plugin;
    
    public PlayerQuitListener(OakTpa plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        plugin.removeGUIType(player.getUniqueId());
        
        if (plugin.getRequestManager().hasActiveRequest(player.getUniqueId())) {
            plugin.getRequestManager().removeRequest(player.getUniqueId());
        }
        
        java.util.List<oaktpa.models.TpaRequest> inboundRequests = 
                plugin.getRequestManager().getRequestsForTarget(player.getUniqueId());
        for (oaktpa.models.TpaRequest request : inboundRequests) {
            plugin.getRequestManager().removeRequest(request.getSenderUuid());
        }
    }
}

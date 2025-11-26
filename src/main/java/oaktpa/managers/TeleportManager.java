package oaktpa.managers;

import oaktpa.OakTpa;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportManager {
    
    private final OakTpa plugin;
    private final Map<String, TeleportTask> activeTeleports;
    
    public TeleportManager(OakTpa plugin) {
        this.plugin = plugin;
        this.activeTeleports = new HashMap<>();
    }
    
    public void scheduleTeleport(Player player, Location destination, Player other) {
        int delay = plugin.getConfigManager().getTeleportDelay();
        
        if (delay <= 0) {
            player.teleport(destination);
            return;
        }
        
        String taskKey = createTaskKey(player.getUniqueId(), other.getUniqueId());
        
        if (activeTeleports.containsKey(taskKey)) {
            activeTeleports.get(taskKey).cancel();
        }
        
        TeleportTask task = new TeleportTask(player, destination, other, delay, taskKey);
        activeTeleports.put(taskKey, task);
        task.start();
    }
    
    public void cancelTeleport(UUID playerId, UUID otherId) {
        String taskKey = createTaskKey(playerId, otherId);
        TeleportTask task = activeTeleports.remove(taskKey);
        if (task != null) {
            task.cancel();
        }
    }
    
    public boolean hasPendingTeleport(UUID playerId, UUID otherId) {
        String taskKey = createTaskKey(playerId, otherId);
        return activeTeleports.containsKey(taskKey);
    }
    
    private String createTaskKey(UUID player1, UUID player2) {
        return player1.toString() + ":" + player2.toString();
    }
    
    private class TeleportTask {
        private final Player player;
        private final Location destination;
        private final Player other;
        private final Location startLocation;
        private final String taskKey;
        private int countdown;
        private BukkitTask task;
        
        public TeleportTask(Player player, Location destination, Player other, int countdown, String taskKey) {
            this.player = player;
            this.destination = destination;
            this.other = other;
            this.startLocation = player.getLocation().clone();
            this.countdown = countdown;
            this.taskKey = taskKey;
        }
        
        public void start() {
            String warmupMsg = plugin.getConfigManager().getMessage("teleport-warmup", 
                    "player", other.getName(), "seconds", String.valueOf(countdown));
            player.sendMessage(plugin.getConfigManager().getPrefix() + warmupMsg);
            
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) {
                        cancel();
                        activeTeleports.remove(taskKey);
                        return;
                    }
                    
                    if (plugin.getConfigManager().isCancelOnMove() && hasMoved()) {
                        String cancelMsg = plugin.getConfigManager().getMessage("teleport-cancelled-moved");
                        player.sendMessage(plugin.getConfigManager().getPrefix() + cancelMsg);
                        cancel();
                        activeTeleports.remove(taskKey);
                        return;
                    }
                    
                    countdown--;
                    
                    if (countdown > 0) {
                        String countMsg = plugin.getConfigManager().getMessage("teleport-countdown", 
                                "seconds", String.valueOf(countdown));
                        player.sendMessage(plugin.getConfigManager().getPrefix() + countMsg);
                    } else {
                        player.teleport(destination);
                        String successMsg = plugin.getConfigManager().getMessage("teleport-success");
                        player.sendMessage(plugin.getConfigManager().getPrefix() + successMsg);
                        activeTeleports.remove(taskKey);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 20L, 20L);
        }
        
        private boolean hasMoved() {
            Location current = player.getLocation();
            return current.getBlockX() != startLocation.getBlockX() ||
                   current.getBlockY() != startLocation.getBlockY() ||
                   current.getBlockZ() != startLocation.getBlockZ();
        }
        
        public void cancel() {
            if (task != null) {
                task.cancel();
            }
        }
    }
    
    public void cancelAll() {
        for (TeleportTask task : activeTeleports.values()) {
            task.cancel();
        }
        activeTeleports.clear();
    }
}

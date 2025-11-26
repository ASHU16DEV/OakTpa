package oaktpa.managers;

import oaktpa.OakTpa;
import oaktpa.models.TpaRequest;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class RequestManager {
    
    private final OakTpa plugin;
    private final Map<UUID, TpaRequest> requestsBySender;
    private final Map<UUID, List<TpaRequest>> requestsByTarget;
    private BukkitTask cleanupTask;
    
    public RequestManager(OakTpa plugin) {
        this.plugin = plugin;
        this.requestsBySender = new HashMap<>();
        this.requestsByTarget = new HashMap<>();
    }
    
    public void createRequest(Player sender, Player target, boolean isTpaHere) {
        TpaRequest request = new TpaRequest(sender.getUniqueId(), target.getUniqueId(), isTpaHere);
        
        requestsBySender.put(sender.getUniqueId(), request);
        
        requestsByTarget.computeIfAbsent(target.getUniqueId(), k -> new ArrayList<>()).add(request);
        
        int timeout = plugin.getConfigManager().getRequestTimeout();
        request.setTimeoutTask(new BukkitRunnable() {
            @Override
            public void run() {
                if (hasActiveRequest(sender.getUniqueId())) {
                    removeRequest(sender.getUniqueId());
                    
                    if (sender.isOnline()) {
                        String msg = plugin.getConfigManager().getMessage("request-timeout-sender", "player", target.getName());
                        sender.sendMessage(plugin.getConfigManager().getPrefix() + msg);
                    }
                    
                    if (target.isOnline()) {
                        String msg = plugin.getConfigManager().getMessage("request-timeout-receiver", "player", sender.getName());
                        target.sendMessage(plugin.getConfigManager().getPrefix() + msg);
                    }
                }
            }
        }.runTaskLater(plugin, timeout * 20L));
    }
    
    public boolean hasActiveRequest(UUID senderUuid) {
        return requestsBySender.containsKey(senderUuid);
    }
    
    public TpaRequest getRequest(UUID senderUuid) {
        return requestsBySender.get(senderUuid);
    }
    
    public TpaRequest getRequestByTarget(UUID targetUuid) {
        List<TpaRequest> requests = requestsByTarget.get(targetUuid);
        if (requests != null && !requests.isEmpty()) {
            return requests.get(0);
        }
        return null;
    }
    
    public List<TpaRequest> getRequestsForTarget(UUID targetUuid) {
        List<TpaRequest> requests = requestsByTarget.get(targetUuid);
        return requests != null ? new ArrayList<>(requests) : new ArrayList<>();
    }
    
    public TpaRequest getLatestRequestForTarget(UUID targetUuid) {
        List<TpaRequest> requests = requestsByTarget.get(targetUuid);
        if (requests == null || requests.isEmpty()) {
            return null;
        }
        
        TpaRequest latest = requests.get(0);
        for (TpaRequest request : requests) {
            if (request.getTimestamp() > latest.getTimestamp()) {
                latest = request;
            }
        }
        return latest;
    }
    
    public void removeRequest(UUID senderUuid) {
        TpaRequest request = requestsBySender.remove(senderUuid);
        if (request != null) {
            if (request.getTimeoutTask() != null) {
                request.getTimeoutTask().cancel();
            }
            
            List<TpaRequest> targetRequests = requestsByTarget.get(request.getTargetUuid());
            if (targetRequests != null) {
                targetRequests.remove(request);
                if (targetRequests.isEmpty()) {
                    requestsByTarget.remove(request.getTargetUuid());
                }
            }
        }
    }
    
    public void cancelAllRequests() {
        for (TpaRequest request : requestsBySender.values()) {
            if (request.getTimeoutTask() != null) {
                request.getTimeoutTask().cancel();
            }
        }
        requestsBySender.clear();
        requestsByTarget.clear();
    }
    
    public void startCleanupTask() {
        if (cleanupTask != null) {
            cleanupTask.cancel();
        }
        
        cleanupTask = new BukkitRunnable() {
            @Override
            public void run() {
                List<UUID> toRemove = new ArrayList<>();
                for (Map.Entry<UUID, TpaRequest> entry : requestsBySender.entrySet()) {
                    Player sender = plugin.getServer().getPlayer(entry.getKey());
                    Player target = plugin.getServer().getPlayer(entry.getValue().getTargetUuid());
                    
                    if (sender == null || !sender.isOnline() || target == null || !target.isOnline()) {
                        toRemove.add(entry.getKey());
                    }
                }
                
                for (UUID senderUuid : toRemove) {
                    removeRequest(senderUuid);
                }
            }
        }.runTaskTimer(plugin, 20L * 60, 20L * 60);
    }
    
    public void stopCleanupTask() {
        if (cleanupTask != null) {
            cleanupTask.cancel();
            cleanupTask = null;
        }
    }
}

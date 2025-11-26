package oaktpa.models;

import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class TpaRequest {
    
    private final UUID senderUuid;
    private final UUID targetUuid;
    private final boolean isTpaHere;
    private final long timestamp;
    private BukkitTask timeoutTask;
    
    public TpaRequest(UUID senderUuid, UUID targetUuid, boolean isTpaHere) {
        this.senderUuid = senderUuid;
        this.targetUuid = targetUuid;
        this.isTpaHere = isTpaHere;
        this.timestamp = System.currentTimeMillis();
    }
    
    public UUID getSenderUuid() {
        return senderUuid;
    }
    
    public UUID getTargetUuid() {
        return targetUuid;
    }
    
    public boolean isTpaHere() {
        return isTpaHere;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public BukkitTask getTimeoutTask() {
        return timeoutTask;
    }
    
    public void setTimeoutTask(BukkitTask timeoutTask) {
        this.timeoutTask = timeoutTask;
    }
}

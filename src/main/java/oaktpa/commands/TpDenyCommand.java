package oaktpa.commands;

import oaktpa.OakTpa;
import oaktpa.models.TpaRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpDenyCommand implements CommandExecutor {
    
    private final OakTpa plugin;
    
    public TpDenyCommand(OakTpa plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("otp.use")) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        TpaRequest request;
        
        if (args.length == 0) {
            request = plugin.getRequestManager().getLatestRequestForTarget(player.getUniqueId());
        } else {
            Player requestSender = Bukkit.getPlayer(args[0]);
            if (requestSender == null) {
                player.sendMessage(plugin.getConfigManager().getPrefix() + 
                        plugin.getConfigManager().getMessage("player-not-found", "player", args[0]));
                return true;
            }
            
            request = plugin.getRequestManager().getRequest(requestSender.getUniqueId());
            if (request == null || !request.getTargetUuid().equals(player.getUniqueId())) {
                player.sendMessage(plugin.getConfigManager().getPrefix() + 
                        plugin.getConfigManager().getMessage("no-request-from-player", "player", args[0]));
                return true;
            }
        }
        
        if (request == null) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("no-pending-request"));
            return true;
        }
        
        Player requester = Bukkit.getPlayer(request.getSenderUuid());
        
        player.sendMessage(plugin.getConfigManager().getPrefix() + 
                plugin.getConfigManager().getMessage("request-denied-receiver", "player", 
                        requester != null ? requester.getName() : "someone"));
        
        if (requester != null && requester.isOnline()) {
            requester.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("request-denied-sender", "player", player.getName()));
        }
        
        plugin.getRequestManager().removeRequest(request.getSenderUuid());
        
        return true;
    }
}

package oaktpa.commands;

import oaktpa.OakTpa;
import oaktpa.models.TpaRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpCancelCommand implements CommandExecutor {
    
    private final OakTpa plugin;
    
    public TpCancelCommand(OakTpa plugin) {
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
        
        TpaRequest request = plugin.getRequestManager().getRequest(player.getUniqueId());
        
        if (request == null) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("no-pending-request"));
            return true;
        }
        
        Player target = Bukkit.getPlayer(request.getTargetUuid());
        
        player.sendMessage(plugin.getConfigManager().getPrefix() + 
                plugin.getConfigManager().getMessage("request-cancelled-sender", "player", 
                        target != null ? target.getName() : "someone"));
        
        if (target != null && target.isOnline()) {
            target.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("request-cancelled-receiver", "player", player.getName()));
        }
        
        plugin.getRequestManager().removeRequest(player.getUniqueId());
        
        return true;
    }
}

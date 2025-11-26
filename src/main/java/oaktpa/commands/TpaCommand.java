package oaktpa.commands;

import oaktpa.OakTpa;
import oaktpa.models.TpaRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TpaCommand implements CommandExecutor {
    
    private final OakTpa plugin;
    
    public TpaCommand(OakTpa plugin) {
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
        
        if (args.length == 0) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("usage-tpa"));
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        
        if (target == null || !target.isOnline()) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("player-not-found", "player", args[0]));
            return true;
        }
        
        if (target.equals(player)) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("cannot-teleport-self"));
            return true;
        }
        
        if (plugin.getConfigManager().isPreventDuplicate() && 
                plugin.getRequestManager().hasActiveRequest(player.getUniqueId())) {
            TpaRequest existingRequest = plugin.getRequestManager().getRequest(player.getUniqueId());
            Player existingTarget = Bukkit.getPlayer(existingRequest.getTargetUuid());
            String targetName = existingTarget != null ? existingTarget.getName() : "someone";
            
            player.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("already-sent-request", "player", targetName));
            return true;
        }
        
        if (!plugin.getDatabaseManager().isTpaEnabled(target)) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("player-disabled-requests", "player", target.getName()));
            return true;
        }
        
        plugin.setGUIType(player.getUniqueId(), false);
        Inventory gui = plugin.getGUIManager().createTpaGUI(target);
        player.openInventory(gui);
        
        return true;
    }
}

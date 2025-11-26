package oaktpa.commands;

import oaktpa.OakTpa;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminTpCommand implements CommandExecutor {
    
    private final OakTpa plugin;
    
    public AdminTpCommand(OakTpa plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("otp.admin.tp")) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("usage-tp"));
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        
        if (target == null || !target.isOnline()) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("player-not-found", "player", args[0]));
            return true;
        }
        
        player.teleport(target.getLocation());
        player.sendMessage(plugin.getConfigManager().getPrefix() + 
                plugin.getConfigManager().getMessage("admin-tp-success", "player", target.getName()));
        
        return true;
    }
}

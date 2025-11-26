package oaktpa.commands;

import oaktpa.OakTpa;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OtpCommand implements CommandExecutor {
    
    private final OakTpa plugin;
    
    public OtpCommand(OakTpa plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("otp.admin.forcetp")) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        if (args.length < 3 || !args[0].equalsIgnoreCase("forcetp")) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("usage-forcetp"));
            return true;
        }
        
        Player player1 = Bukkit.getPlayer(args[1]);
        Player player2 = Bukkit.getPlayer(args[2]);
        
        if (player1 == null || !player1.isOnline()) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("player-not-found", "player", args[1]));
            return true;
        }
        
        if (player2 == null || !player2.isOnline()) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("player-not-found", "player", args[2]));
            return true;
        }
        
        player1.teleport(player2.getLocation());
        
        String message = plugin.getConfigManager().getMessage("admin-forcetp-success", "player1", player1.getName());
        message = message.replace("{player2}", player2.getName());
        sender.sendMessage(plugin.getConfigManager().getPrefix() + message);
        
        return true;
    }
}

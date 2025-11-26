package oaktpa.commands;

import oaktpa.OakTpa;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackCommand implements CommandExecutor {
    
    private final OakTpa plugin;
    
    public BackCommand(OakTpa plugin) {
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
        
        Location deathLocation = plugin.getDatabaseManager().getDeathLocation(player);
        
        if (deathLocation == null) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("no-death-location"));
            return true;
        }
        
        player.teleport(deathLocation);
        player.sendMessage(plugin.getConfigManager().getPrefix() + 
                plugin.getConfigManager().getMessage("back-success"));
        
        return true;
    }
}

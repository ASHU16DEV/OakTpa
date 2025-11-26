package oaktpa.commands;

import oaktpa.OakTpa;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OtpaCommand implements CommandExecutor {
    
    private final OakTpa plugin;
    
    public OtpaCommand(OakTpa plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("otp.admin")) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage("usage-otpa"));
            return true;
        }
        
        plugin.reloadPlugin();
        
        sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                plugin.getConfigManager().getMessage("reload-success"));
        
        return true;
    }
}

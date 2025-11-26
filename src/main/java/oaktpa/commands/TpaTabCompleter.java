package oaktpa.commands;

import oaktpa.OakTpa;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpaTabCompleter implements TabCompleter {
    
    private final OakTpa plugin;
    
    public TpaTabCompleter(OakTpa plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (command.getName().equalsIgnoreCase("otpa")) {
            if (args.length == 1) {
                completions.add("reload");
            }
        } else if (command.getName().equalsIgnoreCase("otp")) {
            if (args.length == 1) {
                completions.add("forcetp");
            } else if (args.length == 2 || args.length == 3) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
        } else {
            if (args.length == 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (sender instanceof Player && player.equals(sender)) {
                        continue;
                    }
                    completions.add(player.getName());
                }
            }
        }
        
        List<String> result = new ArrayList<>();
        String input = args.length > 0 ? args[args.length - 1].toLowerCase() : "";
        
        for (String completion : completions) {
            if (completion.toLowerCase().startsWith(input)) {
                result.add(completion);
            }
        }
        
        return result;
    }
}

package oaktpa.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import oaktpa.OakTpa;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatButtonUtil {
    
    public static void sendTpaRequestButtons(Player target, Player sender, String message, String buttonPrefix, boolean isTpaHere, OakTpa plugin) {
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        
        TextComponent prefix = new TextComponent(ChatColor.translateAlternateColorCodes('&', buttonPrefix));
        
        String acceptCommand = plugin.getConfigManager().getCommandName("tpaccept");
        String denyCommand = plugin.getConfigManager().getCommandName("tpdeny");
        
        TextComponent acceptButton = new TextComponent(ChatColor.GREEN + "[ACCEPT]");
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + acceptCommand + " " + sender.getName()));
        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                new ComponentBuilder("Click to accept " + sender.getName() + "'s request").create()));
        
        TextComponent separator = new TextComponent(ChatColor.GRAY + " | ");
        
        TextComponent denyButton = new TextComponent(ChatColor.RED + "[DENY]");
        denyButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + denyCommand + " " + sender.getName()));
        denyButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                new ComponentBuilder("Click to deny " + sender.getName() + "'s request").create()));
        
        prefix.addExtra(acceptButton);
        prefix.addExtra(separator);
        prefix.addExtra(denyButton);
        
        target.spigot().sendMessage(prefix);
    }
}

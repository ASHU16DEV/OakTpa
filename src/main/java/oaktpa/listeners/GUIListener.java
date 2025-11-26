package oaktpa.listeners;

import oaktpa.OakTpa;
import oaktpa.managers.GUIManager;
import oaktpa.utils.ChatButtonUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class GUIListener implements Listener {
    
    private final OakTpa plugin;
    
    public GUIListener(OakTpa plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        String inventoryTitle = event.getView().getTitle();
        
        String guiTitle = org.bukkit.ChatColor.translateAlternateColorCodes('&', 
                plugin.getConfigManager().getGuiConfig().getString("gui.title", "&6Teleport Request"));
        
        if (!inventoryTitle.equals(guiTitle)) {
            return;
        }
        
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) {
            return;
        }
        
        GUIManager guiManager = plugin.getGUIManager();
        
        if (guiManager.isSendButton(clicked)) {
            Player target = getTargetPlayerFromGUI(inventory);
            
            if (target == null || !target.isOnline()) {
                player.sendMessage(plugin.getConfigManager().getPrefix() + 
                        plugin.getConfigManager().getMessage("player-not-found", "player", "target"));
                player.closeInventory();
                plugin.removeGUIType(player.getUniqueId());
                return;
            }
            
            boolean isTpaHere = plugin.getGUIType(player.getUniqueId());
            
            plugin.getRequestManager().createRequest(player, target, isTpaHere);
            
            String messageSent = isTpaHere ? "tpahere-sent" : "tpa-sent";
            String messageReceived = isTpaHere ? "tpahere-received" : "tpa-received";
            String buttonPrefix = isTpaHere ? "tpahere-received-buttons-prefix" : "tpa-received-buttons-prefix";
            
            player.sendMessage(plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage(messageSent, "player", target.getName()));
            
            String receivedMsg = plugin.getConfigManager().getPrefix() + 
                    plugin.getConfigManager().getMessage(messageReceived, "player", player.getName());
            String buttonPrefixMsg = plugin.getConfigManager().getMessage(buttonPrefix);
            
            ChatButtonUtil.sendTpaRequestButtons(target, player, receivedMsg, buttonPrefixMsg, isTpaHere, plugin);
            
            player.closeInventory();
            plugin.removeGUIType(player.getUniqueId());
            
        } else if (guiManager.isCancelButton(clicked)) {
            player.closeInventory();
            plugin.removeGUIType(player.getUniqueId());
        }
    }
    
    private Player getTargetPlayerFromGUI(Inventory inventory) {
        int headSlot = plugin.getConfigManager().getGuiConfig().getInt("gui.player-head.slot", 13);
        ItemStack headItem = inventory.getItem(headSlot);
        
        if (headItem != null && headItem.hasItemMeta() && headItem.getItemMeta() instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
            if (skullMeta.getOwningPlayer() != null) {
                return Bukkit.getPlayer(skullMeta.getOwningPlayer().getUniqueId());
            }
        }
        
        return null;
    }
}

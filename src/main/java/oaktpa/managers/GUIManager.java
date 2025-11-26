package oaktpa.managers;

import oaktpa.OakTpa;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {
    
    private final OakTpa plugin;
    
    public GUIManager(OakTpa plugin) {
        this.plugin = plugin;
    }
    
    public Inventory createTpaGUI(Player target) {
        ConfigurationSection guiSection = plugin.getConfigManager().getGuiConfig().getConfigurationSection("gui");
        
        String title = ChatColor.translateAlternateColorCodes('&', 
                guiSection.getString("title", "&6Teleport Request"));
        int size = guiSection.getInt("size", 27);
        
        Inventory gui = Bukkit.createInventory(null, size, title);
        
        if (guiSection.getBoolean("filler.enabled", true)) {
            ItemStack filler = createItem(
                    guiSection.getString("filler.material", "GRAY_STAINED_GLASS_PANE"),
                    guiSection.getString("filler.name", " "),
                    null
            );
            
            List<Integer> fillerSlots = guiSection.getIntegerList("filler.slots");
            for (int slot : fillerSlots) {
                if (slot >= 0 && slot < size) {
                    gui.setItem(slot, filler);
                }
            }
        }
        
        ItemStack playerHead = createPlayerHead(target, guiSection.getConfigurationSection("player-head"));
        int headSlot = guiSection.getInt("player-head.slot", 13);
        gui.setItem(headSlot, playerHead);
        
        ItemStack sendButton = createButton(guiSection.getConfigurationSection("send-button"));
        int sendSlot = guiSection.getInt("send-button.slot", 11);
        gui.setItem(sendSlot, sendButton);
        
        ItemStack cancelButton = createButton(guiSection.getConfigurationSection("cancel-button"));
        int cancelSlot = guiSection.getInt("cancel-button.slot", 15);
        gui.setItem(cancelSlot, cancelButton);
        
        return gui;
    }
    
    private ItemStack createPlayerHead(Player target, ConfigurationSection section) {
        ItemStack skull = new ItemStack(getMaterial("PLAYER_HEAD", "SKULL_ITEM"), 1, (short) 3);
        
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(target);
            
            String name = ChatColor.translateAlternateColorCodes('&', 
                    section.getString("name", "&e{player}"))
                    .replace("{player}", target.getName());
            meta.setDisplayName(name);
            
            List<String> lore = new ArrayList<>();
            for (String line : section.getStringList("lore")) {
                String translatedLine = ChatColor.translateAlternateColorCodes('&', line)
                        .replace("{player}", target.getName())
                        .replace("{world}", target.getWorld().getName());
                lore.add(translatedLine);
            }
            meta.setLore(lore);
            
            skull.setItemMeta(meta);
        }
        
        return skull;
    }
    
    private ItemStack createButton(ConfigurationSection section) {
        String materialName = section.getString("material", "WOOL");
        String name = section.getString("name", "");
        List<String> lore = section.getStringList("lore");
        
        return createItem(materialName, name, lore);
    }
    
    private ItemStack createItem(String materialName, String name, List<String> lore) {
        Material material = getMaterial(materialName, "WOOL");
        ItemStack item = new ItemStack(material);
        
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            
            if (lore != null && !lore.isEmpty()) {
                List<String> translatedLore = new ArrayList<>();
                for (String line : lore) {
                    translatedLore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(translatedLore);
            }
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    private Material getMaterial(String name, String fallback) {
        try {
            return Material.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            ConfigurationSection legacy = plugin.getConfigManager().getGuiConfig()
                    .getConfigurationSection("compatibility.legacy-materials");
            
            if (legacy != null && legacy.contains(name)) {
                String legacyName = legacy.getString(name);
                if (legacyName != null && legacyName.contains(":")) {
                    String[] parts = legacyName.split(":");
                    try {
                        return Material.valueOf(parts[0]);
                    } catch (IllegalArgumentException ex) {
                        // Fallback
                    }
                }
            }
            
            try {
                return Material.valueOf(fallback);
            } catch (IllegalArgumentException ex) {
                return Material.STONE;
            }
        }
    }
    
    public boolean isSendButton(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ConfigurationSection section = plugin.getConfigManager().getGuiConfig()
                .getConfigurationSection("gui.send-button");
        String name = ChatColor.translateAlternateColorCodes('&', section.getString("name", ""));
        
        return item.getItemMeta().getDisplayName().equals(name);
    }
    
    public boolean isCancelButton(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ConfigurationSection section = plugin.getConfigManager().getGuiConfig()
                .getConfigurationSection("gui.cancel-button");
        String name = ChatColor.translateAlternateColorCodes('&', section.getString("name", ""));
        
        return item.getItemMeta().getDisplayName().equals(name);
    }
}

package oaktpa.managers;

import oaktpa.OakTpa;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigManager {
    
    private final OakTpa plugin;
    private FileConfiguration config;
    private FileConfiguration guiConfig;
    private File guiFile;
    
    public ConfigManager(OakTpa plugin) {
        this.plugin = plugin;
    }
    
    public void loadConfigs() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
        
        createGuiConfig();
    }
    
    private void createGuiConfig() {
        guiFile = new File(plugin.getDataFolder(), "gui.yml");
        
        if (!guiFile.exists()) {
            plugin.saveResource("gui.yml", false);
        }
        
        guiConfig = YamlConfiguration.loadConfiguration(guiFile);
        
        InputStream defaultStream = plugin.getResource("gui.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            guiConfig.setDefaults(defaultConfig);
        }
    }
    
    public void saveGuiConfig() {
        try {
            guiConfig.save(guiFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save gui.yml: " + e.getMessage());
        }
    }
    
    public FileConfiguration getConfig() {
        return config;
    }
    
    public FileConfiguration getGuiConfig() {
        return guiConfig;
    }
    
    public String getMessage(String path) {
        String message = config.getString("messages." + path, "");
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public String getMessage(String path, String placeholder, String value) {
        String message = getMessage(path);
        return message.replace("{" + placeholder + "}", value);
    }
    
    public String getPrefix() {
        return getMessage("prefix");
    }
    
    public int getRequestTimeout() {
        return config.getInt("request.timeout", 60);
    }
    
    public boolean isPreventDuplicate() {
        return config.getBoolean("request.prevent-duplicate", true);
    }
    
    public String getDatabaseFilename() {
        return config.getString("database.filename", "oaktpa.db");
    }
    
    public int getTeleportDelay() {
        return config.getInt("teleport.teleport-delay", 5);
    }
    
    public boolean isCancelOnMove() {
        return config.getBoolean("teleport.cancel-on-move", true);
    }
    
    public String getMessage(String path, String placeholder1, String value1, String placeholder2, String value2) {
        String message = getMessage(path);
        message = message.replace("{" + placeholder1 + "}", value1);
        message = message.replace("{" + placeholder2 + "}", value2);
        return message;
    }
    
    public String getCommandName(String command) {
        return config.getString("commands." + command, command);
    }
}

package oaktpa;

import oaktpa.commands.*;
import oaktpa.listeners.DeathListener;
import oaktpa.listeners.GUIListener;
import oaktpa.listeners.PlayerQuitListener;
import oaktpa.managers.ConfigManager;
import oaktpa.managers.DatabaseManager;
import oaktpa.managers.GUIManager;
import oaktpa.managers.RequestManager;
import oaktpa.managers.TeleportManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OakTpa extends JavaPlugin {
    
    private static OakTpa instance;
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private RequestManager requestManager;
    private TeleportManager teleportManager;
    private GUIManager guiManager;
    private Map<UUID, Boolean> guiTypeMap;
    
    @Override
    public void onEnable() {
        instance = this;
        
        getServer().getConsoleSender().sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        getServer().getConsoleSender().sendMessage("§6  ___        _    _____");
        getServer().getConsoleSender().sendMessage("§6 / _ \\  __ _| | _|_   _|_ __  __ _");
        getServer().getConsoleSender().sendMessage("§6| | | |/ _` | |/ / | | | '_ \\/ _` |");
        getServer().getConsoleSender().sendMessage("§6| |_| | (_| |   <  | | | |_) | (_| |");
        getServer().getConsoleSender().sendMessage("§6 \\___/ \\__,_|_|\\_\\ |_| | .__/ \\__,_|");
        getServer().getConsoleSender().sendMessage("§6                        |_|");
        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage("§7  Version: §e1.0.0");
        getServer().getConsoleSender().sendMessage("§7  Author: §eASHU16");
        getServer().getConsoleSender().sendMessage("§7  Description: §fProfessional TPA Plugin with GUI");
        getServer().getConsoleSender().sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        loadPlugin();
        
        getServer().getConsoleSender().sendMessage("§a✓ §fOakTpa has been §aenabled §fsuccessfully!");
        getServer().getConsoleSender().sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
        if (requestManager != null) {
            requestManager.cancelAllRequests();
        }
        if (teleportManager != null) {
            teleportManager.cancelAll();
        }
        
        getServer().getConsoleSender().sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        getServer().getConsoleSender().sendMessage("§c✗ §fOakTpa has been §cdisabled§f!");
        getServer().getConsoleSender().sendMessage("§7Thank you for using OakTpa!");
        getServer().getConsoleSender().sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    public void loadPlugin() {
        configManager = new ConfigManager(this);
        configManager.loadConfigs();
        
        databaseManager = new DatabaseManager(this);
        databaseManager.initialize();
        
        requestManager = new RequestManager(this);
        teleportManager = new TeleportManager(this);
        guiManager = new GUIManager(this);
        guiTypeMap = new HashMap<>();
        
        registerCommands();
        registerListeners();
        
        requestManager.startCleanupTask();
    }
    
    public void reloadPlugin() {
        if (requestManager != null) {
            requestManager.cancelAllRequests();
            requestManager.stopCleanupTask();
        }
        if (teleportManager != null) {
            teleportManager.cancelAll();
        }
        
        configManager.loadConfigs();
        guiManager = new GUIManager(this);
        
        requestManager.startCleanupTask();
        
        getServer().getConsoleSender().sendMessage("§a✓ §fOakTpa reloaded successfully!");
    }
    
    private void registerCommands() {
        getCommand("tpa").setExecutor(new TpaCommand(this));
        getCommand("tpahere").setExecutor(new TpaHereCommand(this));
        getCommand("tpaccept").setExecutor(new TpAcceptCommand(this));
        getCommand("tpcancel").setExecutor(new TpCancelCommand(this));
        getCommand("tpdeny").setExecutor(new TpDenyCommand(this));
        getCommand("tpatoggle").setExecutor(new TpaToggleCommand(this));
        getCommand("back").setExecutor(new BackCommand(this));
        getCommand("tp").setExecutor(new AdminTpCommand(this));
        getCommand("otpa").setExecutor(new OtpaCommand(this));
        getCommand("otp").setExecutor(new OtpCommand(this));
        
        TpaTabCompleter tabCompleter = new TpaTabCompleter(this);
        getCommand("tpa").setTabCompleter(tabCompleter);
        getCommand("tpahere").setTabCompleter(tabCompleter);
        getCommand("tpaccept").setTabCompleter(tabCompleter);
        getCommand("tpdeny").setTabCompleter(tabCompleter);
        getCommand("tp").setTabCompleter(tabCompleter);
        getCommand("otp").setTabCompleter(tabCompleter);
        getCommand("otpa").setTabCompleter(tabCompleter);
    }
    
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    }
    
    public static OakTpa getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    public RequestManager getRequestManager() {
        return requestManager;
    }
    
    public GUIManager getGUIManager() {
        return guiManager;
    }
    
    public TeleportManager getTeleportManager() {
        return teleportManager;
    }
    
    public void setGUIType(UUID playerUuid, boolean isTpaHere) {
        guiTypeMap.put(playerUuid, isTpaHere);
    }
    
    public boolean getGUIType(UUID playerUuid) {
        return guiTypeMap.getOrDefault(playerUuid, false);
    }
    
    public void removeGUIType(UUID playerUuid) {
        guiTypeMap.remove(playerUuid);
    }
}

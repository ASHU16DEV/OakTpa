package oaktpa.managers;

import oaktpa.OakTpa;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class DatabaseManager {
    
    private final OakTpa plugin;
    private Connection connection;
    
    public DatabaseManager(OakTpa plugin) {
        this.plugin = plugin;
    }
    
    public void initialize() {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            
            String dbPath = new File(dataFolder, plugin.getConfigManager().getDatabaseFilename()).getAbsolutePath();
            String url = "jdbc:sqlite:" + dbPath;
            
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
            
            createTables();
            
            plugin.getServer().getConsoleSender().sendMessage("§a✓ §fDatabase connected successfully!");
        } catch (ClassNotFoundException | SQLException e) {
            plugin.getServer().getConsoleSender().sendMessage("§c✗ §fFailed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void createTables() throws SQLException {
        String createPlayersTable = "CREATE TABLE IF NOT EXISTS players (" +
                "uuid TEXT PRIMARY KEY," +
                "username TEXT NOT NULL," +
                "tpa_enabled INTEGER DEFAULT 1," +
                "last_death_world TEXT," +
                "last_death_x REAL," +
                "last_death_y REAL," +
                "last_death_z REAL," +
                "last_death_yaw REAL," +
                "last_death_pitch REAL" +
                ");";
        
        Statement stmt = connection.createStatement();
        stmt.execute(createPlayersTable);
        stmt.close();
    }
    
    public boolean isTpaEnabled(Player player) {
        try {
            String query = "SELECT tpa_enabled FROM players WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, player.getUniqueId().toString());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean enabled = rs.getInt("tpa_enabled") == 1;
                rs.close();
                stmt.close();
                return enabled;
            }
            rs.close();
            stmt.close();
            
            createPlayerEntry(player);
            return true;
            
        } catch (SQLException e) {
            plugin.getLogger().severe("Error checking TPA status: " + e.getMessage());
            return true;
        }
    }
    
    public void setTpaEnabled(Player player, boolean enabled) {
        try {
            createPlayerEntry(player);
            
            String update = "UPDATE players SET tpa_enabled = ? WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(update);
            stmt.setInt(1, enabled ? 1 : 0);
            stmt.setString(2, player.getUniqueId().toString());
            stmt.executeUpdate();
            stmt.close();
            
        } catch (SQLException e) {
            plugin.getLogger().severe("Error setting TPA status: " + e.getMessage());
        }
    }
    
    public void saveDeathLocation(Player player, Location location) {
        try {
            createPlayerEntry(player);
            
            String update = "UPDATE players SET last_death_world = ?, last_death_x = ?, " +
                    "last_death_y = ?, last_death_z = ?, last_death_yaw = ?, last_death_pitch = ? " +
                    "WHERE uuid = ?";
            
            PreparedStatement stmt = connection.prepareStatement(update);
            stmt.setString(1, location.getWorld().getName());
            stmt.setDouble(2, location.getX());
            stmt.setDouble(3, location.getY());
            stmt.setDouble(4, location.getZ());
            stmt.setFloat(5, location.getYaw());
            stmt.setFloat(6, location.getPitch());
            stmt.setString(7, player.getUniqueId().toString());
            stmt.executeUpdate();
            stmt.close();
            
        } catch (SQLException e) {
            plugin.getLogger().severe("Error saving death location: " + e.getMessage());
        }
    }
    
    public Location getDeathLocation(Player player) {
        try {
            String query = "SELECT last_death_world, last_death_x, last_death_y, last_death_z, " +
                    "last_death_yaw, last_death_pitch FROM players WHERE uuid = ?";
            
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, player.getUniqueId().toString());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String worldName = rs.getString("last_death_world");
                if (worldName == null) {
                    rs.close();
                    stmt.close();
                    return null;
                }
                
                double x = rs.getDouble("last_death_x");
                double y = rs.getDouble("last_death_y");
                double z = rs.getDouble("last_death_z");
                float yaw = rs.getFloat("last_death_yaw");
                float pitch = rs.getFloat("last_death_pitch");
                
                rs.close();
                stmt.close();
                
                org.bukkit.World world = plugin.getServer().getWorld(worldName);
                if (world != null) {
                    return new Location(world, x, y, z, yaw, pitch);
                }
            }
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            plugin.getLogger().severe("Error getting death location: " + e.getMessage());
        }
        return null;
    }
    
    private void createPlayerEntry(Player player) throws SQLException {
        String insert = "INSERT OR IGNORE INTO players (uuid, username) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(insert);
        stmt.setString(1, player.getUniqueId().toString());
        stmt.setString(2, player.getName());
        stmt.executeUpdate();
        stmt.close();
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getServer().getConsoleSender().sendMessage("§a✓ §fDatabase connection closed!");
            }
        } catch (SQLException e) {
            plugin.getServer().getConsoleSender().sendMessage("§c✗ §fError closing database: " + e.getMessage());
        }
    }
}

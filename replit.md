# OakTpa Plugin - Development Notes

## Project Overview

**OakTpa** is a professional Minecraft TPA (teleport request) plugin built with Java and Maven. It features an interactive GUI system, SQLite database, and complete customization support.

### Key Information
- **Plugin Name**: OakTpa
- **Version**: 1.0.0
- **Author**: ASHU16
- **Minecraft Support**: 1.16.5 - 1.21.10
- **Build System**: Maven
- **Database**: SQLite (embedded)

## Project Structure

```
workspace/
├── src/main/java/oaktpa/
│   ├── OakTpa.java                 # Main plugin class
│   ├── commands/                   # All command implementations
│   │   ├── TpaCommand.java         # /tpa command
│   │   ├── TpaHereCommand.java     # /tpahere command
│   │   ├── TpAcceptCommand.java    # /tpaccept command
│   │   ├── TpCancelCommand.java    # /tpcancel command
│   │   ├── TpDenyCommand.java      # /tpdeny command
│   │   ├── TpaToggleCommand.java   # /tpatoggle command
│   │   ├── BackCommand.java        # /back command
│   │   ├── AdminTpCommand.java     # /tp (admin)
│   │   ├── OtpaCommand.java        # /otpa reload
│   │   ├── OtpCommand.java         # /otp forcetp
│   │   └── TpaTabCompleter.java    # Tab completion
│   ├── managers/                   # Core managers
│   │   ├── ConfigManager.java      # Config & messages
│   │   ├── DatabaseManager.java    # SQLite operations
│   │   ├── GUIManager.java         # Inventory GUI
│   │   └── RequestManager.java     # Request tracking
│   ├── listeners/                  # Event listeners
│   │   ├── GUIListener.java        # GUI click events
│   │   └── DeathListener.java      # Death location tracking
│   └── models/                     # Data models
│       └── TpaRequest.java         # Request model
├── src/main/resources/
│   ├── plugin.yml                  # Plugin metadata
│   ├── config.yml                  # Main configuration
│   └── gui.yml                     # GUI customization
├── pom.xml                         # Maven configuration
└── README.md                       # Documentation
```

## Building the Plugin

```bash
# Clean and build
mvn clean package

# Output location
target/OakTpa-1.0.0.jar
```

## Features Implemented

### ✅ Player Commands
- `/tpa` - Teleport request with GUI
- `/tpahere` - Request player to teleport to you
- `/tpaccept` - Accept requests
- `/tpcancel` - Cancel sent requests
- `/tpdeny` - Deny received requests
- `/tpatoggle` - Toggle request reception
- `/back` - Return to death location

### ✅ Admin Commands
- `/otpa reload` - Hot-reload entire plugin
- `/tp <player>` - Instant teleport
- `/otp forcetp` - Force teleport players

### ✅ GUI System
- 27-slot customizable inventory
- Player head display
- Send/Cancel buttons
- Clickable-only items
- Hot-reload support

### ✅ Advanced Features
- 60-second auto-timeout
- Duplicate request prevention
- SQLite database integration
- Professional colored console output
- Smart tab completion
- Permission system
- Multi-version support (1.16.5-1.21.10)

## Configuration Files

### config.yml
Contains all messages, timers, and settings. All messages support color codes using `&` format.

### gui.yml
Complete GUI customization including:
- Slot positions
- Item materials
- Display names
- Lore lines
- Legacy material compatibility

## Database

The plugin automatically creates `oaktpa.db` in the plugin folder with tables for:
- Player TPA toggle status
- Last death locations

## Console Output

The plugin features professional colored console messages:
- Green (§a) for success
- Red (§c) for errors
- Yellow (§e) for important info
- Gray (§7/§8) for details

## Recent Changes

### 2025-11-24 (Update 2)
- **NEW**: Added teleport delay system (5 seconds configurable countdown)
- **NEW**: Added cancel-on-move feature - teleport cancels if player moves
- **NEW**: Implemented clickable chat buttons (ACCEPT/DENY) for TPA requests
- **NEW**: Added command customization in config.yml
- **IMPROVED**: Fixed ASCII art 'K' character in console banner
- **IMPROVED**: Enhanced config.yml with teleport warmup messages
- Updated README.md with all new features

### 2025-11-24 (Update 1)
- Initial plugin creation
- Implemented all player and admin commands
- Created GUI system with hot-reload
- Added SQLite database integration
- Implemented request timeout system
- Added professional console output
- Created comprehensive configuration system
- **Fixed critical bug**: Refactored RequestManager with bidirectional indexing (requestsBySender + requestsByTarget) for O(1) lookups
- Added PlayerQuitListener for automatic cleanup of GUI state and orphaned requests
- Built successfully with Maven - production ready

## How to Use on Minecraft Server

1. Build the plugin: `mvn clean package`
2. Copy `target/OakTpa-1.0.0.jar` to your server's `plugins/` folder
3. Start/restart your Minecraft server
4. Plugin will auto-create config files
5. Customize `config.yml` and `gui.yml` as needed
6. Use `/otpa reload` to apply changes without restart

## User Preferences

- Professional colored console output
- Clean, organized code structure
- Full hot-reload functionality
- Extensive customization options
- No package prefix (simple "oaktpa" package)

## Dependencies

- Spigot API 1.16.5-R0.1-SNAPSHOT (provided)
- SQLite JDBC 3.42.0.0 (shaded)
- Maven Shade Plugin for JAR packaging

## Notes

This is a Minecraft server plugin, not a standalone application. It requires a Spigot/Paper Minecraft server to run.

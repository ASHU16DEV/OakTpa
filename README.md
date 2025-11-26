# OakTpa - Professional TPA Plugin

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Minecraft](https://img.shields.io/badge/minecraft-1.16.5--1.21.10-green.svg)
![Author](https://img.shields.io/badge/author-ASHU16-orange.svg)

A professional teleport request plugin for Minecraft with an interactive GUI system, SQLite database, and full customization support.

## Features

### Player Commands
- `/tpa <player>` - Send a teleport request to another player
- `/tpahere <player>` - Request a player to teleport to you
- `/tpaccept [player]` - Accept a teleport request (latest or specific player)
- `/tpcancel` - Cancel your sent teleport request
- `/tpdeny [player]` - Deny a teleport request
- `/tpatoggle` - Toggle receiving teleport requests ON/OFF
- `/back` - Teleport to your last death location

### Admin Commands
- `/otpa reload` - Reload entire plugin (configs, GUI, messages)
- `/tp <player>` - Teleport to a player instantly
- `/otp forcetp <player1> <player2>` - Force teleport player1 to player2

### GUI System
- **27-slot inventory** with customizable layout
- **Player head display** showing target player and world
- **Send & Cancel buttons** with custom materials and lores
- **Clickable-only items** - prevents inventory manipulation
- **Auto-refresh** on reload - changes apply instantly

### Smart Features
- **Teleport delay** - Configurable countdown (5 seconds default) before teleport
- **Cancel on move** - Teleport cancelled if player moves during countdown
- **Clickable chat buttons** - ACCEPT/DENY buttons directly in chat messages
- **60-second timeout** - Auto-cancel requests after timeout
- **Duplicate prevention** - One request per player at a time
- **Request tracking** - Multiple requests managed efficiently
- **Death location** - Automatically saves last death position
- **SQLite database** - Persistent player data storage
- **Tab completion** - Smart suggestions for all commands
- **Hot-reload** - `/otpa reload` updates everything instantly

## Installation

1. **Download the plugin**
   ```bash
   mvn clean package
   ```
   The plugin JAR will be created at `target/OakTpa-1.0.0.jar`

2. **Install on server**
   - Copy `OakTpa-1.0.0.jar` to your server's `plugins/` folder
   - Restart your server or use a plugin manager

3. **Configuration**
   - Files will be created automatically in `plugins/OakTpa/`
   - Edit `config.yml` for messages and settings
   - Edit `gui.yml` to customize the GUI appearance

## Configuration

### config.yml
```yaml
# Request timeout in seconds
request:
  timeout: 60
  prevent-duplicate: true

# Teleport delay and cancel on move
teleport:
  teleport-delay: 5        # Seconds before teleport
  cancel-on-move: true     # Cancel if player moves

# Customize command names
commands:
  tpa: 'tpa'
  tpaccept: 'tpaccept'
  # ... all commands customizable

# All messages with color code support
messages:
  prefix: '&8[&6OakTpa&8]&r '
  tpa-sent: '&aTeleport request sent to &e{player}&a!'
  tpa-received: '&e{player} &awants to teleport to you.'
  # ... and many more customizable messages
```

### gui.yml
```yaml
gui:
  title: '&6Teleport Request'
  size: 27
  
  player-head:
    slot: 13
    material: PLAYER_HEAD
    name: '&e{player}'
  
  send-button:
    slot: 11
    material: LIME_WOOL
    name: '&a&lSEND REQUEST'
  
  cancel-button:
    slot: 15
    material: RED_WOOL
    name: '&c&lCANCEL'
```

## Permissions

### Player Permissions
- `otp.use` - Access to all player commands (default: true)

### Admin Permissions
- `otp.admin` - Access to /otpa reload
- `otp.admin.tp` - Access to /tp command
- `otp.admin.forcetp` - Access to /otp forcetp command

## How It Works

### TPA Request Flow
1. Player types `/tpa <player>` and presses Enter
2. **GUI opens** showing the target player's head and options
3. Player clicks **SEND** to send the request
4. Target receives notification with **clickable ACCEPT/DENY buttons** in chat
5. Target can click buttons OR use `/tpaccept` or `/tpdeny` commands
6. If accepted, **5-second countdown** starts (configurable)
7. Player must **stay still** during countdown (movement cancels)
8. After countdown, requester teleports to target
9. If denied/cancelled, both players get notified
10. After 60 seconds, request auto-expires

### TPA Here Request Flow
Same as TPA, but the **target teleports to requester** instead.

## Database

OakTpa uses **SQLite** for data storage:
- Player TPA toggle status (enabled/disabled)
- Last death locations for `/back` command
- Automatic database creation on first run
- No external database setup required

## Building from Source

```bash
# Clone the repository
git clone <repository-url>

# Build with Maven
mvn clean package

# Output JAR will be in target/OakTpa-1.0.0.jar
```

## Support

- **Minecraft Version**: 1.16.5 - 1.21.10
- **Java Version**: 8+
- **Server Software**: Spigot, Paper, Purpur, etc.

## Author

Created by **ASHU16**

## License

All rights reserved.

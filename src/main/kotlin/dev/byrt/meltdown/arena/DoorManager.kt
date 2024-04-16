package dev.byrt.meltdown.arena

import dev.byrt.meltdown.data.Door
import dev.byrt.meltdown.data.DoorStatus
import dev.byrt.meltdown.data.DoorType
import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Sounds

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material

class DoorManager(private val game : Game) {
    private var doorList = ArrayList<Door>()
    private var latestDoorId = 0

    fun populateDoors() {
        // Spawn room door (Red)
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -1998.0, 70.0, -2080.0),
                Location(game.locationManager.getWorld(), -1992.0, 73.0, -2076.0),
                DoorType.SPAWN_RED,
                DoorStatus.OPEN
            )
        )
        // Spawn room door (Yellow)
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -1924.0, 70.0, -1998.0),
                Location(game.locationManager.getWorld(), -1920.0, 73.0, -1992.0),
                DoorType.SPAWN_YELLOW,
                DoorStatus.OPEN
            )
        )
        // Spawn room door (Lime)
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -2008.0, 70.0, -1924.0),
                Location(game.locationManager.getWorld(), -2002.0, 73.0, -1920.0),
                DoorType.SPAWN_LIME,
                DoorStatus.OPEN
            )
        )
        // Spawn room door (Blue)
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -2080.0, 70.0, -2008.0),
                Location(game.locationManager.getWorld(), -2076.0, 73.0, -2002.0),
                DoorType.SPAWN_BLUE,
                DoorStatus.OPEN
            )
        )
        // Coin room doors (Top right)
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -1936.0, 70.0, -2028.0),
                Location(game.locationManager.getWorld(), -1930.0, 73.0, -2024.0),
                DoorType.COIN_CORNER_NE,
                DoorStatus.OPEN
            )
        )
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -1976.0, 70.0, -2059.0),
                Location(game.locationManager.getWorld(), -1972.0, 73.0, -2053.0),
                DoorType.COIN_CORNER_NE,
                DoorStatus.OPEN
            )
        )
        // Coin room doors (Bottom left)
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -2070.0, 70.0, -1976.0),
                Location(game.locationManager.getWorld(), -2064.0, 73.0, -1972.0),
                DoorType.COIN_CORNER_SW,
                DoorStatus.OPEN
            )
        )
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -2028.0, 70.0, -1947.0),
                Location(game.locationManager.getWorld(), -2024.0, 73.0, -1941.0),
                DoorType.COIN_CORNER_SW,
                DoorStatus.OPEN
            )
        )
        // Corner (Top left)
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -2028.0, 70.0, -2070.0),
                Location(game.locationManager.getWorld(), -2024.0, 73.0, -2064.0),
                DoorType.CORNER_NW,
                DoorStatus.OPEN
            )
        )
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -2059.0, 70.0, -2028.0),
                Location(game.locationManager.getWorld(), -2053.0, 73.0, -2024.0),
                DoorType.CORNER_NW,
                DoorStatus.OPEN
            )
        )
        // Corner (Bottom right)
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -1976.0, 70.0, -1936.0),
                Location(game.locationManager.getWorld(), -1972.0, 73.0, -1930.0),
                DoorType.CORNER_SE,
                DoorStatus.OPEN
            )
        )
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -1947.0, 70.0, -1976.0),
                Location(game.locationManager.getWorld(), -1941.0, 73.0, -1972.0),
                DoorType.CORNER_SE,
                DoorStatus.OPEN
            )
        )

        // All centre connected doors
        // Red side
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -1983.0, 70.0, -2028.0),
                Location(game.locationManager.getWorld(), -1977.0, 73.0, -2024.0),
                DoorType.CENTRE,
                DoorStatus.PRE_MELT_CLOSED
            )
        )
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -2009.0, 78.0, -2028.0),
                Location(game.locationManager.getWorld(), -2003.0, 81.0, -2024.0),
                DoorType.CENTRE,
                DoorStatus.PRE_MELT_CLOSED
            )
        )
        // Yellow side
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -1976.0, 70.0, -1983.0),
                Location(game.locationManager.getWorld(), -1972.0, 73.0, -1977.0),
                DoorType.CENTRE,
                DoorStatus.PRE_MELT_CLOSED
            )
        )
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -1976.0, 78.0, -2009.0),
                Location(game.locationManager.getWorld(), -1972.0, 81.0, -2003.0),
                DoorType.CENTRE,
                DoorStatus.PRE_MELT_CLOSED
            )
        )
        // Lime side
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -2023.0, 70.0, -1976.0),
                Location(game.locationManager.getWorld(), -2017.0, 73.0, -1972.0),
                DoorType.CENTRE,
                DoorStatus.PRE_MELT_CLOSED
            )
        )
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -1997.0, 78.0, -1976.0),
                Location(game.locationManager.getWorld(), -1991.0, 81.0, -1972.0),
                DoorType.CENTRE,
                DoorStatus.PRE_MELT_CLOSED
            )
        )
        // Blue side
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -2028.0, 70.0, -2023.0),
                Location(game.locationManager.getWorld(), -2024.0, 73.0, -2017.0),
                DoorType.CENTRE,
                DoorStatus.PRE_MELT_CLOSED
            )
        )
        addDoor(
            Door(
                incrementDoorId(),
                Location(game.locationManager.getWorld(), -2028.0, 78.0, -1997.0),
                Location(game.locationManager.getWorld(), -2024.0, 81.0, -1991.0),
                DoorType.CENTRE,
                DoorStatus.PRE_MELT_CLOSED
            )
        )
    }

    fun addDoor(door : Door) {
        doorList.add(door)
    }

    fun removeDoor(door : Door) {
        doorList.remove(door)
    }

    //TODO: Door Operations will be for animating them.
    //TODO: Also change door statuses - Partly for different door blocks depending on door state.
    fun openDoor(door : Door, doorOperation : DoorOperation) {
        game.blockManager.setDoor(door, Material.AIR)
    }

    fun closeDoor(door : Door, material : Material, doorOperation : DoorOperation) {
        game.blockManager.setDoor(door, material)
    }

    fun openCentreDoors() {
        for(door in doorList.filter { door -> door.doorType == DoorType.CENTRE }) {
            openDoor(door, DoorOperation.OPEN_IN_GAME)
        }
        for(player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sounds.Alert.GENERAL_ALERT, 1f, 1f)
            player.sendMessage(
                Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("Doors to the centre of the lab are now open.", NamedTextColor.AQUA, TextDecoration.BOLD)
                )
            )
        }
    }

    fun getDoorByType(doorType : DoorType) : ArrayList<Door> {
        val doors = ArrayList<Door>()
        for(door in doorList) {
            if(door.doorType == doorType) {
                doors.add(door)
            }
        }
        return doors
    }

    fun resetDoors() {
        for(door in doorList) {
            if(door.doorType == DoorType.CENTRE) {
                closeDoor(door, Material.QUARTZ_PILLAR, DoorOperation.CLOSE_RESET)
            } else {
                openDoor(door, DoorOperation.OPEN_RESET)
            }
        }
    }

    fun openAllDoors() {
        for(door in doorList) {
            openDoor(door, DoorOperation.OPEN_RESET)
        }
    }

    private fun incrementDoorId() : Int {
        return latestDoorId++
    }

    fun getDoorsList() : ArrayList<Door> {
        return doorList
    }
}

enum class DoorOperation {
    OPEN_RESET,
    CLOSE_RESET,
    OPEN_IN_GAME,
    CLOSE_MELTDOWN
}
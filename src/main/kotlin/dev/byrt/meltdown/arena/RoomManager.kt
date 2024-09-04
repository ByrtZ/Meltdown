package dev.byrt.meltdown.arena

import dev.byrt.meltdown.data.DoorType
import dev.byrt.meltdown.data.Room
import dev.byrt.meltdown.data.RoomType
import dev.byrt.meltdown.game.Game

import org.bukkit.Location

class RoomManager(private val game : Game) {
    private var roomList = ArrayList<Room>()
    private var latestRoomId = 0

    fun populateRooms() {
        game.plugin.logger.info("Populating rooms..")
        // Spawn rooms
        // Red spawn
        addRoom(
            Room(
                incrementRoomId(),
                Location(game.locationManager.getWorld(), -2025.0, 63.0, -2134.0),
                Location(game.locationManager.getWorld(), -1975.0, 92.0, -2078.0),
                RoomType.SPAWN,
                ArrayList(game.doorManager.getDoorByType(DoorType.SPAWN_RED)),
                Location(game.locationManager.getWorld(), -1999.0, 69.0, -2123.0)
            )
        )
        // Yellow spawn
        addRoom(
            Room(
                incrementRoomId(),
                Location(game.locationManager.getWorld(), -1922.0, 63.0, -2025.0),
                Location(game.locationManager.getWorld(), -1866.0, 92.0, -1975.0),
                RoomType.SPAWN,
                ArrayList(game.doorManager.getDoorByType(DoorType.SPAWN_YELLOW)),
                Location(game.locationManager.getWorld(), -1877.0, 69.0, -1999.0)
            )
        )
        // Lime spawn
        addRoom(
            Room(
                incrementRoomId(),
                Location(game.locationManager.getWorld(), -2025.0, 63.0, -1922.0),
                Location(game.locationManager.getWorld(), -1975.0, 92.0, -1866.0),
                RoomType.SPAWN,
                ArrayList(game.doorManager.getDoorByType(DoorType.SPAWN_LIME)),
                Location(game.locationManager.getWorld(), -2001.0, 69.0, -1877.0)
            )
        )
        // Blue spawn
        addRoom(
            Room(
                incrementRoomId(),
                Location(game.locationManager.getWorld(), -2134.0, 63.0, -2025.0),
                Location(game.locationManager.getWorld(), -2078.0, 92.0, -1975.0),
                RoomType.SPAWN,
                ArrayList(game.doorManager.getDoorByType(DoorType.SPAWN_BLUE)),
                Location(game.locationManager.getWorld(), -2123.0, 69.0, -2001.0)
            )
        )
        // North East coin
        addRoom(
            Room(
                incrementRoomId(),
                Location(game.locationManager.getWorld(), -1974.0, 63.0, -2077.0),
                Location(game.locationManager.getWorld(), -1923.0, 92.0, -2026.0),
                RoomType.CORNER,
                ArrayList(game.doorManager.getDoorByType(DoorType.COIN_CORNER_NE)),
                Location(game.locationManager.getWorld(), -1933.0, 69.0, -2066.0)
            )
        )
        // South East corner
        addRoom(
            Room(
                incrementRoomId(),
                Location(game.locationManager.getWorld(), -1974.0, 63.0, -1974.0),
                Location(game.locationManager.getWorld(), -1923.0, 92.0, -1923.0),
                RoomType.CORNER,
                ArrayList(game.doorManager.getDoorByType(DoorType.CORNER_SE)),
                Location(game.locationManager.getWorld(), -1937.0, 69.0, -1951.0)
            )
        )
        // South West coin
        addRoom(
            Room(
                incrementRoomId(),
                Location(game.locationManager.getWorld(), -2077.0, 63.0, -1974.0),
                Location(game.locationManager.getWorld(), -2026.0, 92.0, -1923.0),
                RoomType.CORNER,
                ArrayList(game.doorManager.getDoorByType(DoorType.COIN_CORNER_SW)),
                Location(game.locationManager.getWorld(), -2067.0, 69.0, -1934.0)
            )
        )
        // North West corner
        addRoom(
            Room(
                incrementRoomId(),
                Location(game.locationManager.getWorld(), -2077.0, 63.0, -2077.0),
                Location(game.locationManager.getWorld(), -2026.0, 92.0, -2026.0),
                RoomType.CORNER,
                ArrayList(game.doorManager.getDoorByType(DoorType.CORNER_NW)),
                Location(game.locationManager.getWorld(), -2063.0, 69.0, -2049.0)
            )
        )
        // Centre adjacent (Red)
        addRoom(
            Room(
                incrementRoomId(),
                Location(game.locationManager.getWorld(), -2025.0, 63.0, -2077.0),
                Location(game.locationManager.getWorld(), -1975.0, 92.0, -2026.0),
                RoomType.CENTRE_ADJACENT,
                ArrayList(game.doorManager.getDoorByType(DoorType.CENTRE)),
                Location(game.locationManager.getWorld(), -1995.0, 69.0, -2063.0)
            )
        )
        // Centre adjacent (Yellow)
        addRoom(
            Room(
                incrementRoomId(),
                Location(game.locationManager.getWorld(), -1974.0, 63.0, -2025.0),
                Location(game.locationManager.getWorld(), -1923.0, 92.0, -1975.0),
                RoomType.CENTRE_ADJACENT,
                ArrayList(game.doorManager.getDoorByType(DoorType.CENTRE)),
                Location(game.locationManager.getWorld(), -1937.0, 69.0, -1995.0)
            )
        )
        // Centre adjacent (Lime)
        addRoom(
            Room(
                incrementRoomId(),
                Location(game.locationManager.getWorld(), -2025.0, 63.0, -1974.0),
                Location(game.locationManager.getWorld(), -1975.0, 92.0, -1923.0),
                RoomType.CENTRE_ADJACENT,
                ArrayList(game.doorManager.getDoorByType(DoorType.CENTRE)),
                Location(game.locationManager.getWorld(), -2005.0, 69.0, -1937.0)
            )
        )
        // Centre adjacent (Blue)
        addRoom(
            Room(
                incrementRoomId(),
                Location(game.locationManager.getWorld(), -2077.0, 63.0, -2025.0),
                Location(game.locationManager.getWorld(), -2026.0, 92.0, -1975.0),
                RoomType.CENTRE_ADJACENT,
                ArrayList(game.doorManager.getDoorByType(DoorType.CENTRE)),
                Location(game.locationManager.getWorld(), -2063.0, 69.0, -2005.0)
            )
        )
        // Centre
        addRoom(
            Room(
                incrementRoomId(),
                Location(game.locationManager.getWorld(), -2025.0, 63.0, -2025.0),
                Location(game.locationManager.getWorld(), -1975.0, 92.0, -1975.0),
                RoomType.CENTRE,
                ArrayList(game.doorManager.getDoorByType(DoorType.CENTRE)),
                Location(game.locationManager.getWorld(), -2013.0, 69.0, -2013.0)
            )
        )
    }

    fun addRoom(room : Room) {
        roomList.add(room)
    }

    fun removeRoom(room : Room) {
        roomList.remove(room)
    }

    fun beginMeltingRoomType(roomType : RoomType) {
        for(room in roomList.filter { room -> room.roomType == roomType }) {
            if(!game.meltingRoomTask.getMeltingTaskMap().contains(room)) {
                game.meltingManager.startMelting(room)
            }
        }
    }

    private fun incrementRoomId() : Int {
        return latestRoomId++
    }

    fun getRoomsList() : ArrayList<Room> {
        return roomList
    }
}
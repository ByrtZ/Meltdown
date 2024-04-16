package dev.byrt.meltdown.data

import org.bukkit.Location

data class Room(val id : Int, val corner1 : Location, val corner2 : Location, val roomType : RoomType, var doors : ArrayList<Door>)

enum class RoomType {
    CENTRE,
    CENTRE_ADJACENT,
    CORNER,
    SPAWN
}

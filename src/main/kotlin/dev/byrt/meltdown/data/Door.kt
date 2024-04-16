package dev.byrt.meltdown.data

import org.bukkit.Location

data class Door(val id : Int, val corner1 : Location, val corner2 : Location, val doorType : DoorType, var doorStatus : DoorStatus)

enum class DoorType {
    CENTRE,
    CORNER_NW,
    COIN_CORNER_NE,
    CORNER_SE,
    COIN_CORNER_SW,
    SPAWN_RED,
    SPAWN_YELLOW,
    SPAWN_LIME,
    SPAWN_BLUE
}

enum class DoorStatus {
    OPEN,
    PRE_MELT_CLOSED,
    POST_MELT_CLOSED,
    CLOSING
}
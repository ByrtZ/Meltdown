package dev.byrt.meltdown.data

import org.bukkit.Location

data class Door(val id : Int, val corner1 : Location, val corner2 : Location, val doorType : DoorType, var doorStatus : DoorStatus)

enum class DoorType {
    CENTRE,
    DIVIDER,
    GENERIC
}

enum class DoorStatus {
    OPEN,
    PRE_MELT_CLOSED,
    POST_MELT_CLOSED,
    CLOSING
}
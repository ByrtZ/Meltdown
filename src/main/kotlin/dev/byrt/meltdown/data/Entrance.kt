package dev.byrt.meltdown.data

import dev.byrt.meltdown.state.Teams

import org.bukkit.Location
import org.bukkit.World

data class Entrance(val id : Int, val team : Teams, val world : World,
                    val x1 : Int, val x2 : Int, val startingY : Int, val z1 : Int, val z2 : Int,
                    val corner1 : Location, val corner2 : Location)
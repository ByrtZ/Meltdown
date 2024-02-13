package dev.byrt.meltdown.data

import dev.byrt.meltdown.state.Teams

import org.bukkit.Location

import java.util.*

data class Heater(val id : Int, val owner : UUID, val location : Location, val team : Teams) {
    companion object {
        const val HEATER_RADIUS = 3.5
    }
}
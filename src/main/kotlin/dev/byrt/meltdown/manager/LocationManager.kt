package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Teams

import org.bukkit.Location

class LocationManager(private val game : Game) {
    private var redSpawns = ArrayList<Location>()
    private var redSpawnCounter = 0
    private var blueSpawns = ArrayList<Location>()
    private var blueSpawnCounter = 0

    private val spawn = Location(game.plugin.server.getWorld("Cheese"), 0.5, -52.0 ,0.5, 0.0f, 0.0f)
    private val arenaCentre = Location(game.plugin.server.getWorld("Cheese"), 1000.5, 0.0, 1000.5, 0.0f, 0.0f)

    fun populateSpawns() {
        redSpawns.add(Location(game.plugin.server.getWorld("Cheese"), 949.5, 3.0, 999.5, -90.0f, 0.0f))
        redSpawns.add(Location(game.plugin.server.getWorld("Cheese"), 949.5, 3.0, 1001.5, -90.0f, 0.0f))
        redSpawns.add(Location(game.plugin.server.getWorld("Cheese"), 947.5, 3.0, 999.5, -90.0f, 0.0f))
        redSpawns.add(Location(game.plugin.server.getWorld("Cheese"), 947.5, 3.0, 1001.5, -90.0f, 0.0f))

        blueSpawns.add(Location(game.plugin.server.getWorld("Cheese"), 1051.5, 3.0, 1001.5, 90.0f, 0.0f))
        blueSpawns.add(Location(game.plugin.server.getWorld("Cheese"), 1051.5, 3.0, 999.5, 90.0f, 0.0f))
        blueSpawns.add(Location(game.plugin.server.getWorld("Cheese"), 1053.5, 3.0, 1001.5, 90.0f, 0.0f))
        blueSpawns.add(Location(game.plugin.server.getWorld("Cheese"), 1053.5, 3.0, 999.5, 90.0f, 0.0f))
    }

    fun incrementSpawnCounter(team : Teams) {
        when(team) {
            Teams.RED -> {
                if(redSpawnCounter >= redSpawns.size - 1) {
                    redSpawnCounter = 0
                } else {
                    redSpawnCounter++
                }
            }
            Teams.BLUE -> {
                if(blueSpawnCounter >= blueSpawns.size - 1) {
                    blueSpawnCounter = 0
                } else {
                    blueSpawnCounter++
                }
            } else -> {
            game.plugin.logger.severe("An error occurred when attempting to increment a spawn location counter.")
        }
        }
    }

    fun getRedSpawns() : ArrayList<Location> {
        return redSpawns
    }

    fun getRedSpawnCounter() : Int {
        return redSpawnCounter
    }

    fun getBlueSpawns() : ArrayList<Location> {
        return blueSpawns
    }


    fun getBlueSpawnCounter() : Int {
        return blueSpawnCounter
    }

    fun resetSpawnCounters() {
        redSpawnCounter = 0
        blueSpawnCounter = 0
    }

    fun getSpawn() : Location {
        return spawn
    }

    fun getArenaCentre() : Location {
        return arenaCentre
    }
}
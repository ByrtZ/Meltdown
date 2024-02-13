package dev.byrt.meltdown.manager

import dev.byrt.meltdown.data.Entrance
import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Teams

import org.bukkit.Location
import org.bukkit.World

class LocationManager(private val game : Game) {
    private val world = game.plugin.server.getWorld("Cheese")!!
    private val spawn = Location(game.plugin.server.getWorld("Cheese"), 0.5, -52.0 ,0.5, 0.0f, 0.0f)
    private val arenaCentre = Location(game.plugin.server.getWorld("Cheese"), 1000.5, 0.0, 1000.5, 0.0f, 0.0f)

    private var redSpawns = ArrayList<Location>()
    private var redSpawnCounter = 0
    private var yellowSpawns = ArrayList<Location>()
    private var yellowSpawnCounter = 0
    private var limeSpawns = ArrayList<Location>()
    private var limeSpawnCounter = 0
    private var blueSpawns = ArrayList<Location>()
    private var blueSpawnCounter = 0

    fun populateSpawns() {
        game.plugin.logger.info("Populating spawns...")
        redSpawns.add(Location(world, -1998.5, 70.0, -2132.5, 0.0f, 0.0f))
        /*redSpawns.add(Location(world, -1999.5, 70.0, -2133.5, 0.0f, 0.0f))
        redSpawns.add(Location(world, -2001.5, 70.0, -2133.5, 0.0f, 0.0f))
        redSpawns.add(Location(world, -2002.5, 70.0, -2132.5, 0.0f, 0.0f))*/

        yellowSpawns.add(Location(world, -1868.5, 70.0, -1998.5, 90.0f, 0.0f))
        /*yellowSpawns.add(Location(world, -1867.5, 70.0, -1999.5, 90.0f, 0.0f))
        yellowSpawns.add(Location(world, -1867.5, 70.0, -2001.5, 90.0f, 0.0f))
        yellowSpawns.add(Location(world, -1868.5, 70.0, -2002.5, 90.0f, 0.0f))*/

        limeSpawns.add(Location(world, -2002.5, 70.0, -1868.5, -180.0f, 0.0f))
        /*limeSpawns.add(Location(world, -2001.5, 70.0, -1867.5, -180.0f, 0.0f))
        limeSpawns.add(Location(world, -1999.5, 70.0, -1867.5, -180.0f, 0.0f))
        limeSpawns.add(Location(world, -1998.5, 70.0, -1868.5, -180.0f, 0.0f))*/

        blueSpawns.add(Location(world, -2132.5, 70.0, -2002.5, -90.0f, 0.0f))
        /*blueSpawns.add(Location(world, -2133.5, 70.0, -2001.5, -90.0f, 0.0f))
        blueSpawns.add(Location(world, -2133.5, 70.0, -1999.5, -90.0f, 0.0f))
        blueSpawns.add(Location(world, -2132.5, 70.0, -1998.5, -90.0f, 0.0f))*/
    }

    fun populateEntrances() {
        game.plugin.logger.info("Populating entrances..")
        /**Red entrance**/
        game.entranceManager.addEntrance(Entrance(
            game.entranceManager.incrementEntranceId(),
            Teams.RED, world,
            -2002, -1998,
            70,
            -2129, -2129,
            Location(world, -2002.0, 70.0, -2129.0),
            Location(world, -1998.0, 73.0, -2129.0)
            )
        )
        /**Yellow entrance**/
        game.entranceManager.addEntrance(Entrance(
            game.entranceManager.incrementEntranceId(),
            Teams.YELLOW, world,
            -1871, -1871,
            70,
            -2002, -1998,
            Location(world, -1871.0, 70.0, -2002.0),
            Location(world, -1871.0, 73.0, -1998.0)
            )
        )
        /**Lime entrance**/
        game.entranceManager.addEntrance(Entrance(
            game.entranceManager.incrementEntranceId(),
            Teams.LIME, world,
            -2002, -1998,
            70,
            -1871, -1871,
            Location(world, -2002.0, 70.0, -1871.0),
            Location(world, -1998.0, 73.0, -1871.0)
            )
        )
        /**Blue entrance**/
        game.entranceManager.addEntrance(Entrance(
            game.entranceManager.incrementEntranceId(),
            Teams.BLUE, world,
            -2129, -2129,
            70,
            -2002, -1998,
            Location(world, -2129.0, 70.0, -2002.0),
            Location(world, -2129.0, 73.0, -1998.0)
            )
        )
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
            Teams.YELLOW -> {
                if(yellowSpawnCounter >= yellowSpawns.size - 1) {
                    yellowSpawnCounter = 0
                } else {
                    yellowSpawnCounter++
                }
            }
            Teams.LIME -> {
                if(limeSpawnCounter >= limeSpawns.size - 1) {
                    limeSpawnCounter = 0
                } else {
                    limeSpawnCounter++
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

    fun getYellowSpawns() : ArrayList<Location> {
        return yellowSpawns
    }

    fun getYellowSpawnCounter() : Int {
        return yellowSpawnCounter
    }

    fun getLimeSpawns() : ArrayList<Location> {
        return limeSpawns
    }

    fun getLimeSpawnCounter() : Int {
        return limeSpawnCounter
    }

    fun getBlueSpawns() : ArrayList<Location> {
        return blueSpawns
    }


    fun getBlueSpawnCounter() : Int {
        return blueSpawnCounter
    }

    fun resetSpawnCounters() {
        redSpawnCounter = 0
        yellowSpawnCounter = 0
        limeSpawnCounter = 0
        blueSpawnCounter = 0
    }

    fun getSpawn() : Location {
        return spawn
    }

    fun getArenaCentre() : Location {
        return arenaCentre
    }

    fun getWorld() : World {
        return world
    }
}
package dev.byrt.meltdown.arena

import dev.byrt.meltdown.data.Entrance
import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Teams

import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable

class EntranceManager(private val game : Game) {
    private var entrancesList = ArrayList<Entrance>()
    private var latestEntranceId = 0
    private var entranceLoopMap = mutableMapOf<Int, BukkitRunnable>()

    fun populateEntrances() {
        game.plugin.logger.info("Populating entrances..")
        /**Red entrance**/
       addEntrance(Entrance(
            incrementEntranceId(),
            Teams.RED, game.locationManager.getWorld(),
            -2002, -1998,
            70,
            -2129, -2129,
            Location(game.locationManager.getWorld(), -2002.0, 70.0, -2129.0),
            Location(game.locationManager.getWorld(), -1998.0, 73.0, -2129.0)
            )
        )
        /**Yellow entrance**/
        addEntrance(Entrance(
            incrementEntranceId(),
            Teams.YELLOW, game.locationManager.getWorld(),
            -1871, -1871,
            70,
            -2002, -1998,
            Location(game.locationManager.getWorld(), -1871.0, 70.0, -2002.0),
            Location(game.locationManager.getWorld(), -1871.0, 73.0, -1998.0)
            )
        )
        /**Lime entrance**/
        addEntrance(Entrance(
            incrementEntranceId(),
            Teams.LIME, game.locationManager.getWorld(),
            -2002, -1998,
            70,
            -1871, -1871,
            Location(game.locationManager.getWorld(), -2002.0, 70.0, -1871.0),
            Location(game.locationManager.getWorld(), -1998.0, 73.0, -1871.0)
            )
        )
        /**Blue entrance**/
        addEntrance(Entrance(
            incrementEntranceId(),
            Teams.BLUE, game.locationManager.getWorld(),
            -2129, -2129,
            70,
            -2002, -1998,
            Location(game.locationManager.getWorld(), -2129.0, 70.0, -2002.0),
            Location(game.locationManager.getWorld(), -2129.0, 73.0, -1998.0)
            )
        )
    }

    private fun addEntrance(entrance : Entrance) {
        entrancesList.add(entrance)
    }

    private fun removeEntrance(entrance : Entrance) {
        entrancesList.remove(entrance)
    }

    fun openAllEntrances() {
        for(entrance in entrancesList) {
            openEntrance(entrance)
        }
    }

    private fun openEntrance(entrance : Entrance) {
        var entranceTickTimer = 0
        val openEntranceRunnable = object : BukkitRunnable() {
            override fun run() {
                if(entranceTickTimer == 0) {
                    game.blockManager.entranceOpenStageOne(entrance.team, entrance.world, entrance.x1, entrance.x2, entrance.startingY, entrance.z1, entrance.z2)
                }
                if(entranceTickTimer == 5) {
                    game.blockManager.entranceOpenStageTwo(entrance.team, entrance.world, entrance.x1, entrance.x2, entrance.startingY, entrance.z1, entrance.z2)
                }
                if(entranceTickTimer == 10) {
                    game.blockManager.entranceOpenStageThree(entrance.team, entrance.world, entrance.x1, entrance.x2, entrance.startingY, entrance.z1, entrance.z2)
                }
                if(entranceTickTimer >= 20) {
                    cancel()
                }
                entranceTickTimer++
            }
        }
        openEntranceRunnable.runTaskTimer(game.plugin, 0L, 1L)
        entranceLoopMap[entrance.id] = openEntranceRunnable
    }

    fun resetEntrances() {
        for(entrance in entrancesList) {
            game.blockManager.resetEntrance(entrance.team, entrance.corner1, entrance.corner2)
        }
    }

    private fun incrementEntranceId() : Int {
        return latestEntranceId++
    }

    fun getEntrancesList() : ArrayList<Entrance> {
        return entrancesList
    }
}
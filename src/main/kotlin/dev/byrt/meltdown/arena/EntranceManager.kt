package dev.byrt.meltdown.arena

import dev.byrt.meltdown.data.Entrance
import dev.byrt.meltdown.game.Game

import org.bukkit.scheduler.BukkitRunnable

class EntranceManager(private val game : Game) {
    private var entrancesList = ArrayList<Entrance>()
    private var latestEntranceId = 0
    private var entranceLoopMap = mutableMapOf<Int, BukkitRunnable>()

    fun addEntrance(entrance : Entrance) {
        entrancesList.add(entrance)
    }

    fun removeEntrance(entrance : Entrance) {
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

    fun incrementEntranceId() : Int {
        return latestEntranceId++
    }

    fun getEntrancesList() : ArrayList<Entrance> {
        return entrancesList
    }
}
package dev.byrt.meltdown.lobby

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.game.GameState

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.Bat
import org.bukkit.entity.EntityType

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*
import kotlin.collections.ArrayList

class LobbySecret(private val game : Game) {
    private var lobbySecretTaskMap = mutableMapOf<Int, BukkitRunnable>()
    private var latestLobbySecretTaskId = 0
    private var secretBatTaskMap = mutableMapOf<Int, BukkitRunnable>()
    private var latestSecretBatTaskId = 0
    private var randomColours = ArrayList<Color>()
    private var batSpawnLocations = ArrayList<Location>()
    private fun startLobbySecretTask() {
        var taskTicks = 0
        val lobbySecretRunnable = object : BukkitRunnable() {
            override fun run() {
                if(game.gameManager.getGameState() == GameState.IDLE) {
                    if(taskTicks % 5 == 0) {
                        startBatParticleTask()
                    }
                    if(taskTicks >= 20) {
                        taskTicks = 0
                    }
                    taskTicks++
                } else {
                    this.cancel()
                }
            }
        }
        lobbySecretRunnable.runTaskTimer(game.plugin, 0L, 1L)
        latestLobbySecretTaskId = lobbySecretRunnable.taskId
        lobbySecretTaskMap[lobbySecretRunnable.taskId] = lobbySecretRunnable
    }

    private fun startBatParticleTask() {
        var taskTicks = 0
        val bat = getBat()
        val secretBatRunnable = object : BukkitRunnable() {
            override fun run() {
                if(game.gameManager.getGameState() == GameState.IDLE) {
                    game.locationManager.getWorld().spawnParticle(Particle.REDSTONE, bat.location, 1, DustOptions(getRandomColour(), 0.75f))
                    taskTicks++
                    if(taskTicks >= 30) {
                        bat.remove()
                        stopBatParticleTask(this.taskId)
                    }
                } else {
                    stopBatParticleTask(this.taskId)
                }
            }
        }
        secretBatRunnable.runTaskTimer(game.plugin, 0L, 1L)
        latestSecretBatTaskId = secretBatRunnable.taskId
        secretBatTaskMap[secretBatRunnable.taskId] = secretBatRunnable
    }

    private fun stopBatParticleTask(secretBatRunnableId : Int) {
        secretBatTaskMap[secretBatRunnableId]?.cancel()
    }

    private fun getBat() : Bat {
        val bat = game.locationManager.getWorld().spawnEntity(getRandomSpawnLoc(), EntityType.BAT) as Bat
        bat.isAwake = true
        bat.isSilent = true
        bat.isInvisible = true
        bat.isInvulnerable = true
        bat.addScoreboardTag("lobby_secret")
        bat.velocity = Vector(0.0, 0.75, 0.0)
        return bat
    }

    private fun getRandomColour() : Color {
        val random = Random()
        return randomColours[random.nextInt(randomColours.size)]
    }

    private fun getRandomSpawnLoc() : Location {
        val random = Random()
        return batSpawnLocations[random.nextInt(batSpawnLocations.size)]
    }

    private fun populateColours() {
        // Dark reds
        randomColours.add(Color.fromRGB(128, 17, 9))
        randomColours.add(Color.fromRGB(107, 11, 4))
        randomColours.add(Color.fromRGB(79, 8, 2))
        randomColours.add(Color.fromRGB(128, 17, 9))
        randomColours.add(Color.fromRGB(59, 5, 1))
        randomColours.add(Color.fromRGB(153, 12, 2))
        randomColours.add(Color.fromRGB(138, 19, 11))

        // Blacks/dark greys
        randomColours.add(Color.fromRGB(41, 39, 39))
        randomColours.add(Color.fromRGB(33, 30, 30))
        randomColours.add(Color.fromRGB(28, 27, 27))
        randomColours.add(Color.fromRGB(23, 21, 21))
        randomColours.add(Color.fromRGB(10, 10, 10))
        randomColours.add(Color.fromRGB(0, 0, 0))

    }

    private fun populateSpawnLocations() {
        for(x in -2..5) {
            for(z in -57..-49) {
                val block = game.locationManager.getWorld().getBlockAt(x, -45, z)
                if(block.type == Material.GLOW_LICHEN) {
                    batSpawnLocations.add(block.location)
                }
            }
        }
    }

    fun beginLobbySecret() {
        populateColours()
        populateSpawnLocations()
        startLobbySecretTask()
    }

    fun reset() {
        for(bat in game.locationManager.getWorld().getEntitiesByClass(Bat::class.java)) {
            if(bat.scoreboardTags.contains("lobby_secret")) {
                bat.remove()
            }
        }
        for(task in lobbySecretTaskMap) {
            task.value.cancel()
        }
        for(task in secretBatTaskMap) {
            task.value.cancel()
        }
    }
}
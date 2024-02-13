package dev.byrt.meltdown.task

import dev.byrt.meltdown.data.Heater
import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Sounds
import dev.byrt.meltdown.state.Teams

import org.bukkit.*
import org.bukkit.scheduler.BukkitRunnable

import kotlin.math.cos
import kotlin.math.sin

class HeaterTask(private val game : Game) {
    private var heaterLoopMap = mutableMapOf<Heater, BukkitRunnable>()
    fun startHeaterLoop(heater : Heater) {
        game.heaterManager.addHeater(heater)
        val heaterRunnable = object : BukkitRunnable() {
            var heaterAliveSeconds = 0
            var heaterAliveTicks = 0
            override fun run() {
                if(heater.location.block.type != Material.NETHERITE_BLOCK) {
                    stopHeaterLoop(heater)
                } else {
                    if(heaterAliveTicks % 5 == 0) {
                        heaterParticleCircle(heater.location, heater.team)
                    }
                    if(heaterAliveTicks >= 20) {
                        heater.location.world.playSound(heater.location, Sounds.Heater.HEATER_LOOP, 1f, 1f)
                        heaterAliveTicks = 0
                        heaterAliveSeconds++
                    }
                    if(heaterAliveSeconds == 15) {
                        stopHeaterLoop(heater)
                    }
                    heaterAliveTicks++
                }
            }
        }
        heaterRunnable.runTaskTimer(game.plugin, 0L, 1L)
        heaterLoopMap[heater] = heaterRunnable
    }

    fun stopHeaterLoop(heater : Heater) {
        heaterLoopMap.remove(heater)?.cancel()
        game.heaterManager.removeHeater(heater)
        Bukkit.getPlayer(heater.owner)?.let { game.heaterManager.breakHeater(heater.location, it) }
    }

    fun getHeaterLoopMap() : Map<Heater, BukkitRunnable> {
        return heaterLoopMap
    }

    private fun heaterParticleCircle(location : Location, team : Teams) {
        object : BukkitRunnable() {
            override fun run() {
                for(i in 0 .. 20) {
                    val x = cos(i.toDouble()) * Heater.HEATER_RADIUS
                    val z = sin(i.toDouble()) * Heater.HEATER_RADIUS
                    location.world.spawnParticle(
                        Particle.REDSTONE,
                        location.x + x + 0.5,
                        location.y + 0.25,
                        location.z + z + 0.5,
                        1,
                        Particle.DustOptions(game.teamManager.getTeamColor(team), 1.25f)
                    )
                    if(i == 20) {
                        cancel()
                    }
                }
            }
        }.runTaskTimer(game.plugin, 0L, 1L)
    }
}
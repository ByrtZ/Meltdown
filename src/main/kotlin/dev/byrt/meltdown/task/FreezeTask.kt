package dev.byrt.meltdown.task

import dev.byrt.meltdown.data.Heater
import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

import java.util.*

class FreezeTask(private val game : Game) {
    private var freezeLoopMap  = mutableMapOf<UUID, BukkitRunnable>()
    private var frostVignetteTaskMap = mutableMapOf<UUID, BukkitRunnable>()
    fun startFreezeLoop(player: Player, frozenLoc: Location, team : Teams) {
        val freezeRunnable = object : BukkitRunnable() {
            var thawTimer = 5
            var isHeating = false
            override fun run() {
                if(!game.eliminatedTask.getEliminatedTeamTaskMap().containsKey(team)) {
                    if(game.heaterTask.getHeaterLoopMap().isNotEmpty()) {
                        for((heater, _) in game.heaterTask.getHeaterLoopMap()) {
                            val distance = frozenLoc.distance(heater.location)
                            if(distance <= Heater.HEATER_RADIUS && heater.team == team) {
                                isHeating = true
                                game.freezeManager.removePlayerTeamFrozen(player, team)
                                break
                            }
                        }

                        if(!isHeating) {
                            game.freezeManager.addPlayerTeamFrozen(player, team)
                            player.sendActionBar(Component.text("You are frozen.").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                            if(thawTimer < 5) {
                                thawTimer++
                            }
                        } else {
                            player.world.spawnParticle(Particle.FLAME, player.location, 10, 0.75, 0.75, 0.75, 0.1)
                            if(thawTimer < 1) {
                                player.sendActionBar(Component.text("You have been unfrozen!").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                                game.freezeManager.removePlayerTeamFrozen(player, team)
                                stopFreezeLoop(player, false)
                            } else {
                                player.sendActionBar(Component.text("You will thaw in ${thawTimer}s...").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                                thawTimer--
                            }
                        }
                    } else {
                        player.sendActionBar(Component.text("You are frozen.").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                        game.freezeManager.addPlayerTeamFrozen(player, team)
                        if(thawTimer < 5) {
                            thawTimer++
                        }
                        if(!game.freezeManager.getFrozenPlayers().contains(player.uniqueId)) {
                            stopFreezeLoop(player, true)
                        }
                    }
                }
            }
        }
        freezeRunnable.runTaskTimer(game.plugin, 0L, 20L)
        freezeLoopMap[player.uniqueId] = freezeRunnable
    }

    fun stopFreezeLoop(player : Player, forcefullyThawed : Boolean) {
        freezeLoopMap.remove(player.uniqueId)?.cancel()
        if(!forcefullyThawed) {
            game.freezeManager.unfreezePlayer(player)
        }
    }

    fun cancelFreezeLoop(player : Player) {
        freezeLoopMap.remove(player.uniqueId)?.cancel()
        game.freezeManager.setFrozenBlocks(player, Material.AIR)
        game.freezeManager.getFrozenPlayers().remove(player.uniqueId)
        game.playerManager.clearKit(player)
        stopFrostVignetteTask(player)
        game.freezeManager.resetFrostVignette(player)
        player.gameMode = GameMode.SPECTATOR
    }

    fun startFrostVignetteTask(player : Player) {
        val frostVignetteRunnable = object : BukkitRunnable() {
            var freezeTicks = 0
            override fun run() {
                player.freezeTicks = freezeTicks
                freezeTicks += 2
                if(freezeTicks >= 200 || !game.freezeManager.isFrozen(player)) {
                    player.freezeTicks = Int.MAX_VALUE
                    stopFrostVignetteTask(player)
                }
            }
        }
        frostVignetteRunnable.runTaskTimer(game.plugin, 0L, 1L)
        frostVignetteTaskMap[player.uniqueId] = frostVignetteRunnable
    }

    fun stopFrostVignetteTask(player : Player) {
        frostVignetteTaskMap[player.uniqueId]?.cancel()
    }
}
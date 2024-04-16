package dev.byrt.meltdown.task

import dev.byrt.meltdown.data.Heater
import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.manager.PlayerLifeState
import dev.byrt.meltdown.manager.ScoreModificationMode
import dev.byrt.meltdown.state.Sounds
import dev.byrt.meltdown.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

import java.util.*

class FreezeTask(private val game : Game) {
    private var freezeLoopMap  = mutableMapOf<UUID, BukkitRunnable>()
    private var isHeatingList = ArrayList<UUID>()
    private var frostVignetteTaskMap = mutableMapOf<UUID, BukkitRunnable>()
    fun startFreezeLoop(player : Player, freezer : Player?, frozenLoc : Location, team : Teams) {
        val freezeRunnable = object : BukkitRunnable() {
            var thawTimer = 6
            override fun run() {
                if(!game.eliminationManager.getEliminatedTeams().contains(team)) {
                    if(game.heaterTask.getHeaterLoopMap().isNotEmpty()) {
                        for((heater, _) in game.heaterTask.getHeaterLoopMap()) {
                            if(heater.team == team) {
                                val distance = frozenLoc.distanceSquared(heater.location)
                                if(distance <= Heater.HEATER_RADIUS * Heater.HEATER_RADIUS) {
                                    if(!isHeatingList.contains(player.uniqueId)) isHeatingList.add(player.uniqueId)
                                    game.eliminationManager.checkTeamStatus(team)
                                    break
                                } else {
                                    if(isHeatingList.contains(player.uniqueId)) isHeatingList.remove(player.uniqueId)
                                    game.eliminationManager.checkTeamStatus(team)
                                }
                            }
                        }
                        if(!game.eliminationManager.getFrozenPlayers().contains(player.uniqueId)) {
                            stopFreezeLoop(player, freezer, true)
                        }
                        if(!isHeatingList.contains(player.uniqueId)) {
                            game.eliminationManager.checkTeamStatus(team)
                            player.sendActionBar(Component.text("You are frozen.").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                            if(thawTimer < 6) {
                                thawTimer++
                            }
                        } else {
                            player.world.spawnParticle(Particle.FLAME, player.location, 10, 0.75, 0.75, 0.75, 0.1)
                            if(thawTimer < 1) {
                                player.sendActionBar(Component.text("You have been unfrozen!").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                                game.eliminationManager.checkTeamStatus(team)
                                stopFreezeLoop(player, freezer, false)
                            } else {
                                player.sendActionBar(Component.text("You will thaw in ${thawTimer}s...").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                                thawTimer--
                            }
                        }
                    } else {
                        player.sendActionBar(Component.text("You are frozen.").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                        isHeatingList.remove(player.uniqueId)
                        game.eliminationManager.checkTeamStatus(team)
                        if(thawTimer < 6) {
                            thawTimer++
                        }
                        if(!game.eliminationManager.getFrozenPlayers().contains(player.uniqueId)) {
                            stopFreezeLoop(player, freezer, true)
                        }
                    }
                } else {
                    player.sendActionBar(Component.text("Better luck next time.").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                }
            }
        }
        freezeRunnable.runTaskTimer(game.plugin, 0L, 20L)
        freezeLoopMap[player.uniqueId] = freezeRunnable
    }

    fun stopFreezeLoop(player : Player, freezer : Player?, forcefullyThawed : Boolean) {
        freezeLoopMap.remove(player.uniqueId)?.cancel()
        if(game.gameManager.getGameState() == GameState.IN_GAME && !game.eliminationManager.getEliminatedPlayers().contains(player)) {
            game.teamManager.sendTeamThawedMessage(
                Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text(player.name).color(game.teamManager.getPlayerTeam(player.uniqueId).textColor))
                    .append(Component.text(" was unfrozen.")),
                player,
                game.teamManager.getPlayerTeam(player.uniqueId)
            )

            if(freezer != null) {
                game.scoreManager.modifyScore(25, ScoreModificationMode.SUB, game.teamManager.getPlayerTeam(freezer.uniqueId))
                freezer.sendMessage(
                    Component.text("[")
                        .append(Component.text("-25"))
                        .append(Component.text(" coins", NamedTextColor.GOLD))
                        .append(Component.text("] ", NamedTextColor.WHITE))
                        .append(Component.text("["))
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text(player.name).color(game.teamManager.getPlayerTeam(player.uniqueId).textColor))
                        .append(Component.text(" was unfrozen.", NamedTextColor.RED)))
                freezer.playSound(freezer.location, Sounds.Score.UNDO_ELIMINATION, 1f, 0f)
            }
        }
        if(!forcefullyThawed) {
            game.freezeManager.unfreezePlayer(player)
        }
    }

    fun cancelFreezeLoop(player : Player) {
        game.freezeManager.setFrozenBlocks(player, Material.AIR)
        game.playerManager.clearKit(player)
        stopResetFrostVignetteTask(player)
        stopFreezeLoop(player, null, true)
        game.eliminationManager.changePlayerLifeState(player, PlayerLifeState.ELIMINATED)
    }

    fun getHeatingList() : ArrayList<UUID> {
        return isHeatingList
    }

    fun startFrostVignetteTask(player : Player) {
        val frostVignetteRunnable = object : BukkitRunnable() {
            var freezeTicks = 0
            override fun run() {
                player.freezeTicks = freezeTicks
                freezeTicks += 2
                if(freezeTicks >= 200 || !game.eliminationManager.isFrozen(player)) {
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

    private fun stopResetFrostVignetteTask(player : Player) {
        frostVignetteTaskMap[player.uniqueId]?.cancel()
        player.freezeTicks = 0
    }
}
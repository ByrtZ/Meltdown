package dev.byrt.meltdown.task

import dev.byrt.meltdown.game.Game

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

import java.util.*

class FreezeTask(private val game : Game) {
    private var freezeLoopMap  = mutableMapOf<UUID, BukkitRunnable>()
    fun startFreezeLoop(player : Player, frozenLoc: Location) {
        val freezeRunnable = object : BukkitRunnable() {
            var thawTimer = 5
            override fun run() {
                if(game.heaterTask.getHeaterLoopMap().isNotEmpty()) {
                    game.heaterTask.getHeaterLoopMap().forEach { heater ->
                        if(frozenLoc.distance(heater.key.location) <= 3.5 && heater.key.team == game.teamManager.getPlayerTeam(player.uniqueId)) {
                            player.world.spawnParticle(Particle.FLAME, player.location, 10, 0.75, 0.75, 0.75, 0.1)
                            if(thawTimer < 1) {
                                player.sendActionBar(Component.text("You have been unfrozen!").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                                stopFreezeLoop(player, false)
                            } else {
                                player.sendActionBar(Component.text("You will thaw in ${thawTimer}s...").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                                thawTimer--
                            }
                        } else {
                            player.sendActionBar(Component.text("You are frozen.").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                            if(thawTimer < 5) {
                                thawTimer++
                            }
                        }
                    }
                } else {
                    player.sendActionBar(Component.text("You are frozen.").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                    if(!game.freezeManager.getFrozenPlayers().contains(player.uniqueId)) {
                        stopFreezeLoop(player, true)
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
}
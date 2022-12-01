package me.byrt.meltdown.task

import me.byrt.meltdown.Main

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class FrozenLoopTask(player : Player, frozenPlayerLocation : Location, thawTime : Int) {
    private var frozenLoopTask : BukkitTask? = null
    init {
        if(frozenLoopTask != null) {
            frozenLoopTask!!.cancel()
        }
        frozenLoop(player, frozenPlayerLocation, thawTime)
    }

    private fun frozenLoop(player : Player, frozenPlayerLocation: Location, thawTime: Int) {
        frozenLoopTask = object : BukkitRunnable() {
            var thawTimer = thawTime
            override fun run() {
                if(Main.getGame().getHeaterManager().getHeaterLocations().isNotEmpty()) {
                    Main.getGame().getHeaterManager().getHeaterLocations().forEach { location ->
                        if(Main.getGame().getHeaterManager().getHeaterLocations().contains(location)) {
                            if(frozenPlayerLocation.distance(location) <= 3.5) {
                                player.world.spawnParticle(Particle.FLAME, player.location, 10, 0.75, 0.75, 0.75, 0.1)
                                if(thawTimer == 0) {
                                    player.sendActionBar(Component.text("You have been unfrozen!").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                                    Main.getGame().getFreezeManager().unfreezePlayer(player)
                                    this.cancel()
                                } else {
                                    player.sendActionBar(Component.text("You will unthaw in ${thawTimer}s...").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                                    thawTimer--
                                }
                            }
                        } else {
                            player.sendActionBar(Component.text("You are frozen.").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                            thawTimer = 5
                            this.cancel()
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20L)
    }
}
package me.byrt.meltdown.manager

import me.byrt.meltdown.Main

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.*
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

import kotlin.math.cos
import kotlin.math.sin

@Suppress("unused")
class HeaterManager(private val game : Game) {
    private var heaterLocations = ArrayList<Location>()
    private var heaterLoopTask : BukkitTask? = null

    fun placeHeater(placementLocation : Location, placer : Player) {
        heaterLocations.add(placementLocation)
        placementLocation.block.type = Material.NETHERITE_BLOCK
        placer.playSound(placementLocation, "heater_place", 1f, 1f)
        placer.sendMessage(Component.text("You placed your heater!").color(NamedTextColor.GOLD))
        if(heaterLoopTask != null) {
            heaterLoopTask!!.cancel()
        }
        heaterLoop()
    }

    fun breakHeater(destructionLocation : Location, destructor : Player) {
        heaterLocations.remove(destructionLocation)
        destructionLocation.block.type = Material.AIR
        destructor.playSound(destructionLocation, "heater_break", 1f, 1f)
        destructor.sendMessage(Component.text("You broke a heater!").color(NamedTextColor.GOLD))
    }

    private fun heaterLoop() {
        heaterLoopTask = object : BukkitRunnable() {
            override fun run() {
                if(Main.getGame().getHeaterManager().heaterLocations.isNotEmpty()) {
                    Main.getGame().getHeaterManager().heaterLocations.forEach { location ->
                        var d = 0
                        while (Main.getGame().getHeaterManager().heaterLocations.isNotEmpty()) {
                            if(!Main.getGame().getHeaterManager().heaterLocations.contains(location)) {
                                this.cancel()
                            } else {
                                val x = cos(d.toDouble()) * 3
                                val z = sin(d.toDouble()) * 3
                                location.world.spawnParticle(Particle.REDSTONE, location.x + x + 0.5, location.y + 0.25, location.z + z + 0.5, 1, DustOptions(Color.ORANGE, 2F))
                                d+= 1
                                if(d == 20) {
                                    break
                                }
                            }
                        }
                    }
                } else {
                    this.cancel()
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 10L)
    }

    fun getHeaterLocations(): ArrayList<Location> {
        return this.heaterLocations
    }
}
package dev.byrt.meltdown.event

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState

import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Arrow
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.scheduler.BukkitRunnable

@Suppress("unused")
class ProjectileMoveEvent : Listener {
    @EventHandler
    private fun onProjectileMove(e : ProjectileLaunchEvent) {
        if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME || Main.getGame().gameManager.getGameState() == GameState.OVERTIME) {
            if(e.entity is Arrow) {
                val arrow = e.entity as Arrow
                object : BukkitRunnable() {
                    override fun run() {
                        if(!arrow.isDead) {
                            arrow.isCritical = false
                            arrow.location.world.spawnParticle(
                                Particle.DUST,
                                arrow.location,
                                4,
                                Particle.DustOptions(Color.WHITE, 1f)
                            )
                        } else {
                            this.cancel()
                        }
                    }
                }.runTaskTimer(Main.getPlugin(), 0L, 1L)
            }
        }
    }
}
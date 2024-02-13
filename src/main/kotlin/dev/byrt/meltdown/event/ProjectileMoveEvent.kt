package dev.byrt.meltdown.event

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState

import io.papermc.paper.event.entity.EntityMoveEvent

import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Arrow
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Suppress("unused")
class ProjectileMoveEvent : Listener {
    @EventHandler
    private fun onProjectileMove(e : EntityMoveEvent) {
        if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME || Main.getGame().gameManager.getGameState() == GameState.OVERTIME) {
            if(e.entity is Arrow) {
                val arrow = e.entity as Arrow
                arrow.location.world.spawnParticle(
                    Particle.REDSTONE,
                    arrow.location.x,
                    arrow.location.y,
                    arrow.location.z,
                    3,
                    Particle.DustOptions(Color.WHITE, 1f)
                )
            }
        }
    }
}
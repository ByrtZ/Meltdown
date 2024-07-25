package dev.byrt.meltdown.event

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.state.Sounds

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

@Suppress("unused")
class PlayerShootBowEvent : Listener {
    @EventHandler
    private fun onBowShoot(e : EntityShootBowEvent) {
        if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME || Main.getGame().gameManager.getGameState() == GameState.OVERTIME) {
            if(e.entity is Player && !Main.getGame().lifestates.isFrozen(e.entity as Player)) {
                val player = e.entity as Player
                player.world.playSound(player.location, Sounds.Bow.FROST_BOW_SHOOT, 1.0f, 1.0f)
            } else {
                e.isCancelled = true
            }
        } else {
            e.isCancelled = true
        }
    }
}
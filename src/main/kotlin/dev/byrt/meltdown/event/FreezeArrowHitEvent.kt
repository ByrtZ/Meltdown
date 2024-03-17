package dev.byrt.meltdown.event

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState

import org.bukkit.GameMode
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent

@Suppress("unused")
class FreezeArrowHitEvent : Listener {
    @EventHandler
    private fun onFreezeArrowHit(e : ProjectileHitEvent) {
        if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME || Main.getGame().gameManager.getGameState() == GameState.OVERTIME) {
            if(e.entity is Arrow && e.hitEntity is Player && e.entity.shooter is Player && (e.hitEntity as Player).player?.gameMode == GameMode.SURVIVAL) {
                val player = e.hitEntity as Player
                val shooter = e.entity.shooter as Player
                if(!Main.getGame().teamManager.isSpectator(player.uniqueId)) {
                    if(!Main.getGame().eliminationManager.getFrozenPlayers().contains(player.uniqueId) && player.fireTicks < 0) {
                        Main.getGame().freezeManager.freezePlayer(player, shooter)
                        e.entity.remove()
                    } else {
                        e.entity.remove()
                        e.isCancelled = true
                    }
                } else {
                    e.entity.remove()
                    e.isCancelled = true
                }
            }

            if(e.entity is Arrow && e.hitBlock != null) {
                e.entity.remove()
            }
        } else {
            e.entity.remove()
            e.isCancelled = true
        }
    }
}
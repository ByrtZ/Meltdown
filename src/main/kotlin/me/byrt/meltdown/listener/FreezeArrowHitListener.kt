package me.byrt.meltdown.listener

import me.byrt.meltdown.Main
import me.byrt.meltdown.manager.GameState

import org.bukkit.GameMode
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent

@Suppress("unused")
class FreezeArrowHitListener : Listener {
    @EventHandler
    private fun onFreezeArrowHit(e : ProjectileHitEvent) {
        if(Main.getGame().getGameState() == GameState.IN_GAME) {
            if(e.entity is Arrow && e.hitEntity is Player && e.entity.shooter is Player && (e.hitEntity as Player).player?.gameMode == GameMode.SURVIVAL) {
                if(!Main.getGame().getFreezeManager().getFrozenPlayers().contains(e.hitEntity?.uniqueId)) {
                    Main.getGame().getFreezeManager().freezePlayer(e.hitEntity as Player)
                    Main.getGame().getFreezeManager().freezePlayerDisplay(e.hitEntity as Player, e.entity.shooter as Player)
                }
            }

            if(e.entity is Arrow && e.hitBlock != null) {
                e.entity.remove()
            }
        } else {
            e.isCancelled = true
        }
    }
}
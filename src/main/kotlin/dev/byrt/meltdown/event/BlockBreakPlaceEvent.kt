package dev.byrt.meltdown.event

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

@Suppress("unused")
class BlockBreakPlaceEvent : Listener {
    @EventHandler
    private fun onBlockBreak(e : BlockBreakEvent) {
        e.isCancelled = !(Main.getGame().getBuildMode() && e.player.isOp && Main.getGame().gameManager.getGameState() == GameState.IDLE)
    }

    @EventHandler
    private fun onBlockPlace(e : BlockPlaceEvent) {
        e.isCancelled = !(Main.getGame().getBuildMode() && e.player.isOp && Main.getGame().gameManager.getGameState() == GameState.IDLE)
    }
}
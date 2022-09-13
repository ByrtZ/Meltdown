package me.byrt.meltdown.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

@Suppress("unused")
class BlockBreakPlaceListener : Listener {
    @EventHandler
    private fun onBlockBreak(e : BlockBreakEvent) {
        e.isCancelled = true
    }

    @EventHandler
    private fun onBlockPlace(e : BlockPlaceEvent) {
        e.isCancelled = true
    }
}
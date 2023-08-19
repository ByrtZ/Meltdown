package dev.byrt.meltdown.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

@Suppress("unused")
class PlayerDropItemsEvent : Listener {
    @EventHandler
    private fun onDropItem(e : PlayerDropItemEvent) {
        e.isCancelled = true
    }
}
package dev.byrt.meltdown.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent

@Suppress("unused")
class ExplosionEvent : Listener {
    @EventHandler
    private fun onExplosion(e : EntityExplodeEvent) {
        e.blockList().clear()
    }
}
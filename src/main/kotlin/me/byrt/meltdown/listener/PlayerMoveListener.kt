package me.byrt.meltdown.listener

import me.byrt.meltdown.Main

import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

@Suppress("unused")
class PlayerMoveListener : Listener {
    @EventHandler
    private fun onPlayerMove(e : PlayerMoveEvent) {
        if(Main.getGame().getFreezeManager().getFrozenPlayers().contains(e.player.uniqueId)) {
            val to: Location = e.from
            to.pitch = e.to.pitch
            to.yaw = e.to.yaw
            e.to = to
        }
    }
}
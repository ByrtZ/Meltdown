package dev.byrt.meltdown.event

import dev.byrt.meltdown.Main

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent

@Suppress("unused")
class FishEvent : Listener {
    @EventHandler
    private fun onFish(e : PlayerFishEvent) {
        e.hook.minWaitTime = 80
        e.hook.maxWaitTime = 140
        e.hook.isSkyInfluenced = true
        e.expToDrop = 0

        if(e.state == PlayerFishEvent.State.CAUGHT_FISH) {
            e.caught?.remove()
            val randomDrop = Main.getGame().lobbyFishing.rollRandomDrop()
            val item = Main.getGame().locationManager.getWorld().dropItem(e.hook.location, randomDrop)
            e.player.playPickupItemAnimation(item)
            e.player.inventory.addItem(randomDrop)
        }
    }
}
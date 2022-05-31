package me.byrt.meltdown.listener

import me.byrt.meltdown.Main

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

@Suppress("unused")
class PlaceBreakHeaterListener : Listener {
    @EventHandler
    private fun placeHeater(e : PlayerInteractEvent) {
        if(e.action == Action.RIGHT_CLICK_BLOCK && e.clickedBlock?.type != Material.AIR && e.blockFace == BlockFace.UP && e.player.inventory.itemInMainHand.type == Material.NETHERITE_SHOVEL) {
            e.player.inventory.remove(Material.NETHERITE_SHOVEL)
            val heaterPlacementLocation = Location(e.clickedBlock?.world, e.clickedBlock?.location!!.x, e.clickedBlock?.location!!.y + 1, e.clickedBlock?.location!!.z)
            Main.getGame().getHeaterManager().placeHeater(heaterPlacementLocation, e.player)
        }
    }

    @EventHandler
    private fun breakHeater(e : PlayerInteractEvent) {
        if(e.action == Action.LEFT_CLICK_BLOCK && e.clickedBlock?.type == Material.NETHERITE_BLOCK) {
            e.player.inventory.addItem(ItemStack(Material.NETHERITE_SHOVEL))
            Main.getGame().getHeaterManager().breakHeater(e.clickedBlock!!.location, e.player)
        }
    }
}
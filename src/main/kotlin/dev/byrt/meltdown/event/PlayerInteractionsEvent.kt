package dev.byrt.meltdown.event

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

@Suppress("unused")
class PlayerInteractionsEvent : Listener {
    @EventHandler
    private fun interact(e : PlayerInteractEvent) {
        if(e.action == Action.RIGHT_CLICK_BLOCK &&
            e.clickedBlock?.type != Material.AIR &&
            e.blockFace == BlockFace.UP &&
            e.player.inventory.itemInMainHand.type == Material.NETHERITE_SHOVEL
            && e.player.gameMode == GameMode.SURVIVAL &&
            !e.player.hasCooldown(Material.NETHERITE_SHOVEL) &&
            !Main.getGame().freezeManager.isFrozen(e.player) &&
                Main.getGame().gameManager.getGameState() == GameState.IN_GAME || Main.getGame().gameManager.getGameState() == GameState.OVERTIME) {
            Main.getGame().heaterManager.placeHeater(Location(e.clickedBlock?.world, e.clickedBlock?.location!!.x, e.clickedBlock?.location!!.y + 1, e.clickedBlock?.location!!.z), e.player)
        }
        if(e.action == Action.LEFT_CLICK_BLOCK && e.clickedBlock?.type == Material.NETHERITE_BLOCK && e.player.gameMode == GameMode.SURVIVAL && !Main.getGame().freezeManager.isFrozen(e.player) &&
            Main.getGame().gameManager.getGameState() == GameState.IN_GAME || Main.getGame().gameManager.getGameState() == GameState.OVERTIME) {
            val clickedBlock = e.clickedBlock as Block
            for(heater in Main.getGame().heaterManager.getHeaterList()) {
                if(heater.location.block.type == clickedBlock.type) {
                    clickedBlock.type = Material.AIR
                } else {
                    e.isCancelled = true
                }
            }
        }
        if(e.action.isRightClick
            && e.clickedBlock?.blockData is Openable
            || e.clickedBlock?.blockData is Directional
            || e.clickedBlock?.blockData is Orientable
            || e.clickedBlock?.blockData is Rotatable
            || e.clickedBlock?.blockData is Powerable
            || e.clickedBlock?.type == Material.FLOWER_POT
            || e.clickedBlock?.type == Material.BEACON
            || e.clickedBlock?.type?.name?.startsWith("POTTED_") == true) {
            e.isCancelled = true
        }
    }
}
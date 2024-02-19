package dev.byrt.meltdown.event

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.*
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

@Suppress("unused")
class PlayerInteractionsEvent : Listener {
    @EventHandler
    private fun interact(e : PlayerInteractEvent) {
        if(Main.getGame().gameManager.getGameState() != GameState.IDLE) {
            if(e.action == Action.RIGHT_CLICK_BLOCK &&
                e.clickedBlock?.type != Material.AIR &&
                e.clickedBlock?.type != Material.LIGHT_BLUE_STAINED_GLASS &&
                e.blockFace == BlockFace.UP &&
                e.clickedBlock?.getRelative(BlockFace.UP)?.type == Material.AIR &&
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
                    if(heater.location == clickedBlock.location) {
                        clickedBlock.type = Material.AIR
                    } else {
                        e.isCancelled = true
                    }
                }
            }
            if(e.action.isRightClick && e.player.gameMode == GameMode.SURVIVAL && e.player.inventory.itemInMainHand.type == Material.GRAY_DYE && !e.player.hasCooldown(Material.GRAY_DYE) && (Main.getGame().gameManager.getGameState() == GameState.IN_GAME || Main.getGame().gameManager.getGameState() == GameState.OVERTIME)) {
                Main.getGame().sharedItemManager.setTeamTelepickaxeOwner(e.player, Main.getGame().teamManager.getPlayerTeam(e.player.uniqueId), false)
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
        } else {
            if(Main.getGame().gameManager.getGameState() == GameState.IDLE && e.action.isRightClick && e.player.inventory.itemInMainHand.type == Material.RED_DYE) {
                Main.getGame().queue.leaveQueue(e.player)
                e.isCancelled = true
            } else {
                e.isCancelled = !(e.player.isOp && Main.getGame().getBuildMode())
            }
        }
    }

    @EventHandler
    private fun onInteract(e : PlayerInteractEntityEvent) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE && e.rightClicked.type == EntityType.CHICKEN) {
            if(Main.getGame().cooldownManager.attemptJoinQueue(e.player) && !Main.getGame().queue.getQueue().contains(e.player.uniqueId)) {
                if(e.rightClicked.scoreboardTags.contains("meltdown.queue.npc") && !Main.getGame().queue.getQueue().contains(e.player.uniqueId)) {
                    Main.getGame().queue.joinQueue(e.player)
                }
            }
        }
    }
}
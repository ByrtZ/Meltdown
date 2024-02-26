package dev.byrt.meltdown.event

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.manager.ScoreModificationMode
import dev.byrt.meltdown.state.Sounds

import io.papermc.paper.event.block.BlockBreakBlockEvent

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.block.BlockPlaceEvent

@Suppress("unused")
class BlockBreakPlaceDropEvent : Listener {
    @EventHandler
    private fun onBlockBreak(e : BlockBreakEvent) {
        if(e.player.inventory.itemInMainHand.type == Material.DEBUG_STICK || e.player.inventory.itemInMainHand.type == Material.WOODEN_AXE) {
            e.isCancelled = true
        } else {
            if(Main.getGame().gameManager.getGameState() != GameState.IN_GAME || Main.getGame().gameManager.getGameState() != GameState.OVERTIME) {
                e.isCancelled = !(Main.getGame().getBuildMode() && e.player.isOp)
            }
            if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME || Main.getGame().gameManager.getGameState() == GameState.OVERTIME) {
                if(e.block.type == Material.RAW_GOLD_BLOCK && e.player.inventory.itemInMainHand.type == Material.NETHERITE_PICKAXE) {
                    Main.getGame().scoreManager.modifyScore(10, ScoreModificationMode.ADD, Main.getGame().teamManager.getPlayerTeam(e.player.uniqueId))
                    Main.getGame().teamManager.playTeamSound(Sounds.Score.MINE_COIN_CRATE, 2f, Main.getGame().teamManager.getPlayerTeam(e.player.uniqueId))
                    e.isCancelled = false
                } else {
                    e.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    private fun onBlockPlace(e : BlockPlaceEvent) {
        e.isCancelled = !(Main.getGame().getBuildMode() && e.player.isOp && Main.getGame().gameManager.getGameState() == GameState.IDLE)
    }

    @EventHandler
    private fun onBlockItemDrop(e : BlockDropItemEvent) {
        e.isCancelled = true
    }

    @EventHandler
    private fun onBlockBreakBlock(e : BlockBreakBlockEvent) {
        e.drops.clear()
    }
}
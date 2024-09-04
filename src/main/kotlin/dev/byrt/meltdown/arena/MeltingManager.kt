package dev.byrt.meltdown.arena

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.data.Room
import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.game.GameState

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.BlockData
import org.bukkit.scheduler.BukkitRunnable

import kotlin.random.Random

class MeltingManager(private val game : Game) {
    private val meltedBlocks = mutableSetOf<Triple<Material, BlockData, Location>>()
    private val meltableBlockTypes = ArrayList<Material>()

    fun startMelting(room : Room) {
        game.meltingRoomTask.startMeltingTask(room)
    }

    fun stopMelting(room : Room) {
        game.meltingRoomTask.stopMeltingTask(room)
    }

    fun melt(block: Block) {
        object : BukkitRunnable() {
            override fun run() {
                if(game.gameManager.getGameState() == GameState.IN_GAME || game.gameManager.getGameState() == GameState.OVERTIME) {
                    if(meltedBlocks.contains(Triple(block.type, block.blockData, block.location))) {
                        cancel()
                    } else {
                        if(meltableBlockTypes.contains(block.type)) {
                            meltedBlocks.add(Triple(block.type, block.blockData, block.location))
                            meltBlockStageOne(block)
                            val meltableBlocks = getMeltableBlocksRelative(block)
                            if(meltableBlocks.isEmpty()) {
                                cancel()
                            } else {
                                for(relativeBlock in meltableBlocks) {
                                    melt(relativeBlock)
                                }
                            }
                        } else {
                            cancel()
                        }
                    }
                } else {
                    cancel()
                }
            }
        }.runTaskLater(game.plugin, Random.nextLong(15L, 35L))
    }

    fun meltBlockStageOne(block: Block) {
        object : BukkitRunnable() {
            override fun run() {
                if(game.gameManager.getGameState() == GameState.IN_GAME || game.gameManager.getGameState() == GameState.OVERTIME) {
                    block.type = Material.BLACK_CONCRETE
                    meltBlockStageTwo(block)
                } else {
                    cancel()
                }
            }
        }.runTaskLater(game.plugin, Random.nextLong(10L,15L))
    }

    fun meltBlockStageTwo(block: Block) {
        object : BukkitRunnable() {
            override fun run() {
                if(game.gameManager.getGameState() == GameState.IN_GAME || game.gameManager.getGameState() == GameState.OVERTIME) {
                    block.type = Material.AIR
                } else {
                    cancel()
                }
            }
        }.runTaskLater(game.plugin, Random.nextLong(35L, 55L))
    }

    fun getMeltableBlocksRelative(block: Block) : ArrayList<Block> {
        val blocks = ArrayList<Block>()
        val faces = listOf(BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH)
        for(face in faces) {
            val relativeBlock = block.getRelative(face)
            if(meltableBlockTypes.contains(relativeBlock.type)) {
                blocks.add(relativeBlock)
            }
        }
        return blocks
    }

    fun getMeltedBlocks() : Set<Triple<Material, BlockData, Location>> {
        return meltedBlocks
    }

    fun resetMeltedBlocks() {
        for(melted in meltedBlocks) {
            Bukkit.getWorld("Cheese")!!.getBlockAt(melted.third).type = melted.first
            Bukkit.getWorld("Cheese")!!.getBlockAt(melted.third).blockData = melted.second
        }
        meltedBlocks.clear()
    }

    fun getMeltableBlockTypes(): ArrayList<Material> {
        return meltableBlockTypes
    }

    fun reloadMeltableBlocksList() {
        meltableBlockTypes.clear()
        val meltable = Main.getGame().configManager.getMeltableConfig().getStringList("meltable") as ArrayList<String>
        meltable.forEach { name -> Material.getMaterial(name)?.let { meltableBlockTypes.add(it) } }
    }
}
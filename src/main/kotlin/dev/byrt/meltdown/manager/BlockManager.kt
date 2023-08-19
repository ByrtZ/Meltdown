package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game

import org.bukkit.Bukkit.getWorld
import org.bukkit.Material

class BlockManager(private val game : Game) {
    fun removeBarriers() {
        for (z in 999..1001) { // Removing red barrier
            for (y in 3..5) {
                getWorld("Cheese")?.getBlockAt(950, y, z)?.type = Material.AIR
            }
        }
        for (z in 999..1001) { // Removing blue barrier
            for (y in 3..5) {
                getWorld("Cheese")?.getBlockAt(1050, y, z)?.type = Material.AIR
            }
        }
    }

    private fun resetBarriers() {
        for (z in 999..1001) { // Resetting red barrier
            for (y in 3..5) {
                getWorld("Cheese")?.getBlockAt(950, y, z)?.type = Material.BARRIER
            }
        }
        for (z in 999..1001) { // Resetting blue barrier
            for (y in 3..5) {
                getWorld("Cheese")?.getBlockAt(1050, y, z)?.type = Material.BARRIER
            }
        }
    }

    fun resetAllBlocks() {
        resetBarriers()
    }
}
package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game

import org.bukkit.entity.Player

import java.util.*

class CooldownManager(private val game : Game) {
    private var queueCooldown = HashMap<UUID, Long>()
    private val queueCooldownTime = 1500
    fun attemptJoinQueue(player : Player) : Boolean {
        return if(!queueCooldown.containsKey(player.uniqueId) || System.currentTimeMillis() - queueCooldown[player.uniqueId]!! > queueCooldownTime) {
            queueCooldown[player.uniqueId] = System.currentTimeMillis()
            true
        } else {
            false
        }
    }
}
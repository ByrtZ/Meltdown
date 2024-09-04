package dev.byrt.meltdown.manager

import dev.byrt.meltdown.data.Heater
import dev.byrt.meltdown.data.HeaterBreakReason
import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Sounds

import org.bukkit.*
import org.bukkit.entity.Player

class HeaterManager(private val game : Game) {
    private var heaterList = mutableSetOf<Heater>()
    private var latestHeaterId = 0
    fun placeHeater(location : Location, player : Player) {
        player.inventory.remove(Material.NETHERITE_SHOVEL)
        location.block.type = Material.NETHERITE_BLOCK
        player.world.playSound(location, Sounds.Heater.HEATER_PLACE, 1f, 1f)
        game.heaterTask.startHeaterLoop(Heater(incrementHeaterId(), player.uniqueId, location, game.teamManager.getPlayerTeam(player.uniqueId)))
    }

    fun breakHeater(location : Location, owner : Player, breakReason : HeaterBreakReason) {
        if(owner.gameMode != GameMode.SPECTATOR && !game.lifestates.isFrozen(owner)) {
            game.itemManager.giveHeaterItem(owner)
            owner.setCooldown(Material.NETHERITE_SHOVEL, 10 * 20)
        }
        owner.sendMessage(breakReason.reason)
        location.world.playSound(location, Sounds.Heater.HEATER_BREAK, 1f, 1f)
        location.world.spawnParticle(Particle.CRIT, location, 12, 0.75, 0.75, 0.75)
        location.block.type = Material.AIR
    }

    fun stopAllHeaters() {
        val aliveHeaters = game.heaterTask.getHeaterLoopMap().keys
        for(heater in aliveHeaters) {
            game.heaterTask.stopHeaterLoop(heater, HeaterBreakReason.NOT_IN_GAME)
        }
    }

    fun isHeaterActive(player : Player) : Boolean {
        for(heater in game.heaterTask.getHeaterLoopMap().keys) {
            return heater.owner == player.uniqueId
        }
        return false
    }

    private fun incrementHeaterId() : Int {
        return latestHeaterId++
    }

    fun addHeater(heater : Heater) {
        heaterList.add(heater)
    }

    fun removeHeater(heater : Heater) {
        heaterList.remove(heater)
    }

    fun getPlayerHeater(player : Player) : Heater? {
        for(heater in heaterList) {
            if(heater.owner == player.uniqueId) {
                return heater
            }
        }
        return null
    }

    fun getHeaterList() : Set<Heater> {
        return heaterList
    }
}
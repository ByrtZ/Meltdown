package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Teams

import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class PlayerManager(private var game : Game) {
    fun setPlayersNotFlying() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.allowFlight }.forEach {
                player: Player -> disableFlightPlayers(player)
        }
    }

    private fun disableFlightPlayers(player: Player) {
        if (player.gameMode == GameMode.ADVENTURE || player.gameMode == GameMode.SURVIVAL) {
            player.allowFlight = false
            player.isFlying = false
        }
    }

    fun setPlayersFlying() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> !player.allowFlight }.forEach {
                player: Player -> enableFlightPlayers(player)
        }
    }

    private fun enableFlightPlayers(player: Player) {
        if (player.gameMode == GameMode.ADVENTURE || player.gameMode == GameMode.SURVIVAL) {
            player.velocity = Vector(0.0, 0.5, 0.0)
            player.allowFlight = true
            player.isFlying = true
        }
    }

    fun giveItemsToPlayers() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> giveItems(player) }
    }

    private fun giveItems(player: Player) {
        if(game.teamManager.getPlayerTeam(player.uniqueId) != Teams.SPECTATOR) {
            game.itemManager.givePlayerKit(player)
        }
    }

    fun clearNonBootsItems() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> clearKit(player) }
    }

    fun clearKit(player : Player) {
        player.inventory.remove(Material.NETHERITE_SHOVEL)
        player.inventory.remove(Material.BOW)
        player.inventory.remove(Material.ARROW)
        player.inventory.setItemInOffHand(null)
    }

    private fun clearAllItems() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.inventory.clear()
        }
    }

    fun teleportPlayersToGame() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player?.let {
            game.teamManager.getPlayerTeam(it.uniqueId) } != Teams.SPECTATOR}
            .forEach{ player: Player -> teleportPlayers(player) }
    }

    private fun teleportPlayers(player : Player) {
        if(game.teamManager.isInRedTeam(player.uniqueId)) {
            for(redPlayerUUID in game.teamManager.getRedTeam()) {
                val redPlayer = Bukkit.getPlayer(redPlayerUUID)
                redPlayer!!.teleport(game.locationManager.getRedSpawns()[game.locationManager.getRedSpawnCounter()])
                game.locationManager.incrementSpawnCounter(Teams.RED)
            }
        }
        if(game.teamManager.isInBlueTeam(player.uniqueId)) {
            for(bluePlayerUUID in game.teamManager.getBlueTeam()) {
                val bluePlayer = Bukkit.getPlayer(bluePlayerUUID)
                bluePlayer!!.teleport(game.locationManager.getBlueSpawns()[game.locationManager.getBlueSpawnCounter()])
                game.locationManager.incrementSpawnCounter(Teams.BLUE)
            }
        }
    }

    fun teleportSpectatorsToArena() {
        for(player in Bukkit.getOnlinePlayers()) {
            if(game.teamManager.getPlayerTeam(player.uniqueId) == Teams.SPECTATOR) {
                player.teleport(game.locationManager.getArenaCentre())
            }
        }
    }

    private fun teleportPlayersToSpawn() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.teleport(game.locationManager.getSpawn())
        }
    }

    fun setSpectatorsGameMode() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player?.let {
            game.teamManager.getPlayerTeam(it.uniqueId) } == Teams.SPECTATOR}
            .forEach{ player: Player -> player.gameMode = GameMode.SPECTATOR }
    }

    fun setPlayersSurvival() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player?.let {
            game.teamManager.getPlayerTeam(it.uniqueId) } != Teams.SPECTATOR}
            .forEach{ player: Player -> player.gameMode = GameMode.SURVIVAL }
    }

    fun setPlayersAdventure() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player?.let {
            game.teamManager.getPlayerTeam(it.uniqueId) } != Teams.SPECTATOR}
            .forEach{ player: Player -> player.gameMode = GameMode.ADVENTURE }
    }

    private fun setAllAdventure() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.gameMode = GameMode.ADVENTURE
        }
    }

    fun resetPlayers() {
        clearAllItems()
        setPlayersNotFlying()
        setAllAdventure()
        teleportPlayersToSpawn()
    }
}
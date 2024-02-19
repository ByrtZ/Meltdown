package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Sounds
import dev.byrt.meltdown.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

import java.time.Duration
import java.util.*

import kotlin.collections.ArrayList

class FreezeManager(private var game : Game) {
    private var frozenPlayers = ArrayList<UUID>()
    private var redFrozenPlayers = ArrayList<Player>()
    private var yellowFrozenPlayers = ArrayList<Player>()
    private var limeFrozenPlayers = ArrayList<Player>()
    private var blueFrozenPlayers = ArrayList<Player>()

    fun freezePlayer(player : Player, shooter : Player?) {
        if(frozenPlayers.contains(player.uniqueId)) return
        frozenPlayers.add(player.uniqueId)
        val freezeLoc = findNearestFreezeLocation(player)
        player.teleport(freezeLoc)
        setFrozenBlocks(player, Material.LIGHT_BLUE_STAINED_GLASS)
        player.damage(0.01)
        player.playSound(player.location, Sounds.Freeze.FROZEN_1, 5f, 1f)
        player.playSound(player.location, Sounds.Freeze.FROZEN_2, 1f, 1f)
        game.playerManager.clearKit(player)
        game.freezeTask.startFreezeLoop(player, freezeLoc, game.teamManager.getPlayerTeam(player.uniqueId))
        game.freezeTask.startFrostVignetteTask(player)

        if(shooter != null) {
            freezePlayerDisplay(player, shooter)
        }
    }

    private fun freezePlayerDisplay(frozenPlayer : Player, shooter : Player) {
        shooter.sendMessage(
            Component.text("[")
                .append(Component.text("▶").color(NamedTextColor.YELLOW))
                .append(Component.text("] "))
                .append(Component.text("["))
                .append(Component.text("\uD83C\uDFF9").color(NamedTextColor.AQUA))
                .append(Component.text("] You froze "))
                .append(Component.text(frozenPlayer.name).color(game.teamManager.getPlayerTeam(frozenPlayer.uniqueId).textColor))
                .append(Component.text("!")))
        shooter.playSound(shooter.location, Sounds.Score.ELIMINATION, 1f, 1.25f)

        game.teamManager.sendTeamFrozenMessage(
            Component.text("[")
                .append(Component.text("▶").color(NamedTextColor.YELLOW))
                .append(Component.text("] "))
                .append(Component.text(frozenPlayer.name).color(game.teamManager.getPlayerTeam(frozenPlayer.uniqueId).textColor))
                .append(Component.text(" was frozen by "))
                .append(Component.text(shooter.name).color(game.teamManager.getPlayerTeam(shooter.uniqueId).textColor))
                .append(Component.text(".", NamedTextColor.WHITE)),
            game.teamManager.getPlayerTeam(frozenPlayer.uniqueId),
            game.teamManager.getPlayerTeam(shooter.uniqueId)
        )

        frozenPlayer.showTitle(Title.title(
            Component.text("FROZEN").color(NamedTextColor.AQUA),
            Component.text(""),
            Title.Times.times(
                Duration.ofSeconds(0),
                Duration.ofSeconds(4),
                Duration.ofSeconds(1),
                )
            )
        )
    }

    fun unfreezePlayer(player : Player) {
        if(!frozenPlayers.contains(player.uniqueId)) return
        frozenPlayers.remove(player.uniqueId)
        setFrozenBlocks(player, Material.AIR)
        resetFrostVignette(player)
        player.playSound(player.location, Sounds.Freeze.UNFREEZE, 1f, 1f)
        player.setCooldown(Material.BOW, 4 * 20)
        player.setCooldown(Material.NETHERITE_SHOVEL, 10 * 20)
        player.fireTicks = 4 * 20

        game.itemManager.giveFrostBowItem(player)
        game.itemManager.giveFrostArrowItem(player)
        if(!game.heaterManager.isHeaterActive(player)) {
            game.itemManager.giveHeaterItem(player)
        }
        if(player == game.sharedItemManager.getTeamTelepickaxeOwner(player)) {
            game.itemManager.giveUsableTelepickaxe(player)
        } else {
            game.itemManager.giveUnusableTelepickaxe(player)
        }

        game.teamManager.sendTeamThawedMessage(
            Component.text("[")
                .append(Component.text("▶").color(NamedTextColor.YELLOW))
                .append(Component.text("] "))
                .append(Component.text(player.name).color(game.teamManager.getPlayerTeam(player.uniqueId).textColor))
                .append(Component.text(" was unfrozen.")),
            player,
            game.teamManager.getPlayerTeam(player.uniqueId)
        )
    }

    private fun findNearestFreezeLocation(player : Player) : Location {
        val searchRadius = 4
        var closestLocation = player.location
        var closestDistanceSquared = Double.MAX_VALUE
        for(x in -searchRadius..searchRadius) {
            for(y in -searchRadius - 8..searchRadius) {
                for(z in -searchRadius..searchRadius) {
                    val targetLocation = player.location.clone().add(x.toDouble(), y.toDouble(), z.toDouble())
                    val block = targetLocation.block
                    val blockAbove = targetLocation.clone().add(0.0, 1.0, 0.0).block
                    val blockBelow = targetLocation.clone().subtract(0.0, 1.0, 0.0).block
                    if(block.type == Material.AIR && blockAbove.type == Material.AIR && blockBelow.isSolid && !blockBelow.type.name.endsWith("_SLAB")) {
                        val distanceSquared = player.location.distanceSquared(targetLocation)
                        if(distanceSquared < closestDistanceSquared) {
                            closestLocation = targetLocation.clone()
                            closestDistanceSquared = distanceSquared
                        }
                    }
                }
            }
        }
        return Location(closestLocation.world, closestLocation.blockX.toDouble() + 0.5, closestLocation.blockY.toDouble(), closestLocation.blockZ.toDouble() + 0.5, player.location.yaw, player.location.pitch)
    }

    fun setFrozenBlocks(player : Player, material : Material) {
        val playerLoc = Location(player.world, player.location.blockX.toDouble(), player.location.blockY.toDouble(), player.location.blockZ.toDouble())
        val playerEyeLoc = Location(player.world, player.eyeLocation.blockX.toDouble(), player.eyeLocation.blockY.toDouble(), player.eyeLocation.blockZ.toDouble())
        playerLoc.block.type = material
        playerEyeLoc.block.type = material
    }

    fun resetFrostVignette(player : Player) {
        player.freezeTicks = 0
    }

    fun getFrozenPlayers() : ArrayList<UUID> {
        return frozenPlayers
    }

    fun addPlayerTeamFrozen(player : Player, team : Teams) {
        when(team) {
            Teams.RED -> {
                if(redFrozenPlayers.contains(player)) return
                redFrozenPlayers.add(player)
                if(redFrozenPlayers.size == game.teamManager.getRedTeam().filter{uuid -> Bukkit.getPlayer(uuid)?.gameMode == GameMode.SURVIVAL}.size) {
                    game.eliminatedTask.startEliminateTeamTask(redFrozenPlayers, team)
                }
            }
            Teams.YELLOW -> {
                if(yellowFrozenPlayers.contains(player)) return
                yellowFrozenPlayers.add(player)
                if(yellowFrozenPlayers.size == game.teamManager.getYellowTeam().filter{uuid -> Bukkit.getPlayer(uuid)?.gameMode == GameMode.SURVIVAL}.size) {
                    game.eliminatedTask.startEliminateTeamTask(yellowFrozenPlayers, team)
                }
            }
            Teams.LIME -> {
                if(limeFrozenPlayers.contains(player)) return
                limeFrozenPlayers.add(player)
                if(limeFrozenPlayers.size == game.teamManager.getLimeTeam().filter{uuid -> Bukkit.getPlayer(uuid)?.gameMode == GameMode.SURVIVAL}.size) {
                    game.eliminatedTask.startEliminateTeamTask(limeFrozenPlayers, team)
                }
            }
            Teams.BLUE -> {
                if(blueFrozenPlayers.contains(player)) return
                blueFrozenPlayers.add(player)
                if(blueFrozenPlayers.size == game.teamManager.getBlueTeam().filter{uuid -> Bukkit.getPlayer(uuid)?.gameMode == GameMode.SURVIVAL}.size) {
                    game.eliminatedTask.startEliminateTeamTask(blueFrozenPlayers, team)
                }
            }
            Teams.SPECTATOR -> {
                // nuh uh.
            }
        }
    }

    fun removePlayerTeamFrozen(player : Player, team : Teams) {
        when(team) {
            Teams.RED -> {
                if(!redFrozenPlayers.contains(player)) return
                redFrozenPlayers.remove(player)
            }
            Teams.YELLOW -> {
                if(!yellowFrozenPlayers.contains(player)) return
                yellowFrozenPlayers.remove(player)
            }
            Teams.LIME -> {
                if(!limeFrozenPlayers.contains(player)) return
                limeFrozenPlayers.remove(player)
            }
            Teams.BLUE -> {
                if(!blueFrozenPlayers.contains(player)) return
                blueFrozenPlayers.remove(player)
            }
            Teams.SPECTATOR -> {
                // nuh uh.
            }
        }
    }

    fun isFrozen(player : Player) : Boolean {
        return frozenPlayers.contains(player.uniqueId)
    }

    fun resetTeamFrozenLists() {
        redFrozenPlayers.clear()
        yellowFrozenPlayers.clear()
        limeFrozenPlayers.clear()
        blueFrozenPlayers.clear()
    }
}
package me.byrt.meltdown.manager

import me.byrt.meltdown.Main
import me.byrt.meltdown.task.FrozenLoopTask

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

import java.time.Duration
import java.util.*

import kotlin.collections.ArrayList

class FreezeManager(private var game : Game) {
    private var frozenPlayers = ArrayList<UUID>()

    fun freezePlayer(player : Player) {
        player.freezeTicks = Int.MAX_VALUE
        player.playSound(player.location, "event.meltdown_frozen", 1f, 1f)
        val freezeLoc = Location(player.world, player.location.blockX.toDouble(), player.location.blockY.toDouble(), player.location.blockZ.toDouble(), player.location.yaw, player.location.pitch)
        val freezeEyeLoc = Location(player.world, player.eyeLocation.blockX.toDouble(), player.eyeLocation.blockY.toDouble(), player.eyeLocation.blockZ.toDouble())
        freezeLoc.block.type = Material.LIGHT_BLUE_STAINED_GLASS
        freezeEyeLoc.block.type = Material.LIGHT_BLUE_STAINED_GLASS
        player.teleport(freezeLoc)
        frozenPlayers.add(player.uniqueId)
        startFrozenLoop(player, freezeLoc)
    }

    fun freezePlayerDisplay(frozenPlayer : Player, shooter : Player) {
        shooter.sendMessage(Component.text("You froze ${frozenPlayer.name}!").color(NamedTextColor.AQUA))
        shooter.playSound(shooter.location, "event.scoreacquired", 1f, 1f)
        frozenPlayer.sendMessage(Component.text("You were frozen by ${shooter.name}!").color(NamedTextColor.AQUA))
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
        player.freezeTicks = 0
        player.playSound(player.location, "event.meltdown_unfrozen", 1f, 1f)
        val playerLoc = Location(player.world, player.location.blockX.toDouble(), player.location.blockY.toDouble(), player.location.blockZ.toDouble())
        val playerEyeLoc = Location(player.world, player.eyeLocation.blockX.toDouble(), player.eyeLocation.blockY.toDouble(), player.eyeLocation.blockZ.toDouble())
        playerLoc.block.type = Material.AIR
        playerEyeLoc.block.type = Material.AIR
        frozenPlayers.remove(player.uniqueId)
    }

    private fun startFrozenLoop(player : Player, frozenPlayerLocation : Location) {
        val frozenLoop = FrozenLoopTask(player, frozenPlayerLocation, 5)
        Main.getPlugin().logger.info("Started new Frozen loop with $player, at $frozenPlayerLocation.\nFrozen Loop information: $frozenLoop")
    }

    fun getFrozenPlayers() : ArrayList<UUID> {
        return this.frozenPlayers
    }

    fun isFrozen(player : Player) : Boolean {
        return frozenPlayers.contains(player.uniqueId)
    }
}
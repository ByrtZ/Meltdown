package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Sounds

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

import java.time.Duration

class FreezeManager(private var game : Game) {
    fun freezePlayer(player : Player, shooter : Player?) {
        game.lifestates.changePlayerLifeState(player, PlayerLifeState.FROZEN)
        val freezeLoc = findNearestFreezeLocation(player)
        player.teleport(freezeLoc)
        setFrozenBlocks(player, Material.LIGHT_BLUE_STAINED_GLASS)
        player.damage(0.01)
        player.playSound(player.location, Sounds.Freeze.FROZEN, 1f, 1f)
        game.playerManager.clearKit(player)
        game.freezeTask.startFreezeLoop(player, shooter, freezeLoc, game.teamManager.getPlayerTeam(player.uniqueId))
        game.freezeTask.startFrostVignetteTask(player)
        freezePlayerDisplay(player, shooter)
    }

    private fun freezePlayerDisplay(frozenPlayer : Player, shooter : Player?) {
        frozenPlayer.showTitle(Title.title(
            Component.text("FROZEN").color(NamedTextColor.AQUA),
            Component.text(""),
            Title.Times.times(
                Duration.ofSeconds(1),
                Duration.ofSeconds(3),
                Duration.ofSeconds(1),
                )
            )
        )
        if(shooter != null) {
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
            shooter.sendMessage(
                Component.text("[")
                    .append(Component.text("+25"))
                    .append(Component.text(" coins", NamedTextColor.GOLD))
                    .append(Component.text("] ", NamedTextColor.WHITE))
                    .append(Component.text("["))
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("["))
                    .append(Component.text("\uD83C\uDFF9").color(NamedTextColor.GREEN))
                    .append(Component.text("] You froze "))
                    .append(Component.text(frozenPlayer.name).color(game.teamManager.getPlayerTeam(frozenPlayer.uniqueId).textColor))
                    .append(Component.text("!")))
            shooter.playSound(shooter.location, Sounds.Score.ACQUIRED, 1f, 1f)
            game.scoreManager.modifyScore(25, ScoreModificationMode.ADD, game.teamManager.getPlayerTeam(shooter.uniqueId))
        }
    }

    fun unfreezePlayer(player : Player) {
        game.lifestates.changePlayerLifeState(player, PlayerLifeState.ALIVE)
        setFrozenBlocks(player, Material.AIR)
        resetFrostVignette(player)
        player.playSound(player.location, Sounds.Freeze.UNFREEZE, 1f, 1f)
        player.setCooldown(Material.BOW, 4 * 20)
        player.setCooldown(Material.NETHERITE_SHOVEL, 10 * 20)
        player.fireTicks = 4 * 20

        game.itemManager.giveFrostBowItem(player)
        game.itemManager.giveFrostArrowItem(player)
        game.itemManager.giveMapItem(player)
        if(!game.heaterManager.isHeaterActive(player)) {
            game.itemManager.giveHeaterItem(player)
        }
        if(player == game.sharedItemManager.getTeamTelepickaxeOwner(player)) {
            game.itemManager.giveUsableTelepickaxe(player)
        } else {
            game.itemManager.giveUnusableTelepickaxe(player)
        }
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
        if(material == Material.LIGHT_BLUE_STAINED_GLASS || material == Material.AIR && playerLoc.block.type == Material.LIGHT_BLUE_STAINED_GLASS && playerEyeLoc.block.type == Material.LIGHT_BLUE_STAINED_GLASS) {
            playerLoc.block.type = material
            playerEyeLoc.block.type = material
        }
    }

    private fun resetFrostVignette(player : Player) {
        player.freezeTicks = 0
    }
}
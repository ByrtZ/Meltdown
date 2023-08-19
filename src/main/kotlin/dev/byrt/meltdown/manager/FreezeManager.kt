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
import java.util.*

import kotlin.collections.ArrayList

class FreezeManager(private var game : Game) {
    private var frozenPlayers = ArrayList<UUID>()
    fun freezePlayer(player : Player) {
        frozenPlayers.add(player.uniqueId)
        val freezeLoc = Location(player.world, player.location.blockX.toDouble() + 0.5, player.location.blockY.toDouble(), player.location.blockZ.toDouble() + 0.5, player.location.yaw, player.location.pitch)
        val freezeEyeLoc = Location(player.world, player.eyeLocation.blockX.toDouble(), player.eyeLocation.blockY.toDouble(), player.eyeLocation.blockZ.toDouble())
        player.teleport(freezeLoc)
        freezeLoc.block.type = Material.LIGHT_BLUE_STAINED_GLASS
        freezeEyeLoc.block.type = Material.LIGHT_BLUE_STAINED_GLASS
        player.damage(0.01)
        player.freezeTicks = Int.MAX_VALUE
        player.playSound(player.location, Sounds.Freeze.FROZEN, 1f, 1f)
        game.playerManager.clearKit(player)
        game.freezeTask.startFreezeLoop(player, freezeLoc)
    }

    fun freezePlayerDisplay(frozenPlayer : Player, shooter : Player) {
        shooter.sendMessage(Component.text("You froze ${frozenPlayer.name}!").color(NamedTextColor.AQUA))
        shooter.playSound(shooter.location, Sounds.Score.ELIMINATION, 1f, 1f)
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
        frozenPlayers.remove(player.uniqueId)
        val playerLoc = Location(player.world, player.location.blockX.toDouble(), player.location.blockY.toDouble(), player.location.blockZ.toDouble())
        val playerEyeLoc = Location(player.world, player.eyeLocation.blockX.toDouble(), player.eyeLocation.blockY.toDouble(), player.eyeLocation.blockZ.toDouble())
        playerLoc.block.type = Material.AIR
        playerEyeLoc.block.type = Material.AIR
        player.freezeTicks = 0
        player.playSound(player.location, Sounds.Freeze.UNFREEZE, 1f, 1f)
        player.setCooldown(Material.BOW, 4 * 20)
        player.setCooldown(Material.NETHERITE_SHOVEL, 10 * 20)
        player.fireTicks = 4 * 20

        if(game.heaterManager.isHeaterActive(player)) {
            game.itemManager.giveFrostBowItem(player)
            game.itemManager.giveFrostArrowItem(player)
        } else {
            game.itemManager.givePlayerKit(player)
        }
    }

    fun getFrozenPlayers() : ArrayList<UUID> {
        return this.frozenPlayers
    }

    fun isFrozen(player : Player) : Boolean {
        return frozenPlayers.contains(player.uniqueId)
    }

    /*
    public static Location getGroundLocationAt(Location location)
    {
        // Get the world of the location (can be null)
        final World world = location.getWorld();

        // Get the highest block in this world or null if no world
        final Block highest = world != null ? world.getHighestBlockAt(location).getRelative(BlockFace.DOWN) : null; // Get the highest block in this world or null if no world

        // If the highest block is not null and under the given location keep it if not get the block at given location
        Block block = highest != null && highest.getY() < location.getY() ? highest : location.getBlock();

        // Iterate all block under location until we find a solid block or reach Y == 0
        while(!block.getType().isSolid() && block.getLocation().getY() >= 0)
        {
            // Get the block under the current block
            block = block.getRelative(BlockFace.DOWN);
        }

        // Create a new Location instance with de Y of the block found or Y of given location if no block found
        return new Location(location.getWorld(), location.getX(), block.getY() >= 0 ? block.getY() + 1 : location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }*/
}
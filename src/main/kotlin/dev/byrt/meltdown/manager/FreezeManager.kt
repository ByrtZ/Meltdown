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
        frozenPlayers.add(player.uniqueId)
        val freezeLoc = Location(player.world, player.location.blockX.toDouble() + 0.5, player.location.blockY.toDouble(), player.location.blockZ.toDouble() + 0.5, player.location.yaw, player.location.pitch)
        player.teleport(freezeLoc)
        setFrozenBlocks(player, Material.LIGHT_BLUE_STAINED_GLASS)
        player.damage(0.01)
        player.playSound(player.location, Sounds.Freeze.FROZEN, 1f, 1f)
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
        shooter.playSound(shooter.location, Sounds.Score.ELIMINATION, 1f, 1f)

        game.teamManager.sendTeamFrozenMessage(
            Component.text("[")
                .append(Component.text("▶").color(NamedTextColor.YELLOW))
                .append(Component.text("] "))
                .append(Component.text(frozenPlayer.name).color(game.teamManager.getPlayerTeam(frozenPlayer.uniqueId).textColor))
                .append(Component.text(" was frozen by "))
                .append(Component.text(shooter.name).color(game.teamManager.getPlayerTeam(shooter.uniqueId).textColor)),
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
        frozenPlayers.remove(player.uniqueId)
        setFrozenBlocks(player, Material.AIR)
        resetFrostVignette(player)
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

    //TODO: To find suitable location to freeze player, loop around each applicable relative blockface to find air and a solid block underneath with two blocks of air above that
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
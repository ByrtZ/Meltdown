package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team

import java.util.*

class TeamManager(private val game : Game) {
    private var redTeam = ArrayList<UUID>()
    private var blueTeam = ArrayList<UUID>()
    private var spectators = ArrayList<UUID>()
    private var redDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("redDisplay")
    private var blueDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("blueDisplay")
    private var spectatorDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("spectator")
    private var adminDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("admin")

    fun addToTeam(player : Player, uuid : UUID, team : Teams) {
        when(team) {
            Teams.RED -> {
                if(blueTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.BLUE) }
                if(spectators.contains(uuid)) { removeFromTeam(player, uuid, Teams.SPECTATOR) }
                redTeam.add(uuid)
                redDisplayTeam.addPlayer(Bukkit.getOfflinePlayer(uuid))
                player.sendMessage(Component.text("You are now on ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Red Team")
                        .color(NamedTextColor.RED))
                    .append(Component.text(".")))
                game.itemManager.givePlayerTeamBoots(player, Teams.RED)
            }
            Teams.BLUE -> {
                if(redTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.RED) }
                if(spectators.contains(uuid)) { removeFromTeam(player, uuid, Teams.SPECTATOR) }
                blueTeam.add(uuid)
                blueDisplayTeam.addPlayer(Bukkit.getOfflinePlayer(uuid))
                player.sendMessage(Component.text("You are now on ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Blue Team")
                        .color(NamedTextColor.BLUE))
                    .append(Component.text(".")))
                game.itemManager.givePlayerTeamBoots(player, Teams.BLUE)
            }
            Teams.SPECTATOR -> {
                if(redTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.RED) }
                if(blueTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.BLUE) }
                spectators.add(uuid)
                if(player.isOp) {
                    adminDisplayTeam.addPlayer(Bukkit.getOfflinePlayer(uuid))
                } else {
                    spectatorDisplayTeam.addPlayer(Bukkit.getOfflinePlayer(uuid))
                }
                game.itemManager.givePlayerTeamBoots(player, Teams.SPECTATOR)
                player.sendMessage(Component.text("You are now a Spectator."))
            }
        }
    }

    fun removeFromTeam(player : Player, uuid : UUID, team : Teams) {
        when(team) {
            Teams.RED -> {
                redTeam.remove(uuid)
                redDisplayTeam.removePlayer(Bukkit.getOfflinePlayer(uuid))
                player.sendMessage(Component.text("You are no longer on ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Red Team")
                        .color(NamedTextColor.RED))
                    .append(Component.text(".")))
            }
            Teams.BLUE -> {
                blueTeam.remove(uuid)
                blueDisplayTeam.removePlayer(Bukkit.getOfflinePlayer(uuid))
                player.sendMessage(Component.text("You are no longer on ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Blue Team")
                        .color(NamedTextColor.BLUE))
                    .append(Component.text(".")))
            }
            Teams.SPECTATOR -> {
                spectators.remove(uuid)
                spectatorDisplayTeam.removePlayer(Bukkit.getOfflinePlayer(uuid))
                player.sendMessage(Component.text("You are no longer a Spectator."))
            }
        }
    }

    fun shuffle(players : Collection<Player>) {
        var i = 0
        players.shuffled().forEach {
            removeFromTeam(it, it.uniqueId, getPlayerTeam(it.uniqueId))
            if (i % 2 == 0) {
                addToTeam(it, it.uniqueId, Teams.RED)
            } else {
                addToTeam(it, it.uniqueId, Teams.BLUE)
            }
            i++
        }
    }

    fun addToAdminDisplay(uuid : UUID) {
        adminDisplayTeam.addPlayer(Bukkit.getOfflinePlayer(uuid))
    }

    fun buildDisplayTeams() {
        redDisplayTeam.color(NamedTextColor.RED)
        redDisplayTeam.prefix(Component.text("\uD004 ").color(NamedTextColor.WHITE))
        redDisplayTeam.suffix(Component.text("").color(NamedTextColor.WHITE))
        redDisplayTeam.displayName(Component.text("Red").color(NamedTextColor.RED))
        redDisplayTeam.setAllowFriendlyFire(false)

        blueDisplayTeam.color(NamedTextColor.BLUE)
        blueDisplayTeam.prefix(Component.text("\uD005 ").color(NamedTextColor.WHITE))
        blueDisplayTeam.suffix(Component.text("").color(NamedTextColor.WHITE))
        blueDisplayTeam.displayName(Component.text("Blue").color(NamedTextColor.BLUE))
        blueDisplayTeam.setAllowFriendlyFire(false)

        adminDisplayTeam.color(NamedTextColor.DARK_RED)
        adminDisplayTeam.prefix(Component.text("\uD002 ").color(NamedTextColor.WHITE))
        adminDisplayTeam.suffix(Component.text("").color(NamedTextColor.WHITE))
        adminDisplayTeam.displayName(Component.text("Admin").color(NamedTextColor.DARK_RED))
        adminDisplayTeam.setAllowFriendlyFire(false)

        spectatorDisplayTeam.color(NamedTextColor.GRAY)
        spectatorDisplayTeam.prefix(Component.text("\uD003 ").color(NamedTextColor.WHITE))
        spectatorDisplayTeam.suffix(Component.text("").color(NamedTextColor.WHITE))
        spectatorDisplayTeam.displayName(Component.text("Spectator").color(NamedTextColor.GRAY))
        spectatorDisplayTeam.setAllowFriendlyFire(false)
    }

    fun showDisplayTeamNames() {
        redDisplayTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS)
        blueDisplayTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS)
    }

    fun hideDisplayTeamNames() {
        redDisplayTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS)
        blueDisplayTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS)
    }

    fun destroyDisplayTeams() {
        redDisplayTeam.unregister()
        blueDisplayTeam.unregister()
        adminDisplayTeam.unregister()
        spectatorDisplayTeam.unregister()
    }

    fun getPlayerTeam(uuid : UUID): Teams {
        return if(redTeam.contains(uuid)) {
            Teams.RED
        } else if(blueTeam.contains(uuid)) {
            Teams.BLUE
        } else {
            Teams.SPECTATOR
        }
    }

    fun getTeamNamedTextColor(player : Player) : NamedTextColor {
        if(isInRedTeam(player.uniqueId)) {
            return NamedTextColor.RED
        }
        if(isInBlueTeam(player.uniqueId)) {
            return NamedTextColor.BLUE
        }
        return NamedTextColor.GRAY
    }

    fun getTeamColor(player : Player) : Color {
        if(isInRedTeam(player.uniqueId)) {
            return Color.RED
        }
        if(isInBlueTeam(player.uniqueId)) {
            return Color.BLUE
        }
        return Color.GRAY
    }

    fun getTeamColor(team : Teams) : Color {
        return when(team) {
            Teams.RED -> {
                Color.RED
            }
            Teams.BLUE -> {
                Color.BLUE
            }
            Teams.SPECTATOR -> {
                Color.GRAY
            }
        }
    }

    fun isInRedTeam(uuid : UUID): Boolean {
        return redTeam.contains(uuid)
    }

    fun isInBlueTeam(uuid : UUID): Boolean {
        return blueTeam.contains(uuid)
    }

    fun isSpectator(uuid : UUID): Boolean {
        return spectators.contains(uuid)
    }

    fun getRedTeam(): ArrayList<UUID> {
        return this.redTeam
    }

    fun getBlueTeam(): ArrayList<UUID> {
        return this.blueTeam
    }

    fun getSpectators(): ArrayList<UUID> {
        return this.spectators
    }
}
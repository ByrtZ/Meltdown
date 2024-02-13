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
    private var yellowTeam = ArrayList<UUID>()
    private var limeTeam = ArrayList<UUID>()
    private var blueTeam = ArrayList<UUID>()
    private var spectators = ArrayList<UUID>()
    private var redDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("redDisplay")
    private var yellowDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("yellowDisplay")
    private var limeDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("limeDisplay")
    private var blueDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("blueDisplay")
    private var spectatorDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("spectator")
    private var adminDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("admin")

    fun addToTeam(player : Player, uuid : UUID, team : Teams) {
        when(team) {
            Teams.RED -> {
                if(yellowTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.YELLOW) }
                if(limeTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.LIME) }
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
            Teams.YELLOW -> {
                if(redTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.RED) }
                if(limeTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.LIME) }
                if(blueTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.BLUE) }
                if(spectators.contains(uuid)) { removeFromTeam(player, uuid, Teams.SPECTATOR) }
                yellowTeam.add(uuid)
                yellowDisplayTeam.addPlayer(Bukkit.getOfflinePlayer(uuid))
                player.sendMessage(Component.text("You are now on ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Yellow Team")
                        .color(NamedTextColor.YELLOW))
                    .append(Component.text(".")))
                game.itemManager.givePlayerTeamBoots(player, Teams.YELLOW)
            }
            Teams.LIME -> {
                if(redTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.RED) }
                if(yellowTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.LIME) }
                if(blueTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.BLUE) }
                if(spectators.contains(uuid)) { removeFromTeam(player, uuid, Teams.SPECTATOR) }
                limeTeam.add(uuid)
                limeDisplayTeam.addPlayer(Bukkit.getOfflinePlayer(uuid))
                player.sendMessage(Component.text("You are now on ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Lime Team")
                        .color(NamedTextColor.GREEN))
                    .append(Component.text(".")))
                game.itemManager.givePlayerTeamBoots(player, Teams.LIME)
            }
            Teams.BLUE -> {
                if(redTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.RED) }
                if(yellowTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.YELLOW) }
                if(limeTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.LIME) }
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
                if(yellowTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.YELLOW) }
                if(limeTeam.contains(uuid)) { removeFromTeam(player, uuid, Teams.LIME) }
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
            Teams.YELLOW -> {
                yellowTeam.remove(uuid)
                yellowDisplayTeam.removePlayer(Bukkit.getOfflinePlayer(uuid))
                player.sendMessage(Component.text("You are no longer on ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Yellow Team")
                        .color(NamedTextColor.YELLOW))
                    .append(Component.text(".")))
            }
            Teams.LIME -> {
                limeTeam.remove(uuid)
                limeDisplayTeam.removePlayer(Bukkit.getOfflinePlayer(uuid))
                player.sendMessage(Component.text("You are no longer on ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Lime Team")
                        .color(NamedTextColor.GREEN))
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
            if(i % 2 == 0) {
                addToTeam(it, it.uniqueId, Teams.RED)
            } else {
                addToTeam(it, it.uniqueId, Teams.BLUE)
            }
            i++
        }
    }

    fun sendTeamMessage(component : Component, team : Teams) {
        for(player in getTeamPlayers(team)) {
            player.sendMessage(component)
        }
    }

    fun sendTeamFrozenMessage(component : Component, team1 : Teams, team2 : Teams) {
        for(player in getTeamPlayers(team1) + getTeamPlayers(team2) + getTeamPlayers(Teams.SPECTATOR)) {
            player.sendMessage(component)
        }
    }

    fun sendTeamThawedMessage(component : Component, thawed : Player, team : Teams) {
        for(player in getTeamPlayers(team) + getTeamPlayers(Teams.SPECTATOR)) {
            if(player != thawed) {
                player.sendMessage(component)
            } else {
                player.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] You were unfrozen!")))
            }
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

        yellowDisplayTeam.color(NamedTextColor.YELLOW)
        yellowDisplayTeam.prefix(Component.text("\uD007 ").color(NamedTextColor.WHITE))
        yellowDisplayTeam.suffix(Component.text("").color(NamedTextColor.WHITE))
        yellowDisplayTeam.displayName(Component.text("Yellow").color(NamedTextColor.YELLOW))
        yellowDisplayTeam.setAllowFriendlyFire(false)

        limeDisplayTeam.color(NamedTextColor.GREEN)
        limeDisplayTeam.prefix(Component.text("\uD008 ").color(NamedTextColor.WHITE))
        limeDisplayTeam.suffix(Component.text("").color(NamedTextColor.WHITE))
        limeDisplayTeam.displayName(Component.text("Lime").color(NamedTextColor.GREEN))
        limeDisplayTeam.setAllowFriendlyFire(false)

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

    fun hideDisplayTeamNames() {
        redDisplayTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS)
        yellowDisplayTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS)
        limeDisplayTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS)
        blueDisplayTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS)
    }

    fun destroyDisplayTeams() {
        redDisplayTeam.unregister()
        yellowDisplayTeam.unregister()
        limeDisplayTeam.unregister()
        blueDisplayTeam.unregister()
        adminDisplayTeam.unregister()
        spectatorDisplayTeam.unregister()
    }

    fun getPlayerTeam(uuid : UUID): Teams {
        return if(redTeam.contains(uuid)) {
            Teams.RED
        } else if(yellowTeam.contains(uuid)) {
            Teams.YELLOW
        } else if(limeTeam.contains(uuid)) {
            Teams.LIME
        } else if(blueTeam.contains(uuid)) {
            Teams.BLUE
        } else {
            Teams.SPECTATOR
        }
    }

    private fun getTeamPlayers(team : Teams) : List<Player> {
        val teamPlayers = ArrayList<Player>()
        when(team) {
            Teams.RED -> {
                for(redPlayer in redTeam) {
                    Bukkit.getPlayer(redPlayer)?.let { teamPlayers.add(it) }
                }
            }
            Teams.YELLOW -> {
                for(yellowPlayer in yellowTeam) {
                    Bukkit.getPlayer(yellowPlayer)?.let { teamPlayers.add(it) }
                }
            }
            Teams.LIME -> {
                for(limePlayer in limeTeam) {
                    Bukkit.getPlayer(limePlayer)?.let { teamPlayers.add(it) }
                }
            }
            Teams.BLUE -> {
                for(bluePlayer in blueTeam) {
                    Bukkit.getPlayer(bluePlayer)?.let { teamPlayers.add(it) }
                }
            }
            Teams.SPECTATOR -> {
                for(spectator in spectators) {
                    Bukkit.getPlayer(spectator)?.let { teamPlayers.add(it) }
                }
            }
        }
        return teamPlayers
    }


    fun getTeamColor(team : Teams) : Color {
        return when(team) {
            Teams.RED -> {
                Color.RED
            }
            Teams.BLUE -> {
                Color.BLUE
            }
            Teams.YELLOW -> {
                Color.YELLOW
            }
            Teams.LIME -> {
                Color.LIME
            }
            Teams.SPECTATOR -> {
                Color.GRAY
            }
        }
    }

    fun isInRedTeam(uuid : UUID): Boolean {
        return redTeam.contains(uuid)
    }

    fun isInYellowTeam(uuid : UUID): Boolean {
        return yellowTeam.contains(uuid)
    }

    fun isInLimeTeam(uuid : UUID): Boolean {
        return limeTeam.contains(uuid)
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

    fun getYellowTeam(): ArrayList<UUID> {
        return this.yellowTeam
    }

    fun getLimeTeam(): ArrayList<UUID> {
        return this.limeTeam
    }

    fun getBlueTeam(): ArrayList<UUID> {
        return this.blueTeam
    }

    fun getSpectators(): ArrayList<UUID> {
        return this.spectators
    }

}
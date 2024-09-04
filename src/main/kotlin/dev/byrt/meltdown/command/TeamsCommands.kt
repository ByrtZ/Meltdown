package dev.byrt.meltdown.command

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.state.*
import dev.byrt.meltdown.util.DevStatus

import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import org.incendo.cloud.annotations.*
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class TeamsCommands {
    @Command("teams set <player> <team>")
    @CommandDescription("Puts the specified player on the specified team.")
    @Permission("meltdown.jointeam")
    fun setTeam(css: CommandSourceStack, @Argument("player") player : Player, @Argument("team") team : Teams) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            if(Main.getGame().teamManager.getPlayerTeam(player.uniqueId) == team) {
                css.sender.sendMessage(Component.text("This player is already on ${team.toString().lowercase()} team.").color(NamedTextColor.RED))
            } else {
                Main.getGame().teamManager.addToTeam(player, player.uniqueId, team)
                Main.getGame().dev.parseDevMessage("Team Update: ${player.name} is now on ${team.toString().lowercase()} team.", DevStatus.INFO)
            }
        } else {
            css.sender.sendMessage(Component.text("Unable to modify teams in this state.", NamedTextColor.RED))
        }
    }

    @Command("teams shuffle")
    @CommandDescription("Automatically assigns everyone online to a team.")
    @Permission("meltdown.autoteam")
    fun autoTeam(css: CommandSourceStack, @Flag("ignoreAdmins") doesIgnoreAdmins: Boolean) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            if(!doesIgnoreAdmins) {
                Main.getGame().teamManager.shuffle(Main.getPlugin().server.onlinePlayers)
                Main.getGame().dev.parseDevMessage("All online players successfully split into teams by ${css.sender.name}.", DevStatus.INFO)
            } else {
                try {
                    val nonAdmins = mutableListOf<Player>()
                    for(player in Bukkit.getOnlinePlayers()) {
                        if(!player.isOp) {
                            nonAdmins.add(player)
                        }
                    }
                    if(nonAdmins.isEmpty()) {
                        css.sender.sendMessage(Component.text("Unable to shuffle teams due to no non-admin players online.").color(NamedTextColor.RED))
                    } else {
                        Main.getGame().teamManager.shuffle(nonAdmins)
                        Main.getGame().dev.parseDevMessage("All online non-admin players successfully split into teams by ${css.sender.name}.", DevStatus.INFO)
                    }
                } catch(e : Exception) {
                    css.sender.sendMessage(Component.text("Unable to shuffle teams as an error occurred.").color(NamedTextColor.RED))
                }
            }
        } else {
            css.sender.sendMessage(Component.text("Unable to modify teams in this state.", NamedTextColor.RED))
        }
    }

    @Command("teams list <option>")
    @CommandDescription("Allows the executing player to see the array of the specified team.")
    @Permission("meltdown.teamlist")
    fun teamList(css: CommandSourceStack, @Argument("option") option : TeamsListOptions) {
        when(option) {
            TeamsListOptions.RED -> {
                css.sender.sendMessage(Component.text("DISPLAYING RED TEAM UUIDS:").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                css.sender.sendMessage(Component.text("${Main.getGame().teamManager.getRedTeam()}"))
            }
            TeamsListOptions.YELLOW -> {
                css.sender.sendMessage(Component.text("DISPLAYING YELLOW TEAM UUIDS:").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true))
                css.sender.sendMessage(Component.text("${Main.getGame().teamManager.getYellowTeam()}"))
            }
            TeamsListOptions.LIME -> {
                css.sender.sendMessage(Component.text("DISPLAYING LIME TEAM UUIDS:").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true))
                css.sender.sendMessage(Component.text("${Main.getGame().teamManager.getLimeTeam()}"))
            }
            TeamsListOptions.BLUE -> {
                css.sender.sendMessage(Component.text("DISPLAYING BLUE TEAM UUIDS:").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, true))
                css.sender.sendMessage(Component.text("${Main.getGame().teamManager.getBlueTeam()}"))
            }
            TeamsListOptions.SPECTATOR -> {
                css.sender.sendMessage(Component.text("DISPLAYING SPECTATOR TEAM UUIDS:").decoration(TextDecoration.BOLD, true))
                css.sender.sendMessage(Component.text("${Main.getGame().teamManager.getSpectators()}"))
            }
            TeamsListOptions.ALL -> {
                css.sender.sendMessage(Component.text("\nDISPLAYING RED TEAM UUIDS:").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                css.sender.sendMessage(Component.text("${Main.getGame().teamManager.getRedTeam()}\n"))
                css.sender.sendMessage(Component.text("DISPLAYING YELLOW TEAM UUIDS:").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true))
                css.sender.sendMessage(Component.text("${Main.getGame().teamManager.getYellowTeam()}"))
                css.sender.sendMessage(Component.text("DISPLAYING LIME TEAM UUIDS:").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true))
                css.sender.sendMessage(Component.text("${Main.getGame().teamManager.getLimeTeam()}"))
                css.sender.sendMessage(Component.text("\nDISPLAYING BLUE TEAM UUIDS:").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, true))
                css.sender.sendMessage(Component.text("${Main.getGame().teamManager.getBlueTeam()}\n"))
                css.sender.sendMessage(Component.text("\nDISPLAYING SPECTATOR TEAM UUIDS:").decoration(TextDecoration.BOLD, true))
                css.sender.sendMessage(Component.text("${Main.getGame().teamManager.getSpectators()}\n"))
            }
        }
    }
}

enum class TeamsListOptions {
    SPECTATOR,
    RED,
    YELLOW,
    LIME,
    BLUE,
    ALL
}
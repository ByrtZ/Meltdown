package dev.byrt.meltdown.task

import dev.byrt.meltdown.data.HeaterBreakReason
import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Sounds
import dev.byrt.meltdown.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

import java.time.Duration

import kotlin.collections.ArrayList

class TeamEliminatedTask(private val game : Game) {
    private var eliminateTeamTasks = mutableMapOf<Teams, BukkitRunnable>()

    fun startEliminateTeamTask(teamPlayers : ArrayList<Player>, team : Teams) {
        val eliminateTeamRunnable = object : BukkitRunnable() {
            var eliminatedTaskTimer = 5
            override fun run() {
                if(eliminatedTaskTimer == 5) {
                    for(player in teamPlayers) {
                        player.showTitle(Title.title(
                            Component.text("TEAM FROZEN", NamedTextColor.RED, TextDecoration.BOLD),
                            Component.text(""),
                            Title.Times.times(
                                Duration.ofMillis(500),
                                Duration.ofSeconds(4),
                                Duration.ofMillis(250)
                                )
                            )
                        )
                    }
                }
                if(eliminatedTaskTimer <= 0) {
                    for(player in Bukkit.getOnlinePlayers()) {
                        player.playSound(player.location, Sounds.Score.TEAM_ELIMINATED, 1f, 1f)
                        player.sendMessage(
                            Component.text("[")
                                .append(Component.text("☠☠☠", NamedTextColor.RED))
                                .append(Component.text("] ${team.teamGlyph} ", NamedTextColor.WHITE))
                                .append(Component.text("${team.teamName} ", team.textColor))
                                .append(Component.text("have all been eliminated!", NamedTextColor.WHITE)))
                        if(teamPlayers.contains(player)) {
                            game.freezeTask.cancelFreezeLoop(player)
                            game.heaterManager.getPlayerHeater(player)?.let { game.heaterTask.stopHeaterLoop(it, HeaterBreakReason.DEATH) }
                            player.sendActionBar(Component.text("Team eliminated!", NamedTextColor.RED, TextDecoration.BOLD))
                        }
                    }
                    stopEliminateTeamTask(team)
                }
                eliminatedTaskTimer--
            }
        }
        eliminateTeamRunnable.runTaskTimer(game.plugin, 0L, 20L)
        eliminateTeamTasks[team] = eliminateTeamRunnable
    }

    fun stopEliminateTeamTask(team : Teams) {
        eliminateTeamTasks.remove(team)?.cancel()
    }

    fun clearEliminatedTeamTaskMap() {
        eliminateTeamTasks.clear()
    }

    fun getEliminatedTeamTaskMap() : Map<Teams, BukkitRunnable> {
        return eliminateTeamTasks
    }
}
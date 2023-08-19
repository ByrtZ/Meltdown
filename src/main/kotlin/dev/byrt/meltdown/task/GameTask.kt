package dev.byrt.meltdown.task

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.state.RoundState
import dev.byrt.meltdown.state.Sounds
import dev.byrt.meltdown.state.TimerState
import dev.byrt.meltdown.util.DevStatus

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

import java.time.Duration

class GameTask(private var game : Game) {
    private var gameRunnableList = mutableMapOf<Int, BukkitRunnable>()
    private var currentGameTaskId = 0
    private var timeLeft = 0
    private var previousTimeLeft = 0
    private var displayTime: String = "00:00"
    private var previousDisplayTime: String = "00:00"

    fun gameLoop() {
        val gameRunnable = object : BukkitRunnable() {
            override fun run() {
                // Formatting variables
                previousTimeLeft = timeLeft + 1
                displayTime = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60)
                previousDisplayTime = String.format("%02d:%02d", previousTimeLeft / 60, previousTimeLeft % 60)

                // Update scoreboard timer
                game.infoBoardManager.updateScoreboardTimer(displayTime, previousDisplayTime, game.gameManager.getGameState())

                // Game/round starting front end
                if(game.gameManager.getGameState() == GameState.STARTING && game.timerManager.getTimerState() == TimerState.ACTIVE) {
                    if(game.roundManager.getRoundState() == RoundState.ONE) { // Game tutorial
                        if(timeLeft == 75) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.showTitle(Title.title(
                                    Component.text("Meltdown").color(NamedTextColor.YELLOW),
                                    Component.text(""),
                                    Title.Times.times(
                                        Duration.ofSeconds(1),
                                        Duration.ofSeconds(4),
                                        Duration.ofSeconds(1)
                                        )
                                    )
                                )
                            }
                        }
                        if(timeLeft == 25) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.playSound(player.location, Sounds.Tutorial.TUTORIAL_POP, 1f, 1f)
                                player.sendMessage(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                                    .append(Component.text(" Starting soon:\n\n").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                                        .append(Component.text("      Standby for the game to begin...\n\n").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, false).decoration(TextDecoration.ITALIC, true)
                                            .append(Component.text("\n\n\n")
                                                .append(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true).decoration(TextDecoration.ITALIC, false)
                                                )
                                            )
                                        )
                                    )
                                )
                            }
                        }
                    }
                    if(timeLeft in 4..10) {
                        if(timeLeft == 10) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.playSound(player.location, Sounds.Round.ROUND_STARTING, 1f, 1f)
                            }
                        }
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.showTitle(Title.title(
                                Component.text("Starting in").color(NamedTextColor.AQUA),
                                Component.text("►$timeLeft◄").decoration(TextDecoration.BOLD, true),
                                Title.Times.times(
                                    Duration.ofSeconds(0),
                                    Duration.ofSeconds(5),
                                    Duration.ofSeconds(0)
                                    )
                                )
                            )
                            player.playSound(player.location, Sounds.Timer.CLOCK_TICK, 1f, 1f)
                        }
                    }
                    if(timeLeft == 3 || timeLeft == 2 || timeLeft == 1) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Timer.CLOCK_TICK, 1f, 1f)
                            player.playSound(player.location, Sounds.Timer.STARTING_123, 1f, 1f)
                            if(timeLeft == 3) {
                                player.showTitle(Title.title(
                                    Component.text("Starting in").color(NamedTextColor.AQUA),
                                    Component.text("►$timeLeft◄").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true),
                                    Title.Times.times(
                                        Duration.ofSeconds(0),
                                        Duration.ofSeconds(5),
                                        Duration.ofSeconds(0)
                                    )
                                )
                                )
                            }
                            if(timeLeft == 2) {
                                player.showTitle(Title.title(
                                    Component.text("Starting in").color(NamedTextColor.AQUA),
                                    Component.text("►$timeLeft◄").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true),
                                    Title.Times.times(
                                        Duration.ofSeconds(0),
                                        Duration.ofSeconds(5),
                                        Duration.ofSeconds(0)
                                    )
                                )
                                )
                            }
                            if(timeLeft == 1) {
                                player.showTitle(Title.title(
                                    Component.text("Starting in").color(NamedTextColor.AQUA),
                                    Component.text("►$timeLeft◄").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true),
                                    Title.Times.times(
                                        Duration.ofSeconds(0),
                                        Duration.ofSeconds(5),
                                        Duration.ofSeconds(0)
                                    )
                                )
                                )
                            }
                        }
                    }
                    if(timeLeft <= 0) {
                        game.gameManager.nextState()
                    }
                }

                // IN_GAME state
                if(game.gameManager.getGameState() == GameState.IN_GAME && game.timerManager.getTimerState() == TimerState.ACTIVE) {
                    if(timeLeft in 11..30 || timeLeft % 60 == 0) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Timer.CLOCK_TICK, 1f, 1f)
                        }
                    }
                    if(timeLeft in 0..10) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Timer.CLOCK_TICK_HIGH, 1f, 1f)
                        }
                    }
                }

                // Overtime
                if(timeLeft <= 0 && game.gameManager.getGameState() == GameState.IN_GAME && game.timerManager.getTimerState() == TimerState.ACTIVE && game.gameManager.isOvertimeActive()) {
                    game.gameManager.nextState()
                }

                if(game.gameManager.getGameState() == GameState.OVERTIME && game.timerManager.getTimerState() == TimerState.ACTIVE) {
                    if(timeLeft in 11..30 || timeLeft % 60 == 0) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Timer.CLOCK_TICK, 1f, 1f)
                        }
                    }
                    if(timeLeft in 0..10) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Timer.CLOCK_TICK_HIGH, 1f, 1f)
                        }
                    }
                }

                // Round ending front end
                if(timeLeft <= 0 && game.gameManager.getGameState() == GameState.IN_GAME && game.timerManager.getTimerState() == TimerState.ACTIVE && !game.gameManager.isOvertimeActive() || timeLeft <= 0 && game.gameManager.getGameState() == GameState.OVERTIME && game.timerManager.getTimerState() == TimerState.ACTIVE) {
                    game.gameManager.nextState()
                }

                // Next round
                if(timeLeft <= 0 && game.gameManager.getGameState() == GameState.ROUND_END && game.timerManager.getTimerState() == TimerState.ACTIVE) {
                    game.gameManager.nextState()
                }

                // Game end cycle
                if(game.gameManager.getGameState() == GameState.GAME_END && game.timerManager.getTimerState() == TimerState.ACTIVE) {
                    if(timeLeft <= 0) {
                        game.gameManager.nextState()
                        cancelGameTask()
                    }
                }
                // Decrement timer by 1 if timer is active
                if(game.timerManager.getTimerState() == TimerState.ACTIVE) {
                    timeLeft--
                }
            }
        }
        gameRunnable.runTaskTimer(game.plugin, 0L, 20L)
        currentGameTaskId = gameRunnable.taskId
        gameRunnableList[gameRunnable.taskId] = gameRunnable
    }

    fun cancelGameTask() {
        gameRunnableList.remove(currentGameTaskId)?.cancel()
    }

    fun setTimeLeft(setTimeLeft: Int, sender : Player?) {
        val setTimerTimeLeft = String.format("%02d:%02d", setTimeLeft / 60, setTimeLeft % 60)
        game.infoBoardManager.updateScoreboardTimer(setTimerTimeLeft, displayTime, game.gameManager.getGameState())
        timeLeft = setTimeLeft
        if(sender != null) {
            game.dev.parseDevMessage("Timer updated to $timeLeft seconds by ${sender.name}.", DevStatus.INFO)
        } else {
            game.dev.parseDevMessage("Timer updated to $timeLeft seconds.", DevStatus.INFO)
        }
    }

    fun getTimeLeft() : Int {
        return timeLeft
    }

    fun resetVars() {
        timeLeft = 0
        previousTimeLeft = 0
        displayTime = "00:00"
        previousDisplayTime = "00:00"
    }
}
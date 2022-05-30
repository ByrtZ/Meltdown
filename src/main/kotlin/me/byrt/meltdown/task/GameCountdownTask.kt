package me.byrt.meltdown.task

import me.byrt.meltdown.Main
import me.byrt.meltdown.manager.*

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.scheduler.BukkitRunnable

import java.time.Duration

class GameCountdownTask(private var game: Game) : BukkitRunnable() {
    private var timeLeft = 0
    private var previousTimeLeft = 0
    private var displayTime: String = "00:00"
    private var previousDisplayTime: String = "00:00"
    private var setTimerTimeLeft: String? = null

    override fun run() {
        // Formatting variables
        previousTimeLeft = timeLeft + 1
        displayTime = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60)
        previousDisplayTime = String.format("%02d:%02d", previousTimeLeft / 60, previousTimeLeft % 60)

        // Update scoreboard timer
        game.getInfoBoardManager().updateScoreboardTimer(displayTime, previousDisplayTime, game.getGameState())

        // Game/round starting front end //TODO: GAME TUTORIAL IN ROUND ONE STARTING PHASE
        if (game.getGameState() == GameState.STARTING && game.getTimerState() == TimerState.ACTIVE) {
            if (game.getRoundState() == RoundState.ROUND_ONE) { // Round one starting titles
                if (timeLeft in 4..10) {
                    for (player in Bukkit.getOnlinePlayers()) {
                        if(timeLeft == 10) {
                            player.playSound(player.location, Sound.MUSIC_DISC_BLOCKS, SoundCategory.VOICE, 0.5f, 1f)
                        }
                        player.playSound(player.location, "clockticknormal", 1f, 1f)
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
                    }
                }
                if (timeLeft == 3 || timeLeft == 2 || timeLeft == 1) {
                    for (player in Bukkit.getOnlinePlayers()) {
                        player.playSound(player.location, "123", 1f, 1f)
                        player.playSound(player.location, "clockticknormal", 1f, 1f)
                        if (timeLeft == 3) {
                            player.showTitle(Title.title(
                                    Component.text("Starting in").color(NamedTextColor.AQUA),
                                    Component.text("►$timeLeft◄").color(NamedTextColor.GREEN)
                                        .decoration(TextDecoration.BOLD, true),
                                    Title.Times.times(
                                        Duration.ofSeconds(0),
                                        Duration.ofSeconds(5),
                                        Duration.ofSeconds(0)
                                    )
                                )
                            )
                        }
                        if (timeLeft == 2) {
                            player.showTitle(Title.title(
                                    Component.text("Starting in").color(NamedTextColor.AQUA),
                                    Component.text("►$timeLeft◄").color(NamedTextColor.YELLOW)
                                        .decoration(TextDecoration.BOLD, true),
                                    Title.Times.times(
                                        Duration.ofSeconds(0),
                                        Duration.ofSeconds(5),
                                        Duration.ofSeconds(0)
                                    )
                                )
                            )
                        }
                        if (timeLeft == 1) {
                            player.showTitle(Title.title(
                                    Component.text("Starting in").color(NamedTextColor.AQUA),
                                    Component.text("►$timeLeft◄").color(NamedTextColor.RED)
                                        .decoration(TextDecoration.BOLD, true),
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
            }
        }

        if (timeLeft <= 0) {
            for (player in Bukkit.getOnlinePlayers()) {
                player.playSound(player.location, "go", 1f, 1f)
                player.playSound(player.location, "clocktickhigh", 1f, 1f)
                player.playSound(player.location, "music.meltdown", SoundCategory.VOICE, 1f, 1f)
                player.playSound(player.location, "meltdown_entrance", 1f, 1f)
                player.resetTitle()
            }
            game.setGameState(GameState.IN_GAME)
        }

        // <=30s remaining
        if (game.getGameState() == GameState.IN_GAME && game.getTimerState() == TimerState.ACTIVE) {
            if (timeLeft in 11..30 || timeLeft % 60 == 0) {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.playSound(player.location, "clockticknormal", 1f, 1f)
                }
            }
            if (timeLeft in 0..10) {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.playSound(player.location, "clocktickhigh", 1f, 2f)
                }
            }
        }

        // Round ending front end
        if (timeLeft <= 0 && game.getGameState() == GameState.IN_GAME && game.getTimerState() == TimerState.ACTIVE) {
            if (game.getRoundState() == RoundState.ROUND_THREE) {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.stopSound("music.meltdown", SoundCategory.VOICE)
                    player.playSound(player.location, "roundend", 1f, 1f)
                    player.playSound(player.location, Sound.MUSIC_DISC_MALL, SoundCategory.VOICE, 0.5f, 1f)
                    player.showTitle(Title.title(
                        Component.text("Game Over!").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true),
                        Component.text(""),
                        Title.Times.times(
                            Duration.ofSeconds(0),
                            Duration.ofSeconds(4),
                            Duration.ofSeconds(1)
                            )
                        )
                    )
                    player.gameMode = GameMode.ADVENTURE
                    player.allowFlight = true
                    player.isFlying = true
                }
                game.setGameState(GameState.GAME_END)
            } else {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.stopSound("music.meltdown", SoundCategory.VOICE)
                    player.playSound(player.location, "roundend", 1f, 1f)
                    player.playSound(player.location, Sound.MUSIC_DISC_FAR, SoundCategory.VOICE, 0.5f, 1f)
                    player.showTitle(Title.title(
                        Component.text("Round Over!").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true),
                        Component.text(""),
                        Title.Times.times(
                            Duration.ofSeconds(0),
                            Duration.ofSeconds(4),
                            Duration.ofSeconds(1)
                        )
                    )
                    )
                    player.gameMode = GameMode.ADVENTURE
                    player.allowFlight = true
                    player.isFlying = true
                }
                game.setGameState(GameState.ROUND_END)
            }
        }

        // Increment round if round one ended
        if (game.getGameState() == GameState.ROUND_END && game.getTimerState() == TimerState.ACTIVE) {
            if (timeLeft <= 0) {
                if (game.getRoundState() != RoundState.ROUND_THREE) {
                    when (game.getRoundState()) {
                        RoundState.ROUND_ONE -> {
                            game.setRoundState(RoundState.ROUND_TWO)
                            game.setGameState(GameState.STARTING)
                        }
                        RoundState.ROUND_TWO -> {
                            game.setRoundState(RoundState.ROUND_THREE)
                            game.setGameState(GameState.STARTING)
                        }
                        else -> {
                            Main.getPlugin().logger.info("[ERROR] Something weird happened when incrementing round state.")
                        }
                    }
                }
            }
        }

        // Game end cycle
        if (game.getGameState() == GameState.GAME_END && game.getTimerState() == TimerState.ACTIVE) {
            if (timeLeft <= 0) {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("Thank you for playing! The server will be restarting shortly.").color(NamedTextColor.YELLOW))
                }
                cancel()
            }
        }

        // Decrement timer by 1 if timer is active
        if (game.getTimerState() == TimerState.ACTIVE) {
            timeLeft--
        }
    }

    fun pauseCountdownTask() {
        game.setTimerState(TimerState.PAUSED)
    }

    fun resumeCountdownTask() {
        game.setTimerState(TimerState.ACTIVE)
    }

    fun setTimeLeft(setTimeLeft: Int) {
        setTimerTimeLeft = String.format("%02d:%02d", setTimeLeft / 60, setTimeLeft % 60)
        game.getInfoBoardManager().updateScoreboardTimer(setTimerTimeLeft!!, displayTime, game.getGameState())
        timeLeft = setTimeLeft
    }
}
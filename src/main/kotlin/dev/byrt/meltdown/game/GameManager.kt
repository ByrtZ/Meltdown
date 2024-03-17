package dev.byrt.meltdown.game

import dev.byrt.meltdown.manager.PlayerLifeState
import dev.byrt.meltdown.manager.TeamLifeState
import dev.byrt.meltdown.state.RoundState
import dev.byrt.meltdown.state.Sounds
import dev.byrt.meltdown.state.Teams
import dev.byrt.meltdown.state.TimerState
import dev.byrt.meltdown.task.Music
import dev.byrt.meltdown.util.DevStatus

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.SoundCategory

import java.time.Duration

class GameManager(private val game : Game) {
    private var gameState = GameState.IDLE
    private var overtimeActive = false

    fun nextState() {
        when(this.gameState) {
            GameState.IDLE -> { setGameState(GameState.STARTING) }
            GameState.STARTING -> { setGameState(GameState.IN_GAME) }
            GameState.IN_GAME -> {
                if(overtimeActive) {
                    setGameState(GameState.OVERTIME)
                } else if(game.roundManager.getRoundState().ordinal + 1 >= game.roundManager.getTotalRounds()) {
                    setGameState(GameState.GAME_END)
                } else {
                    setGameState(GameState.ROUND_END)
                }
            }
            GameState.ROUND_END -> { setGameState(GameState.STARTING) }
            GameState.GAME_END -> { setGameState(GameState.IDLE) }
            GameState.OVERTIME -> {
                if(game.roundManager.getRoundState().ordinal + 1 >= game.roundManager.getTotalRounds()) {
                    setGameState(GameState.GAME_END)
                } else {
                    setGameState(GameState.ROUND_END)
                }
            }
        }
    }

    fun setGameState(newState : GameState) {
        if(newState == gameState) return
        game.dev.parseDevMessage("Game state updated from $gameState to $newState.", DevStatus.INFO)
        this.gameState = newState
        game.infoBoardManager.updateStatus()
        when(this.gameState) {
            GameState.IDLE -> {
                game.reload()
                game.gameTask.cancelGameTask()
            }
            GameState.STARTING -> {
                if(game.roundManager.getRoundState() == RoundState.ONE) {
                    game.setBuildMode(false)
                    game.timerManager.setTimerState(TimerState.ACTIVE)
                    game.gameTask.setTimeLeft(GAME_STARTING_TIME, null)
                    game.gameTask.gameLoop()
                    starting()
                } else {
                    game.timerManager.setTimerState(TimerState.ACTIVE)
                    game.gameTask.setTimeLeft(ROUND_STARTING_TIME, null)
                    starting()
                }
            }
            GameState.IN_GAME -> {
                game.timerManager.setTimerState(TimerState.ACTIVE)
                game.gameTask.setTimeLeft(IN_GAME_TIME, null)
                startRound()
            }
            GameState.ROUND_END -> {
                game.timerManager.setTimerState(TimerState.ACTIVE)
                game.gameTask.setTimeLeft(ROUND_END_TIME, null)
                roundEnd()
            }
            GameState.GAME_END -> {
                game.timerManager.setTimerState(TimerState.ACTIVE)
                game.gameTask.setTimeLeft(GAME_END_TIME, null)
                gameEnd()
            }
            GameState.OVERTIME -> {
                game.timerManager.setTimerState(TimerState.ACTIVE)
                game.gameTask.setTimeLeft(OVERTIME_TIME, null)
                startOvertime()
            }
        }
    }

    private fun startRound() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sounds.Round.ROUND_START_PLING, 1f, 1f)
            player.playSound(player.location, Sounds.Round.ROUND_START_PLING, 1f, 2f)
            player.playSound(player.location, Sounds.Timer.CLOCK_TICK_HIGH, 1f, 1f)
            player.playSound(player.location, Sounds.Round.ENTRANCE, 1f, 1.25f)
            game.musicTask.startMusicLoop(player, game.plugin, Music.MAIN)
            player.resetTitle()
        }
        game.playerManager.setPlayersSurvival()
        game.entranceManager.openAllEntrances()
        game.scoreManager.calculatePlacements()
    }

    private fun starting() {
        game.playerManager.setSpectatorsGameMode()
        game.playerManager.setPlayersNotFlying()
        game.playerManager.teleportPlayersToGame()
        game.playerManager.teleportSpectatorsToArena()
        game.playerManager.setPlayersAdventure()
        game.playerManager.clearNonBootsItems()
        game.teamManager.hideDisplayTeamNames()
        game.entranceManager.resetEntrances()
        game.blockManager.setCoinCrates(Material.RAW_GOLD_BLOCK)
        game.blockManager.setCoinCratesBarriers(Material.BLUE_STAINED_GLASS)
        game.blockManager.setCentreCoinCrateBarriers(Material.BLUE_STAINED_GLASS)
        game.playerManager.giveItemsToPlayers()
        game.scoreManager.calculatePlacements()
        game.infoBoardManager.updateRound()
        game.eliminationManager.reset()
        for(player in Bukkit.getOnlinePlayers()) {
            if(game.teamManager.getPlayerTeam(player.uniqueId) != Teams.SPECTATOR) {
                game.eliminationManager.changePlayerLifeState(player, PlayerLifeState.ALIVE)
            }
        }
        for(team in game.teamManager.getActiveTeams()) {
            game.eliminationManager.changeTeamLifeState(team, TeamLifeState.ALIVE)
        }
        if(game.roundManager.getRoundState() == RoundState.ONE) {
            for(player in Bukkit.getOnlinePlayers()) {
                player.showTitle(Title.title(Component.text("\uD000"), Component.text(""), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(2), Duration.ofSeconds(1))))
                player.playSound(player.location, Sounds.Start.START_GAME_SUCCESS, 1f, 1f)
                if(game.teamManager.getPlayerTeam(player.uniqueId) != Teams.SPECTATOR) {
                    game.teamManager.enableTeamGlowing(player, game.teamManager.getPlayerTeam(player.uniqueId))
                }
            }
        }
    }

    private fun startOvertime() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sounds.Alert.OVERTIME_ALERT, 0.35f, 1.25f)
            player.showTitle(Title.title(
                Component.text("OVERTIME!", NamedTextColor.RED, TextDecoration.BOLD),
                Component.text("Uh oh... This shouldn't happen!"),
                Title.Times.times(
                    Duration.ofSeconds(0),
                    Duration.ofSeconds(3),
                    Duration.ofSeconds(1)
                    )
                )
            )
            player.sendMessage(
                Component.text("\n[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("OVERTIME: ", NamedTextColor.RED, TextDecoration.BOLD))
                    .append(Component.text("Well this is an unfortunate series of events... ", NamedTextColor.WHITE)
                    )
                )
        }
    }

    private fun gameEnd() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sounds.GameOver.GAME_OVER_PLING, 1f, 1f)
            player.playSound(player.location, Sounds.GameOver.GAME_OVER_PLING, 1f, 2f)
            player.playSound(player.location, Sounds.GameOver.GAME_OVER_JINGLE, 1f, 1f)
            player.playSound(player.location, Sounds.GameOver.GAME_OVER_COMPLETE, 1f, 1f)
            game.musicTask.stopMusicLoop(player, Music.MAIN)
            game.musicTask.stopMusicLoop(player, Music.OVERTIME)
            player.playSound(player.location, Sounds.Music.GAME_OVER_MUSIC, SoundCategory.VOICE, 0.85f, 1f)
            player.showTitle(Title.title(
                Component.text("Game Over!", NamedTextColor.RED, TextDecoration.BOLD),
                Component.text(""),
                Title.Times.times(
                    Duration.ofSeconds(0),
                    Duration.ofSeconds(4),
                    Duration.ofSeconds(1)
                    )
                )
            )
            if(game.eliminationManager.getFrozenPlayers().contains(player.uniqueId)) {
                game.freezeManager.unfreezePlayer(player)
            }
            //TODO: INSTA-ELIMINATE FROZEN PLAYERS?
        }
        game.playerManager.setAlivePlayersAdventure()
        game.playerManager.setPlayersFlying()
        game.heaterManager.stopAllHeaters()
        game.scoreManager.calculatePlacements()
    }

    private fun roundEnd() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sounds.Round.ROUND_END_PLING, 1f, 1f)
            player.playSound(player.location, Sounds.Round.ROUND_END_PLING, 1f, 2f)
            player.playSound(player.location, Sounds.Round.ROUND_END_JINGLE, 1f, 1f)
            game.musicTask.stopMusicLoop(player, Music.MAIN)
            game.musicTask.stopMusicLoop(player, Music.OVERTIME)
            player.playSound(player.location, Sounds.Music.ROUND_OVER_MUSIC, SoundCategory.VOICE, 0.85f, 1f)
            player.showTitle(
                Title.title(
                    Component.text("Round Over!", NamedTextColor.RED, TextDecoration.BOLD),
                    Component.text(""),
                    Title.Times.times(
                        Duration.ofSeconds(0),
                        Duration.ofSeconds(4),
                        Duration.ofSeconds(1)
                    )
                )
            )
            if(game.eliminationManager.getFrozenPlayers().contains(player.uniqueId)) {
                game.freezeManager.unfreezePlayer(player)
            }
            //TODO: INSTA-ELIMINATE FROZEN PLAYERS?
        }
        game.playerManager.setAlivePlayersAdventure()
        game.playerManager.setPlayersFlying()
        game.heaterManager.stopAllHeaters()
        game.scoreManager.calculatePlacements()
        game.roundManager.nextRound()
    }

    fun getGameState() : GameState {
        return gameState
    }

    fun setOvertimeState(isActive : Boolean) {
        overtimeActive = isActive
    }

    fun isOvertimeActive() : Boolean {
        return overtimeActive
    }

    fun forceState(forcedState : GameState) {
        setGameState(forcedState)
    }

    companion object {
        const val GAME_STARTING_TIME = 80
        const val ROUND_STARTING_TIME = 20
        const val IN_GAME_TIME = 300
        const val ROUND_END_TIME = 15
        const val GAME_END_TIME = 30
        const val OVERTIME_TIME = 30
    }
}
enum class GameState {
    IDLE,
    STARTING,
    IN_GAME,
    ROUND_END,
    GAME_END,
    OVERTIME
}
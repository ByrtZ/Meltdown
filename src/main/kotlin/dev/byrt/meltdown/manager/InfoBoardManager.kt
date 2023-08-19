package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.state.RoundState

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.*

@Suppress("DEPRECATION")
class InfoBoardManager(private val game : Game) {
    private var scoreboardManager: ScoreboardManager = Bukkit.getScoreboardManager()
    private var scoreboard: Scoreboard = scoreboardManager.mainScoreboard
    private lateinit var meltdownBoard: Objective
    private lateinit var currentGameText: Score
    private lateinit var currentMapText: Score
    private lateinit var currentRoundText: Score
    private lateinit var gameStatusText: Score
    private lateinit var gameScoreText: Score
    private lateinit var redScore: Score
    private lateinit var blueScore: Score
    private lateinit var blankSpaceOne: Score
    private lateinit var blankSpaceTwo: Score

    fun buildScoreboard() {
        constructScoreboardInfo()
        meltdownBoard.displaySlot = DisplaySlot.SIDEBAR
        currentGameText.score = 8
        currentMapText.score = 7
        currentRoundText.score = 6
        gameStatusText.score = 5
        blankSpaceOne.score = 4
        gameScoreText.score = 3
        redScore.score = 2
        blueScore.score = 1
        blankSpaceTwo.score = 0
    }

    fun destroyScoreboard() {
        meltdownBoard.displaySlot = null
        meltdownBoard.unregister()
    }

    private fun constructScoreboardInfo() {
        meltdownBoard = scoreboard.registerNewObjective(
            "meltdown_board",
            Criteria.DUMMY,
            Component.text("Byrt's Server").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)
        )

        currentGameText = meltdownBoard.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Game: " + ChatColor.RESET + "Meltdown")
        currentMapText = meltdownBoard.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Map: " + ChatColor.WHITE + "Unconfigured")
        currentRoundText = meltdownBoard.getScore(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
        gameStatusText = meltdownBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")
        gameScoreText = meltdownBoard.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Game Coins: " + ChatColor.RESET + "(" + ChatColor.YELLOW + "x1.0" + ChatColor.RESET + ")")
        redScore = meltdownBoard.getScore(" 1. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                 0c")
        blueScore = meltdownBoard.getScore(" 2. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                0c")
        blankSpaceOne = meltdownBoard.getScore("§")
        blankSpaceTwo = meltdownBoard.getScore("§§")
    }

    fun showScoreboard() {
        meltdownBoard.displaySlot = DisplaySlot.SIDEBAR
    }

    fun updateScoreboardTimer(displayTime : String, previousDisplayTime : String, gameState : GameState) {
        if(gameState == GameState.STARTING) {
            meltdownBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")
            meltdownBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game over!")
            meltdownBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + previousDisplayTime)
            meltdownBoard.scoreboard!!.resetScores(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")

            if(game.roundManager.getRoundState() == RoundState.ONE) {
                meltdownBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + "" + previousDisplayTime)
                gameStatusText = meltdownBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + "" + displayTime)
                gameStatusText.score = 5
            } else {
                meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + previousDisplayTime)
                gameStatusText = meltdownBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + "" + displayTime)
                gameStatusText.score = 5
            }

            meltdownBoard.scoreboard?.resetScores(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
            meltdownBoard.scoreboard?.resetScores(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "${game.roundManager.getRoundState().ordinal}/${game.roundManager.getTotalRounds()}")
            currentRoundText = meltdownBoard.getScore(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "${game.roundManager.getRoundState().ordinal + 1}/${game.roundManager.getTotalRounds()}")
            currentRoundText.score = 6
        }
        if(gameState == GameState.IN_GAME) {
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + previousDisplayTime)
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + previousDisplayTime)
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + previousDisplayTime)
            gameStatusText = meltdownBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + displayTime)
            gameStatusText.score = 5
        }
        if(gameState == GameState.OVERTIME) {
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + previousDisplayTime)
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "OVERTIME: " + ChatColor.RESET + "" + previousDisplayTime)
            gameStatusText = meltdownBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "OVERTIME: " + ChatColor.RESET + "" + displayTime)
            gameStatusText.score = 5
        }
        if(gameState == GameState.ROUND_END) {
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + previousDisplayTime)
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + previousDisplayTime)
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "OVERTIME: " + ChatColor.RESET + "" + previousDisplayTime)
            gameStatusText = meltdownBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + "" + displayTime)
            gameStatusText.score = 5
        }
        if(gameState == GameState.GAME_END) {
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + previousDisplayTime)
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "OVERTIME: " + ChatColor.RESET + "" + previousDisplayTime)
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game ending: " + ChatColor.RESET + previousDisplayTime)

            gameStatusText = if (displayTime == "00:00") {
                meltdownBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game over!")
            } else {
                meltdownBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game ending: " + ChatColor.RESET + "" + displayTime)
            }
            gameStatusText.score = 5
        }
    }
}
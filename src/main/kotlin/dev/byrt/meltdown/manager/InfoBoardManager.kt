package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.state.RoundState
import dev.byrt.meltdown.state.Teams

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
    private lateinit var yellowScore: Score
    private lateinit var limeScore: Score
    private lateinit var blueScore: Score
    private lateinit var blankSpaceOne: Score
    private lateinit var blankSpaceTwo: Score

    fun buildScoreboard() {
        constructScoreboardInfo()
        meltdownBoard.displaySlot = DisplaySlot.SIDEBAR
        currentGameText.score = 9
        currentMapText.score = 9
        currentRoundText.score = 8
        gameStatusText.score = 7
        blankSpaceOne.score = 6
        gameScoreText.score = 5
        redScore.score = 4
        yellowScore.score = 3
        limeScore.score = 2
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
        currentMapText = meltdownBoard.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Map: " + ChatColor.WHITE + "Laboratory")
        currentRoundText = meltdownBoard.getScore(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
        gameStatusText = meltdownBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")
        gameScoreText = meltdownBoard.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Game Coins: " + ChatColor.RESET + "(" + ChatColor.YELLOW + "x1.0" + ChatColor.RESET + ")")
        redScore = meltdownBoard.getScore(" 1. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                0c")
        yellowScore = meltdownBoard.getScore(" 2. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                0c")
        limeScore = meltdownBoard.getScore(" 3. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                0c")
        blueScore = meltdownBoard.getScore(" 4. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                0c")
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
                gameStatusText.score = 7
            } else {
                meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + previousDisplayTime)
                gameStatusText = meltdownBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + "" + displayTime)
                gameStatusText.score = 7
            }

            meltdownBoard.scoreboard?.resetScores(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
            meltdownBoard.scoreboard?.resetScores(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "${game.roundManager.getRoundState().ordinal}/${game.roundManager.getTotalRounds()}")
            currentRoundText = meltdownBoard.getScore(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "${game.roundManager.getRoundState().ordinal + 1}/${game.roundManager.getTotalRounds()}")
            currentRoundText.score = 8
        }
        if(gameState == GameState.IN_GAME) {
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + previousDisplayTime)
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + previousDisplayTime)
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + previousDisplayTime)
            gameStatusText = meltdownBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + displayTime)
            gameStatusText.score = 7
        }
        if(gameState == GameState.OVERTIME) {
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + previousDisplayTime)
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "OVERTIME: " + ChatColor.RESET + "" + previousDisplayTime)
            gameStatusText = meltdownBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "OVERTIME: " + ChatColor.RESET + "" + displayTime)
            gameStatusText.score = 7
        }
        if(gameState == GameState.ROUND_END) {
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + previousDisplayTime)
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + previousDisplayTime)
            meltdownBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "OVERTIME: " + ChatColor.RESET + "" + previousDisplayTime)
            gameStatusText = meltdownBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + "" + displayTime)
            gameStatusText.score = 7
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
            gameStatusText.score = 7
        }
    }

    //TODO: BAND-AID FIX, NEEDS SEVERE REWRITE
    fun updateScoreboardScores() {
        val redPlacement = game.scoreManager.getPlacements().keys.indexOf(Teams.RED) + 1
        val yellowPlacement = game.scoreManager.getPlacements().keys.indexOf(Teams.YELLOW) + 1
        val limePlacement = game.scoreManager.getPlacements().keys.indexOf(Teams.LIME) + 1
        val bluePlacement = game.scoreManager.getPlacements().keys.indexOf(Teams.BLUE) + 1

        meltdownBoard.scoreboard?.resetScores(" 1. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 1. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 1. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 1. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 2. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 2. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 2. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 2. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 3. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 3. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 3. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 3. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 4. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 4. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 4. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                0c")
        meltdownBoard.scoreboard?.resetScores(" 4. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                0c")

        meltdownBoard.scoreboard?.resetScores(" 1. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                ${game.scoreManager.getLastRedScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 1. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                ${game.scoreManager.getLastYellowScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 1. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                ${game.scoreManager.getLastLimeScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 1. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.scoreManager.getLastBlueScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 2. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                ${game.scoreManager.getLastRedScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 2. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                ${game.scoreManager.getLastYellowScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 2. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                ${game.scoreManager.getLastLimeScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 2. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.scoreManager.getLastBlueScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 3. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                ${game.scoreManager.getLastRedScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 3. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                ${game.scoreManager.getLastYellowScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 3. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                ${game.scoreManager.getLastLimeScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 3. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.scoreManager.getLastBlueScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 4. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                ${game.scoreManager.getLastRedScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 4. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                ${game.scoreManager.getLastYellowScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 4. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                ${game.scoreManager.getLastLimeScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 4. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.scoreManager.getLastBlueScore()}c")

        meltdownBoard.scoreboard?.resetScores(" 1. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                ${game.scoreManager.getRedScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 1. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                ${game.scoreManager.getYellowScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 1. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                ${game.scoreManager.getLimeScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 1. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.scoreManager.getBlueScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 2. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                ${game.scoreManager.getRedScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 2. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                ${game.scoreManager.getYellowScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 2. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                ${game.scoreManager.getLimeScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 2. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.scoreManager.getBlueScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 3. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                ${game.scoreManager.getRedScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 3. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                ${game.scoreManager.getYellowScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 3. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                ${game.scoreManager.getLimeScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 3. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.scoreManager.getBlueScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 4. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                ${game.scoreManager.getRedScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 4. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                ${game.scoreManager.getYellowScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 4. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                ${game.scoreManager.getLimeScore()}c")
        meltdownBoard.scoreboard?.resetScores(" 4. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.scoreManager.getBlueScore()}c")

        //FIRST PLACE
        if(redPlacement == 1) {
            redScore = meltdownBoard.getScore(" 1. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                ${game.scoreManager.getRedScore()}c")
            redScore.score = 4
        }
        if(yellowPlacement == 1) {
            yellowScore = meltdownBoard.getScore(" 1. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                ${game.scoreManager.getYellowScore()}c")
            yellowScore.score = 4
        }
        if(limePlacement == 1) {
            limeScore = meltdownBoard.getScore(" 1. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                ${game.scoreManager.getLimeScore()}c")
            limeScore.score = 4
        }
        if(bluePlacement == 1) {
            blueScore = meltdownBoard.getScore(" 1. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.scoreManager.getBlueScore()}c")
            blueScore.score = 4
        }
        //SECOND PLACE
        if(redPlacement == 2) {
            redScore = meltdownBoard.getScore(" 2. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                ${game.scoreManager.getRedScore()}c")
            redScore.score = 3
        }
        if(yellowPlacement == 2) {
            yellowScore = meltdownBoard.getScore(" 2. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                ${game.scoreManager.getYellowScore()}c")
            yellowScore.score = 3
        }
        if(limePlacement == 2) {
            limeScore = meltdownBoard.getScore(" 2. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                ${game.scoreManager.getLimeScore()}c")
            limeScore.score = 3
        }
        if(bluePlacement == 2) {
            blueScore = meltdownBoard.getScore(" 2. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.scoreManager.getBlueScore()}c")
            blueScore.score = 3
        }
        //THIRD PLACE
        if(redPlacement == 3) {
            redScore = meltdownBoard.getScore(" 3. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                ${game.scoreManager.getRedScore()}c")
            redScore.score = 2
        }
        if(yellowPlacement == 3) {
            yellowScore = meltdownBoard.getScore(" 3. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                ${game.scoreManager.getYellowScore()}c")
            yellowScore.score = 2
        }
        if(limePlacement == 3) {
            limeScore = meltdownBoard.getScore(" 3. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                ${game.scoreManager.getLimeScore()}c")
            limeScore.score = 2
        }
        if(bluePlacement == 3) {
            blueScore = meltdownBoard.getScore(" 3. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.scoreManager.getBlueScore()}c")
            blueScore.score = 2
        }
        //FOURTH PLACE
        if(redPlacement == 4) {
            redScore = meltdownBoard.getScore(" 4. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                ${game.scoreManager.getRedScore()}c")
            redScore.score = 1
        }
        if(yellowPlacement == 4) {
            yellowScore = meltdownBoard.getScore(" 4. " + ChatColor.YELLOW.toString() + "Yellow Team " + ChatColor.RESET + "                ${game.scoreManager.getYellowScore()}c")
            yellowScore.score = 1
        }
        if(limePlacement == 4) {
            limeScore = meltdownBoard.getScore(" 4. " + ChatColor.GREEN.toString() + "Lime Team " + ChatColor.RESET + "                ${game.scoreManager.getLimeScore()}c")
            limeScore.score = 1
        }
        if(bluePlacement == 4) {
            blueScore = meltdownBoard.getScore(" 4. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.scoreManager.getBlueScore()}c")
            blueScore.score = 1
        }
    }
}
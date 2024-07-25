package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.state.RoundState
import io.papermc.paper.scoreboard.numbers.NumberFormat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.*

import java.util.*

@Suppress("DEPRECATION")
class InfoBoardManager(private val game : Game) {
    private var scoreboard = Bukkit.getScoreboardManager().mainScoreboard
    private var objective = scoreboard.registerNewObjective("${game.plugin.name.lowercase()}-${UUID.randomUUID()}", Criteria.DUMMY, Component.text("Byrt's Server", NamedTextColor.YELLOW, TextDecoration.BOLD))
    // Game text, map text, score multiplier and blank spaces stay static
    private var currentRoundLine = scoreboard.registerNewTeam("currentRoundLine")
    private val currentRoundLineKey = ChatColor.ITALIC.toString()

    private var gameStatusLine = scoreboard.registerNewTeam("gameStatusLine")
    private val gameStatusLineKey = ChatColor.MAGIC.toString()

    private var firstPlaceLine = scoreboard.registerNewTeam("firstPlaceLine")
    private val firstPlaceLineKey = ChatColor.UNDERLINE.toString()

    private var secondPlaceLine = scoreboard.registerNewTeam("secondPlaceLine")
    private val secondPlaceLineKey = ChatColor.LIGHT_PURPLE.toString()

    private var thirdPlaceLine = scoreboard.registerNewTeam("thirdPlaceLine")
    private val thirdPlaceLineKey = ChatColor.DARK_GREEN.toString()

    private var fourthPlaceLine = scoreboard.registerNewTeam("fourthPlaceLine")
    private val fourthPlaceLineKey = ChatColor.DARK_GRAY.toString()

    fun buildScoreboard() {
        game.plugin.logger.info("Building scoreboard...")
        objective.displaySlot = DisplaySlot.SIDEBAR
        objective.numberFormat(NumberFormat.blank())

        // Static game text
        objective.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Game: " + ChatColor.RESET + "Meltdown").score = 10

        // Static map text
        objective.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Map: " + ChatColor.RESET + "Laboratory").score = 9

        // Modifiable round information
        currentRoundLine.addEntry(currentRoundLineKey)
        currentRoundLine.prefix(Component.text("Round: ", NamedTextColor.GREEN, TextDecoration.BOLD))
        currentRoundLine.suffix(Component.text("None"))
        objective.getScore(currentRoundLineKey).score = 8

        // Modifiable game status information
        gameStatusLine.addEntry(gameStatusLineKey)
        gameStatusLine.prefix(Component.text("Game status: ", NamedTextColor.RED, TextDecoration.BOLD))
        gameStatusLine.suffix(Component.text("Awaiting players...", NamedTextColor.GRAY))
        objective.getScore(gameStatusLineKey).score = 7

        // Static blank space
        objective.getScore("§").score = 6

        // Static score multiplier
        objective.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Game Coins: " + ChatColor.RESET + "(" + ChatColor.YELLOW + "x1.0" + ChatColor.RESET + ")").score = 5

        // Modifiable first placement score
        firstPlaceLine.addEntry(firstPlaceLineKey)
        firstPlaceLine.prefix(Component.text(" 1. "))
        firstPlaceLine.suffix(Component.text("Red Team ", NamedTextColor.RED).append(Component.text("                0c", NamedTextColor.WHITE)))
        objective.getScore(firstPlaceLineKey).score = 4

        // Modifiable second placement score
        secondPlaceLine.addEntry(secondPlaceLineKey)
        secondPlaceLine.prefix(Component.text(" 2. "))
        secondPlaceLine.suffix(Component.text("Yellow Team ", NamedTextColor.YELLOW).append(Component.text("                0c", NamedTextColor.WHITE)))
        objective.getScore(secondPlaceLineKey).score = 3

        // Modifiable third placement score
        thirdPlaceLine.addEntry(thirdPlaceLineKey)
        thirdPlaceLine.prefix(Component.text(" 3. "))
        thirdPlaceLine.suffix(Component.text("Lime Team ", NamedTextColor.GREEN).append(Component.text("                0c", NamedTextColor.WHITE)))
        objective.getScore(thirdPlaceLineKey).score = 2

        // Modifiable fourth placement score
        fourthPlaceLine.addEntry(fourthPlaceLineKey)
        fourthPlaceLine.prefix(Component.text(" 4. "))
        fourthPlaceLine.suffix(Component.text("Blue Team ", NamedTextColor.BLUE).append(Component.text("                0c", NamedTextColor.WHITE)))
        objective.getScore(fourthPlaceLineKey).score = 1

        // Static blank space
        objective.getScore("§§").score = 0
        game.plugin.logger.info("Scoreboard constructed with ID ${objective.name}...")
    }

    fun updateRound() {
        if(game.gameManager.getGameState() == GameState.IDLE) {
            currentRoundLine.suffix(Component.text("None"))
        } else {
            currentRoundLine.suffix(Component.text("${game.roundManager.getRoundState().ordinal + 1}/${game.roundManager.getTotalRounds()}", NamedTextColor.WHITE))
        }
    }

    fun updateStatus() {
        when(game.gameManager.getGameState()) {
            GameState.IDLE -> {
                gameStatusLine.prefix(Component.text("Game status: ", NamedTextColor.RED, TextDecoration.BOLD))
                gameStatusLine.suffix(Component.text("Awaiting players...", NamedTextColor.GRAY))
            }
            GameState.STARTING -> {
                if(game.roundManager.getRoundState() == RoundState.ONE) {
                    gameStatusLine.prefix(Component.text("Game begins: ", NamedTextColor.RED, TextDecoration.BOLD))
                } else {
                    gameStatusLine.prefix(Component.text("Round begins: ", NamedTextColor.RED, TextDecoration.BOLD))
                }
            }
            GameState.IN_GAME -> {
                gameStatusLine.prefix(Component.text("Time left: ", NamedTextColor.RED, TextDecoration.BOLD))
            }
            GameState.ROUND_END -> {
                gameStatusLine.prefix(Component.text("Next round: ", NamedTextColor.RED, TextDecoration.BOLD))
            }
            GameState.GAME_END -> {
                gameStatusLine.prefix(Component.text("Game ending: ", NamedTextColor.RED, TextDecoration.BOLD))
            }
            GameState.OVERTIME -> {
                gameStatusLine.prefix(Component.text("OVERTIME", NamedTextColor.RED, TextDecoration.BOLD))
            }
        }
    }

    fun updateTimer() {
        if(game.gameManager.getGameState() == GameState.OVERTIME) {
            gameStatusLine.suffix(Component.text("", NamedTextColor.WHITE))
        } else {
            gameStatusLine.suffix(Component.text(game.gameTask.getDisplayTimeLeft(), NamedTextColor.WHITE))
        }
    }

    fun updatePlacements() {
        if(game.gameManager.getGameState() == GameState.IDLE) {
            firstPlaceLine.suffix(Component.text("Red Team ", NamedTextColor.RED).append(Component.text("                0c", NamedTextColor.WHITE)))
            secondPlaceLine.suffix(Component.text("Yellow Team ", NamedTextColor.YELLOW).append(Component.text("                0c", NamedTextColor.WHITE)))
            thirdPlaceLine.suffix(Component.text("Lime Team ", NamedTextColor.GREEN).append(Component.text("                0c", NamedTextColor.WHITE)))
            fourthPlaceLine.suffix(Component.text("Blue Team ", NamedTextColor.BLUE).append(Component.text("                0c", NamedTextColor.WHITE)))
        } else {
            val placements = game.scoreManager.getPlacements().keys.toTypedArray()
            val first = placements[0]
            val second = placements[1]
            val third = placements[2]
            val fourth = placements[3]
            firstPlaceLine.suffix(Component.text("${first.teamName} ", first.textColor).append(Component.text("                ${game.scoreManager.getTeamScore(first)}c", NamedTextColor.WHITE)))
            secondPlaceLine.suffix(Component.text("${second.teamName} ", second.textColor).append(Component.text("                ${game.scoreManager.getTeamScore(second)}c", NamedTextColor.WHITE)))
            thirdPlaceLine.suffix(Component.text("${third.teamName} ", third.textColor).append(Component.text("                ${game.scoreManager.getTeamScore(third)}c", NamedTextColor.WHITE)))
            fourthPlaceLine.suffix(Component.text("${fourth.teamName} ", fourth.textColor).append(Component.text("                ${game.scoreManager.getTeamScore(fourth)}c", NamedTextColor.WHITE)))
        }
    }

    fun showScoreboard(player : Player) {
        player.scoreboard = scoreboard
    }

    fun destroyScoreboard() {
        currentRoundLine.unregister()
        gameStatusLine.unregister()
        firstPlaceLine.unregister()
        secondPlaceLine.unregister()
        thirdPlaceLine.unregister()
        fourthPlaceLine.unregister()
        objective.unregister()
    }
}
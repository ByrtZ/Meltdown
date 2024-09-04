package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Sounds
import dev.byrt.meltdown.state.Teams
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

class ScoreManager(private val game : Game) {
    private var placements = HashMap<Teams, Int>()

    fun calculatePlacements() {
        placements.clear()
        placements[Teams.RED] = Teams.RED.score
        placements[Teams.YELLOW] = Teams.YELLOW.score
        placements[Teams.LIME] = Teams.LIME.score
        placements[Teams.BLUE] = Teams.BLUE.score
        placements = placements.toList().sortedBy { (_, scores) -> scores }.reversed().toMap() as HashMap<Teams, Int>
        game.infoBoardManager.updatePlacements()
    }

    fun modifyScore(score : Int, mode : ScoreModificationMode, team : Teams) {
        when(mode) {
            ScoreModificationMode.ADD -> {
                if(team == Teams.RED) {
                    Teams.RED.score += score
                }
                if(team == Teams.YELLOW) {
                    Teams.YELLOW.score += score
                }
                if(team == Teams.LIME) {
                    Teams.LIME.score += score
                }
                if(team == Teams.BLUE) {
                    Teams.BLUE.score += score
                }
            }
            ScoreModificationMode.SUB -> {
                if(team == Teams.RED) {
                    Teams.RED.score -= score
                }
                if(team == Teams.YELLOW) {
                    Teams.YELLOW.score -= score
                }
                if(team == Teams.LIME) {
                    Teams.LIME.score -= score
                }
                if(team == Teams.BLUE) {
                    Teams.BLUE.score -= score
                }
            }
        }
        calculatePlacements()
    }

    fun calculateRoundPlacement() {
        var placementScore = 50
        var roundPlacement = game.teamManager.getActiveTeams().size
        for(team in game.lifestates.getEliminatedTeams() + game.lifestates.getAliveTeams()) {
            game.teamManager.sendTeamMessage(
                Component.text("[")
                    .append(Component.text("+${placementScore}"))
                    .append(Component.text(" coins", NamedTextColor.GOLD))
                    .append(Component.text("] ", NamedTextColor.WHITE))
                    .append(Component.text("["))
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("Your team placed ${roundPlacement}${placementSuffix(roundPlacement)} this round!", NamedTextColor.GREEN, TextDecoration.BOLD)),
                team
            )
            game.teamManager.playTeamSound(Sounds.Score.BIG_ACQUIRED, 1f, team)
            modifyScore(placementScore, ScoreModificationMode.ADD, team)
            placementScore += 50
            roundPlacement--
        }
    }

    private fun placementSuffix(int : Int) : String {
        when(int) {
            4 -> {
                return "th"
            }
            3 -> {
                return "rd"
            }
            2 -> {
                return "nd"
            }
            1 -> {
                return "st"
            } else -> {
                return "null"
            }
        }
    }

    fun getPlacements() : HashMap<Teams, Int> {
        return placements
    }

    fun getTeamScore(team : Teams) : Int {
        return when(team) {
            Teams.RED -> Teams.RED.score
            Teams.YELLOW -> Teams.YELLOW.score
            Teams.LIME -> Teams.LIME.score
            Teams.BLUE -> Teams.BLUE.score
            Teams.SPECTATOR -> 0
        }
    }

    fun resetScores() {
        for(team in Teams.entries) {
            team.score = 0
        }
        placements.clear()
    }
}

enum class ScoreModificationMode {
    ADD,
    SUB
}
package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Teams

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
        for(team in Teams.values()) {
            team.score = 0
        }
        placements.clear()
    }
}

enum class ScoreModificationMode {
    ADD,
    SUB
}
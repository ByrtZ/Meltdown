package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Teams

class ScoreManager(private val game : Game) {
    private var redScore = 0
    private var lastRedScore = 0
    private var yellowScore = 0
    private var lastYellowScore = 0
    private var limeScore = 0
    private var lastLimeScore = 0
    private var blueScore = 0
    private var lastBlueScore = 0
    private var placements = mutableMapOf<Teams, Int>()
    private var lastPlacements = mutableMapOf<Teams, Int>()

    fun calculatePlacements() {
        lastPlacements = placements
        placements.clear()
        placements[Teams.RED] = redScore
        placements[Teams.YELLOW] = yellowScore
        placements[Teams.LIME] = limeScore
        placements[Teams.BLUE] = blueScore
        placements = placements.toList().sortedBy { (_, scores) -> scores }.reversed().toMap() as MutableMap<Teams, Int>
        game.infoBoardManager.updateScoreboardScores()
    }

    fun modifyScore(score : Int, mode : ScoreModificationMode, team : Teams) {
        when(mode) {
            ScoreModificationMode.ADD -> {
                if(team == Teams.RED) {
                    lastRedScore = redScore
                    redScore += score
                }
                if(team == Teams.YELLOW) {
                    lastYellowScore = yellowScore
                    yellowScore += score
                }
                if(team == Teams.LIME) {
                    lastLimeScore = limeScore
                    limeScore += score
                }
                if(team == Teams.BLUE) {
                    lastBlueScore = blueScore
                    blueScore += score
                }
            }
            ScoreModificationMode.SUB -> {
                if(team == Teams.RED) {
                    lastRedScore = redScore
                    redScore -= score
                }
                if(team == Teams.YELLOW) {
                    lastYellowScore = yellowScore
                    yellowScore -= score
                }
                if(team == Teams.LIME) {
                    lastLimeScore = limeScore
                    limeScore -= score
                }
                if(team == Teams.BLUE) {
                    lastBlueScore = blueScore
                    blueScore -= score
                }
            }
        }
        calculatePlacements()
    }

    fun getPlacements() : Map<Teams, Int> {
        return placements
    }

    fun getLastPlacements() : Map<Teams, Int> {
        return lastPlacements
    }

    fun getTeamScore(team : Teams) : Int {
        return when(team) {
            Teams.RED -> redScore
            Teams.YELLOW -> yellowScore
            Teams.LIME -> limeScore
            Teams.BLUE -> blueScore
            Teams.SPECTATOR -> 0
        }
    }

    fun getLastTeamScore(team : Teams) : Int {
        return when(team) {
            Teams.RED -> lastRedScore
            Teams.YELLOW -> lastYellowScore
            Teams.LIME -> lastLimeScore
            Teams.BLUE -> lastBlueScore
            Teams.SPECTATOR -> 0
        }
    }

    fun getRedScore() : Int {
        return redScore
    }

    fun getYellowScore() : Int {
        return yellowScore
    }

    fun getLimeScore() : Int {
        return limeScore
    }

    fun getBlueScore() : Int {
        return blueScore
    }

    fun getLastRedScore() : Int {
        return lastRedScore
    }

    fun getLastYellowScore() : Int {
        return lastYellowScore
    }

    fun getLastLimeScore() : Int {
        return lastLimeScore
    }

    fun getLastBlueScore() : Int {
        return lastBlueScore
    }


    fun resetScores() {
        redScore = 0
        yellowScore = 0
        limeScore = 0
        blueScore = 0
        lastRedScore = 0
        lastYellowScore = 0
        lastLimeScore = 0
        lastBlueScore = 0
        placements.clear()
        lastPlacements.clear()
    }
}

enum class ScoreModificationMode {
    ADD,
    SUB
}
package dev.byrt.meltdown.state

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.util.DevStatus

class Rounds(private val game : Game) {
    private var roundState = RoundState.ONE
    private var totalRounds = 3

    fun nextRound() {
        when(roundState) {
            RoundState.ONE -> { setRoundState(RoundState.TWO) }
            RoundState.TWO -> { setRoundState(RoundState.THREE) }
            RoundState.THREE -> { game.plugin.logger.warning("Attempted to increment past round 3.") }
        }
    }

    fun setRoundState(newRound : RoundState) {
        if(newRound == roundState) return
        game.dev.parseDevMessage("Round state updated from $roundState to $newRound.", DevStatus.INFO)
        this.roundState = newRound
        game.infoBoardManager.updateRound()
    }

    fun getRoundState() : RoundState {
        return roundState
    }

    fun getTotalRounds() : Int {
        return totalRounds
    }
}

enum class RoundState {
    ONE,
    TWO,
    THREE
}
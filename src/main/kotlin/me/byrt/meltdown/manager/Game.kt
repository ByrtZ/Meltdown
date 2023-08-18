package me.byrt.meltdown.manager

import me.byrt.meltdown.Main
import me.byrt.meltdown.task.GameCountdownTask

class Game(private val plugin : Main) {
    private var gameState : GameState = GameState.IDLE
    private var roundState : RoundState = RoundState.ROUND_ONE
    private var timerState : TimerState = TimerState.INACTIVE
    private var gameCountdownTask = GameCountdownTask(this)
    private var infoBoardManager = InfoBoardManager(this)
    private var freezeManager = FreezeManager(this)
    private var heaterManager = HeaterManager(this)

    fun setGameState(newState : GameState) {
        this.gameState = newState
        when(this.gameState) {
            GameState.IDLE -> {
                setTimerState(TimerState.INACTIVE)
                setRoundState(RoundState.ROUND_ONE)
            }
            GameState.STARTING -> {
                setTimerState(TimerState.ACTIVE)
                gameCountdownTask.setTimeLeft(20)
                if(roundState == RoundState.ROUND_ONE) {
                    gameCountdownTask.runTaskTimer(plugin, 0, 20)
                }
            }
            GameState.IN_GAME -> {
                setTimerState(TimerState.ACTIVE)
                gameCountdownTask.setTimeLeft(300)
            }
            GameState.ROUND_END -> {
                setTimerState(TimerState.ACTIVE)
                gameCountdownTask.setTimeLeft(20)
            }
            GameState.GAME_END -> {
                setTimerState(TimerState.ACTIVE)
                gameCountdownTask.setTimeLeft(45)
            }
        }
    }

    fun getGameState(): GameState {
        return this.gameState
    }

    fun getRoundState(): RoundState {
        return this.roundState
    }

    fun setRoundState(roundState : RoundState) {
        this.roundState = roundState
    }

    fun getTimerState(): TimerState {
        return this.timerState
    }

    fun setTimerState(timerState : TimerState) {
        this.timerState = timerState
    }

    fun getGameCountdownTask() : GameCountdownTask {
        return this.gameCountdownTask
    }

    fun getInfoBoardManager() : InfoBoardManager {
        return this.infoBoardManager
    }

    fun getFreezeManager() : FreezeManager {
        return this.freezeManager
    }

    fun getHeaterManager() : HeaterManager {
        return this.heaterManager
    }

    fun cleanUp() {
        infoBoardManager.destroyScoreboard()
    }
}
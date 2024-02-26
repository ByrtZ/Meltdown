package dev.byrt.meltdown.game

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.arena.*
import dev.byrt.meltdown.manager.*
import dev.byrt.meltdown.state.*
import dev.byrt.meltdown.util.*
import dev.byrt.meltdown.task.*
import dev.byrt.meltdown.queue.*

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit

import java.time.Duration

class Game(val plugin : Main) {
    val gameManager = GameManager(this)
    val roundManager = Rounds(this)
    val timerManager = Timer(this)
    val playerManager = PlayerManager(this)
    val teamManager = TeamManager(this)
    val itemManager = ItemManager(this)
    val blockManager = BlockManager(this)
    val entranceManager = EntranceManager(this)
    val infoBoardManager = InfoBoardManager(this)
    val tabListManager = TabListManager(this)
    val locationManager = LocationManager(this)
    val freezeManager = FreezeManager(this)
    val heaterManager = HeaterManager(this)
    val configManager = ConfigManager(this)
    val whitelistManager = WhitelistManager(this)
    val sharedItemManager = SharedItemManager(this)
    val cooldownManager = CooldownManager(this)
    val scoreManager = ScoreManager(this)

    val gameTask = GameTask(this)
    val musicTask = MusicTask(this)
    val heaterTask = HeaterTask(this)
    val freezeTask = FreezeTask(this)
    val eliminatedTask = TeamEliminatedTask(this)

    val queue = Queue(this)
    val queueVisuals = QueueVisuals(this)
    val queueTask = QueueTask(this)

    val dev = Dev(this)

    private var buildMode = false

    fun startGame() {
        if(gameManager.getGameState() == GameState.IDLE) {
            gameManager.nextState()
        } else {
            dev.parseDevMessage("Unable to start, as game is already running.", DevStatus.ERROR)
        }
    }

    fun stopGame() {
        if(gameManager.getGameState() == GameState.IDLE) {
            dev.parseDevMessage("Unable to stop, as no game is running.", DevStatus.ERROR)
        } else {
            gameManager.setGameState(GameState.GAME_END)
        }
    }

    fun setup() {
        infoBoardManager.buildScoreboard()
        teamManager.buildDisplayTeams()
        tabListManager.populateMeltdownPuns()
        locationManager.populateSpawns()
        locationManager.populateEntrances()
        queueVisuals.spawnQueueNPC()
    }

    fun cleanUp() {
        entranceManager.resetEntrances()
        queueVisuals.removeQueueNPC()
        teamManager.destroyDisplayTeams()
        infoBoardManager.destroyScoreboard()
        configManager.saveWhitelistConfig()
    }

    fun reload() {
        gameManager.setGameState(GameState.IDLE)
        roundManager.setRoundState(RoundState.ONE)
        timerManager.setTimerState(TimerState.INACTIVE)
        gameTask.resetVars()
        scoreManager.resetScores()
        locationManager.resetSpawnCounters()
        playerManager.resetPlayers()
        infoBoardManager.destroyScoreboard()
        infoBoardManager.buildScoreboard()
        entranceManager.resetEntrances()
        freezeManager.resetTeamFrozenLists()
        teamManager.showDisplayTeamNames()
        sharedItemManager.clearTelepickaxeOwners()
        queueVisuals.removeQueueNPC()
        queueVisuals.spawnQueueNPC()
        queueVisuals.setAllQueueInvisible()
        queue.setMaxPlayers(16)
        queue.setMinPlayers(8)

        for(player in Bukkit.getOnlinePlayers()) {
            player.showTitle(Title.title(Component.text("\uD000"), Component.text(""), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))))
            Main.getGame().teamManager.addToTeam(player, player.uniqueId, Teams.SPECTATOR)
            if(player.isOp) {
                Main.getGame().teamManager.addToAdminDisplay(player.uniqueId)
            }
        }
    }

    fun setBuildMode(mode : Boolean) {
        this.buildMode = mode
    }

    fun getBuildMode() : Boolean {
        return this.buildMode
    }
}
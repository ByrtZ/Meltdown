package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Teams

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player

import java.util.UUID

class EliminationManager(private val game : Game) {
    private var alivePlayers = ArrayList<UUID>()
    private var aliveTeams = ArrayList<Teams>()
    private var frozenPlayers = ArrayList<UUID>()
    private var eliminatedPlayers = ArrayList<UUID>()
    private var eliminatedTeams = ArrayList<Teams>()

    fun changePlayerLifeState(player : Player, lifeState : PlayerLifeState) {
       if(game.teamManager.isSpectator(player.uniqueId)) return
       when(lifeState) {
           PlayerLifeState.ALIVE -> {
               if(alivePlayers.contains(player.uniqueId)) return
               frozenPlayers.remove(player.uniqueId)
               eliminatedPlayers.remove(player.uniqueId)
               alivePlayers.add(player.uniqueId)
           }
           PlayerLifeState.FROZEN -> {
               if(frozenPlayers.contains(player.uniqueId)) return
               alivePlayers.remove(player.uniqueId)
               eliminatedPlayers.remove(player.uniqueId)
               frozenPlayers.add(player.uniqueId)
           }
           PlayerLifeState.ELIMINATED -> {
               if(eliminatedPlayers.contains(player.uniqueId)) return
               frozenPlayers.remove(player.uniqueId)
               alivePlayers.remove(player.uniqueId)
               eliminatedPlayers.add(player.uniqueId)
               eliminatePlayer(player)
           }
       }
    }

    fun changeTeamLifeState(team : Teams, lifeState : TeamLifeState) {
        if(team == Teams.SPECTATOR) return
        when(lifeState) {
            TeamLifeState.ALIVE -> {
                if(!eliminatedTeams.contains(team) && aliveTeams.contains(team)) return
                eliminatedTeams.remove(team)
                aliveTeams.add(team)
            }
            TeamLifeState.ELIMINATED -> {
                if(eliminatedTeams.contains(team) && !aliveTeams.contains(team)) return
                aliveTeams.remove(team)
                eliminatedTeams.add(team)
                eliminateTeam(team, if(game.gameTask.getTimeLeft() < 10 || !getFullTeamStatus(team).containsValue(PlayerLifeState.FROZEN)) TeamEliminationType.INSTANT else TeamEliminationType.FROZEN)
            }
        }
    }

    fun onPlayerQuit(player : Player) {
        if(!game.teamManager.isSpectator(player.uniqueId)) {
            alivePlayers.remove(player.uniqueId)
            frozenPlayers.remove(player.uniqueId)
            eliminatedPlayers.remove(player.uniqueId)
            eliminatePlayer(player)
        }
    }

    private fun eliminatePlayer(player : Player) {
        player.gameMode = GameMode.SPECTATOR
        player.teleport(game.locationManager.getArenaCentre())
        checkTeamStatus(game.teamManager.getPlayerTeam(player.uniqueId))
    }

    private fun getFullTeamStatus(team : Teams) : HashMap<Player, PlayerLifeState> {
        val eliminatedPlayerTeam = game.teamManager.getTeamPlayers(team)
        val teamStatus = HashMap<Player, PlayerLifeState>()
        for(teamMate in eliminatedPlayerTeam) {
            teamStatus[teamMate] = getPlayerLifeState(teamMate)
        }
        return teamStatus
    }

    fun checkTeamStatus(team : Teams) {
        if(!eliminatedTeams.contains(team)) {
            val teamStatus = getFullTeamStatus(team)
            val canBeEliminatedList = ArrayList<Boolean>()
            for(teamMateStatus in teamStatus) {
                if(teamMateStatus.value != PlayerLifeState.ALIVE && (teamMateStatus.value == PlayerLifeState.FROZEN || teamMateStatus.value == PlayerLifeState.ELIMINATED) && !game.freezeTask.getHeatingList().contains(teamMateStatus.key.uniqueId)) {
                    canBeEliminatedList.add(true)
                } else {
                    canBeEliminatedList.add(false)
                }
            }
            if(!canBeEliminatedList.contains(false)) {
                changeTeamLifeState(team, TeamLifeState.ELIMINATED)
            }
        }
    }

    private fun eliminateTeam(team : Teams, eliminationType : TeamEliminationType) {
        when(eliminationType) {
            TeamEliminationType.FROZEN -> {
                game.eliminatedTask.startEliminateTeamTask(game.teamManager.getTeamPlayers(team), team, 4)
            }
            TeamEliminationType.INSTANT -> {
                game.eliminatedTask.startEliminateTeamTask(game.teamManager.getTeamPlayers(team), team, 0)
            }
        }
    }

    private fun getPlayerLifeState(player : Player) : PlayerLifeState {
        if(alivePlayers.contains(player.uniqueId)) return PlayerLifeState.ALIVE
        if(frozenPlayers.contains(player.uniqueId)) return PlayerLifeState.FROZEN
        if(eliminatedPlayers.contains(player.uniqueId)) return PlayerLifeState.ELIMINATED
        return PlayerLifeState.ELIMINATED // Return if non-existent
    }

    fun getFrozenPlayers() : ArrayList<UUID> {
        return frozenPlayers
    }

    fun isFrozen(player : Player) : Boolean {
        return frozenPlayers.contains(player.uniqueId)
    }

    fun getAlivePlayers() : ArrayList<Player> {
        val players = ArrayList<Player>()
        for(uuid in alivePlayers) {
            Bukkit.getPlayer((uuid))?.let { players.add(it) }
        }
        return players
    }

    fun getAliveTeams() : ArrayList<Teams> {
        val teams = ArrayList<Teams>()
        for(team in aliveTeams) {
            teams.add(team)
        }
        return teams
    }

    fun getEliminatedPlayers() : ArrayList<Player> {
        val players = ArrayList<Player>()
        for(uuid in eliminatedPlayers) {
            Bukkit.getPlayer((uuid))?.let { players.add(it) }
        }
        return players
    }

    fun getEliminatedTeams() : ArrayList<Teams> {
        val teams = ArrayList<Teams>()
        for(team in eliminatedTeams) {
            teams.add(team)
        }
        return teams
    }

    fun reset() {
        alivePlayers.clear()
        aliveTeams.clear()
        frozenPlayers.clear()
        eliminatedPlayers.clear()
        eliminatedTeams.clear()
    }
}

enum class PlayerLifeState {
    ALIVE,
    FROZEN,
    ELIMINATED
}

enum class TeamLifeState {
    ALIVE,
    ELIMINATED
}

enum class TeamEliminationType {
    FROZEN,
    INSTANT
}
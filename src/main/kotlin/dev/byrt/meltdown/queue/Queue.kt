package dev.byrt.meltdown.queue

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.state.Sounds
import dev.byrt.meltdown.util.DevStatus

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.util.UUID

class Queue(private val game : Game) {
    private var queue= mutableSetOf<UUID>()
    private var queueState = QueueState.IDLE
    private var maxPlayers = 16
    private var minPlayers = 8

    fun joinQueue(player : Player) {
        if(queue.contains(player.uniqueId)) return
        if(queue.size > maxPlayers) return
        if(game.gameManager.getGameState() != GameState.IDLE) return
        if(queue.isEmpty()) { setQueueState(QueueState.AWAITING_PLAYERS) }

        queue.add(player.uniqueId)
        game.queueVisuals.updateQueueStatus()
        game.queueVisuals.setQueueVisible()
        game.queueVisuals.giveQueueItem(player)
        player.playSound(player.location, Sounds.Queue.QUEUE_JOIN, 1.0f, 1.5f)

        if(queue.size >= minPlayers && !game.queueTask.getQueueActive()) {
            setQueueState(QueueState.SENDING_PLAYERS_TO_GAME)
        }

        player.sendMessage(Component.text("You joined the queue for Meltdown.", NamedTextColor.GREEN))
        game.dev.parseDevMessage("${player.name} joined the queue.", DevStatus.INFO)
        game.plugin.logger.info("[QUEUE] ${player.name} joined the queue (Queue: ${queue}).")
    }

    fun leaveQueue(player : Player) {
        if(queue.contains(player.uniqueId)) {
            queue.remove(player.uniqueId)
            game.queueVisuals.removeQueueItem(player)
            game.queueVisuals.setQueueInvisible(player)
            game.queueVisuals.updateQueueStatus()
            player.playSound(player.location, Sounds.Queue.QUEUE_LEAVE, 1.0f, 1.0f)

            if(queue.isEmpty()) {
                setQueueState(QueueState.IDLE)
            }

            player.sendMessage(Component.text("You left the queue for Meltdown.", NamedTextColor.RED))
            game.dev.parseDevMessage("${player.name} left the queue.", DevStatus.INFO)
            game.plugin.logger.info("[QUEUE] ${player.name} left the queue (Queue: ${queue}).")
        } else {
            return
        }
    }

    fun deleteQueue() {
        queue.clear()
    }

    fun checkQueueCanStart() {
        if(queue.size >= minPlayers && !game.queueTask.getQueueActive()) {
            setQueueState(QueueState.SENDING_PLAYERS_TO_GAME)
        }
    }

    fun queuedAudience() : Audience {
        return Audience.audience(Bukkit.getOnlinePlayers().filter { p : Player -> queue.contains(p.uniqueId) })
    }

    fun queuedPlayers() : Collection<Player> {
        return Bukkit.getOnlinePlayers().filter { p : Player -> queue.contains(p.uniqueId) }
    }

    fun getQueue() : Set<UUID> {
        return queue
    }

    fun getQueueState() : QueueState {
        return queueState
    }

    fun setQueueState(newState : QueueState) {
        if(newState != queueState) {
            game.dev.parseDevMessage("Queue State updated from $queueState to $newState.", DevStatus.INFO)
            queueState = newState
            game.queueVisuals.updateQueueStatus()
        }
    }

    fun getMaxPlayers() : Int {
        return maxPlayers
    }

    fun setMaxPlayers(int : Int) {
        if(int < 1) return
        maxPlayers = int
        game.dev.parseDevMessage("Queue maximum players set to ${maxPlayers}.", DevStatus.INFO)
    }

    fun getMinPlayers() : Int {
        return minPlayers
    }

    fun setMinPlayers(int : Int) {
        if(int < 1) return
        if(int > maxPlayers) return
        minPlayers = int
        game.dev.parseDevMessage("Queue minimum players set to ${minPlayers}.", DevStatus.INFO)
    }
}
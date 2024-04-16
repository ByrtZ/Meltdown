package dev.byrt.meltdown.task

import dev.byrt.meltdown.arena.DoorOperation
import dev.byrt.meltdown.data.Room
import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.manager.PlayerLifeState
import dev.byrt.meltdown.state.Sounds

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class MeltingRoomTask(private val game : Game) {
    private var meltingRoomTaskMap = mutableMapOf<Room, BukkitRunnable>()
    fun startMeltingTask(room : Room) {
        var quarterSeconds = 0
        var seconds = 0
        val meltingBackground = BossBar.bossBar(Component.text("\uD006"), 0.0f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)
        val meltingTitle = BossBar.bossBar(Component.text("MELTING ROOM", NamedTextColor.RED, TextDecoration.BOLD), 0.0f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)
        val meltingStatus = BossBar.bossBar(Component.text("...", NamedTextColor.GOLD), 0.0f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)

        for(player in Bukkit.getOnlinePlayers()) {
            if(isPlayerInRoom(room, player)) {
                player.playSound(player.location, Sounds.Alert.GENERAL_ALERT, 1.0f, 1.0f)
                player.playSound(player.location, Sounds.Melting.BEGIN_MELTING, 1.0f, 1.0f)
                player.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("ROOM MELTDOWN ALERT! ", NamedTextColor.RED, TextDecoration.BOLD))
                    .append(Component.text("This room has begun melting.", NamedTextColor.GOLD).decoration(TextDecoration.BOLD, false)
                    )
                )
            }
        }

        val meltingRoomRunnable = object : BukkitRunnable() {
            override fun run() {
                if(game.gameManager.getGameState() == GameState.IN_GAME || game.gameManager.getGameState() == GameState.OVERTIME) {
                    if(quarterSeconds % 2 == 0) {
                        meltingTitle.name(Component.text(if(seconds <= MELTING_TIME - 8) "MELTING ROOM" else "MELTDOWN IMMINENT", NamedTextColor.DARK_RED, TextDecoration.BOLD))
                    } else {
                        meltingTitle.name(Component.text(if(seconds <= MELTING_TIME - 8) "MELTING ROOM" else "MELTDOWN IMMINENT", NamedTextColor.RED, TextDecoration.BOLD))
                    }
                    if(quarterSeconds >= 4) {
                        meltingStatus.name(Component.text("Room melting in ", NamedTextColor.GOLD).append(Component.text("${(MELTING_TIME - 1) - seconds}s", NamedTextColor.WHITE)).append(Component.text("!", NamedTextColor.GOLD)))
                        for(player in Bukkit.getOnlinePlayers()) {
                            if(isPlayerInRoom(room, player)) {
                                player.playSound(player.location, Sounds.Melting.MELTING_LOOP, 1.0f, 0.0f)
                                if(!meltingBackground.viewers().contains(player)) meltingBackground.addViewer(player)
                                if(!meltingTitle.viewers().contains(player)) meltingTitle.addViewer(player)
                                if(!meltingStatus.viewers().contains(player)) meltingStatus.addViewer(player)
                            } else {
                                meltingBackground.removeViewer(player)
                                meltingTitle.removeViewer(player)
                                meltingStatus.removeViewer(player)
                            }
                        }
                        quarterSeconds = 0
                        seconds++
                    }
                    if(seconds == MELTING_TIME - 8 && quarterSeconds == 0) {
                        for(door in room.doors) {
                            game.blockManager.doorCloseStageOne(door, Material.NETHERITE_BLOCK)
                        }
                        for(player in Bukkit.getOnlinePlayers()) {
                            if(isPlayerInRoom(room, player)) {
                                player.sendMessage(Component.text("[")
                                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                                    .append(Component.text("] "))
                                    .append(Component.text("MELTDOWN IMMINENT! ", NamedTextColor.RED, TextDecoration.BOLD))
                                    .append(Component.text("This room is entering full lockdown.", NamedTextColor.GOLD).decoration(TextDecoration.BOLD, false)
                                    )
                                )
                            }
                        }
                    }
                    if(seconds == MELTING_TIME - 4 && quarterSeconds == 0) {
                        for(door in room.doors) {
                            game.blockManager.doorCloseStageTwo(door, Material.NETHERITE_BLOCK)
                        }
                    }
                    if(seconds == MELTING_TIME && quarterSeconds == 0) {
                        for(door in room.doors) {
                            game.blockManager.doorCloseStageThree(door, Material.NETHERITE_BLOCK)
                        }
                        for(player in Bukkit.getOnlinePlayers()) {
                            if(isPlayerInRoom(room, player)) {
                                meltingBackground.removeViewer(player)
                                meltingTitle.removeViewer(player)
                                meltingStatus.removeViewer(player)
                            }
                            if(!game.teamManager.isSpectator(player.uniqueId) && !game.eliminationManager.getEliminatedPlayers().contains(player)) {
                                if(isPlayerInRoom(room, player)) {
                                    game.eliminationManager.changePlayerLifeState(player, PlayerLifeState.ELIMINATED)
                                    player.playSound(player.location, Sounds.Score.MELTED_BY_MELTDOWN, 1.0f, 0.75f)
                                    game.teamManager.sendGlobalMessage(Component.text("[")
                                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                                        .append(Component.text("] "))
                                        .append(Component.text(player.name).color(game.teamManager.getPlayerTeam(player.uniqueId).textColor))
                                        .append(Component.text(" got caught in a melting room!")
                                        )
                                    )
                                }
                            }
                        }
                        stopMeltingTask(room)
                    }
                    quarterSeconds++
                } else {
                    for(player in Bukkit.getOnlinePlayers()) {
                        if(isPlayerInRoom(room, player)) {
                            meltingBackground.removeViewer(player)
                            meltingTitle.removeViewer(player)
                            meltingStatus.removeViewer(player)
                        }
                    }
                    stopMeltingTask(room)
                }
            }
        }
        meltingRoomRunnable.runTaskTimer(game.plugin, 0L, 5L)
        meltingRoomTaskMap[room] = meltingRoomRunnable
    }

    fun stopMeltingTask(room : Room) {
        for(door in room.doors) {
            game.doorManager.closeDoor(door, Material.NETHERITE_BLOCK, DoorOperation.CLOSE_MELTDOWN)
        }
        meltingRoomTaskMap.remove(room)?.cancel()
    }

    fun getMeltingTaskMap() : Map<Room, BukkitRunnable> {
        return meltingRoomTaskMap
    }

    fun isPlayerInRoom(room : Room, player : Player) : Boolean {
        return if(room.corner1.blockX <= player.location.x && player.location.x <= room.corner2.blockX) {
            if(room.corner1.blockY <= player.location.y && player.location.y <= room.corner2.blockY) {
                room.corner1.blockZ <= player.location.z && player.location.z <= room.corner2.blockZ
            } else {
                false
            }
        } else {
            false
        }
    }

    companion object {
        const val MELTING_TIME = 30
    }
}
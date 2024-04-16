package dev.byrt.meltdown.arena

import dev.byrt.meltdown.data.Room
import dev.byrt.meltdown.game.Game

class MeltingManager(private val game : Game) {
    fun startMelting(room : Room) {
        game.meltingRoomTask.startMeltingTask(room)
    }

    fun stopMelting(room : Room) {
        game.meltingRoomTask.stopMeltingTask(room)
    }
}
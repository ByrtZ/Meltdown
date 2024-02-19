package dev.byrt.meltdown.queue

enum class QueueState {
    IDLE,
    AWAITING_PLAYERS,
    NO_GAME_AVAILABLE,
    SENDING_PLAYERS_TO_GAME
}
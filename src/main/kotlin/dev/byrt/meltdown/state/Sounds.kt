package dev.byrt.meltdown.state

class Sounds {
    object Music {
        const val GAME_MUSIC = "event.music.meltdown"
        const val OVERTIME_INTRO_MUSIC = "mcc.overtime_intro"
        const val OVERTIME_MUSIC = "mcc.overtime"
        const val GAME_OVER_MUSIC = "mcc.game_over"
        const val ROUND_OVER_MUSIC = ""
    }
    object Timer {
        const val STARTING_123 = "event.123"
        const val CLOCK_TICK = "event.clockticknormal"
        const val CLOCK_TICK_HIGH = "event.clocktickhigh"
    }
    object Round {
        const val ROUND_START_PLING = "event.go"
        const val ROUND_END_PLING = "event.roundend"
        const val ROUND_STARTING = ""
        const val WIN_ROUND = "ui.toast.challenge_complete"
        const val LOSE_ROUND = "entity.ender_dragon.growl"
        const val DRAW_ROUND = "entity.wither.spawn"
        const val ENTRANCE = "event.meltdown_entrance"
    }
    object GameOver {
        const val GAME_OVER_PLING = "event.roundend"
        const val GAME_OVER_EFFECT_1 = "block.respawn_anchor.deplete"
        const val GAME_OVER_EFFECT_2 = "ui.toast.challenge_complete"
    }
    object Start {
        const val START_GAME_SUCCESS = "block.end_portal.spawn"
        const val START_GAME_FAIL = "entity.enderman.teleport"
    }
    object Bow {
        const val FROST_BOW_SHOOT = "event.bow"
        const val FROST_ARROW_HIT = "event.bow_hit"
    }
    object Heater {
        const val HEATER_LOOP = "event.heater_loop_1000ms"
        const val HEATER_PLACE = "event.heater_place"
        const val HEATER_BREAK = "event.heater_break"
        const val HEATER_RECHARGE = "event.heater_recharge"
    }
    object Freeze {
        const val FROZEN = "event.meltdown_frozen"
        const val UNFREEZE = "event.meltdown_unfrozen"
    }
    object Score {
        const val ELIMINATION = "event.scoreacquired"
        const val TEAM_ELIMINATED = "event.teameliminated"
    }
    object Alert {
        const val GENERAL_ALERT = "block.note_block.bit"
        const val OVERTIME_ALERT = "block.portal.travel"
    }
    object Tutorial {
        const val TUTORIAL_POP = "entity.item.pickup"
    }
    object Command {
        const val SHUFFLE_START = "block.note_block.flute"
        const val SHUFFLE_COMPLETE = "block.note_block.flute"
        const val SHUFFLE_FAIL = "block.note_block.didgeridoo"
        const val WHITELIST_START = "block.note_block.flute"
        const val WHITELIST_COMPLETE = "block.note_block.flute"
        const val WHITELIST_FAIL = "block.note_block.didgeridoo"
        const val PING = "entity.experience_orb.pickup"
        const val BUILDMODE_SUCCESS = "entity.mooshroom.convert"
        const val BUILDMODE_FAIL = "entity.enderman.teleport"
    }
}
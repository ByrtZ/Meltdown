package dev.byrt.meltdown.state

class Sounds {
    object Music {
        const val GAME_MUSIC = "mcc.music.meltdown"
        const val OVERTIME_INTRO_MUSIC = "mcc.music.overtime_intro"
        const val OVERTIME_MUSIC = "mcc.music.overtime"
        const val GAME_STARTING_MUSIC = "mcc.music.game_starting"
        const val GAME_OVER_MUSIC = "mcc.music.game_over"
        const val ROUND_OVER_MUSIC = "mcc.music.round_end"
    }
    object Timer {
        const val STARTING_123 = "mcc.game.123"
        const val STARTING_GO = "mcc.game.go"
        const val CLOCK_TICK = "mcc.game.timer.tick_normal"
        const val CLOCK_TICK_HIGH = "mcc.game.timer.tick_high"
    }
    object Round {
        const val ROUND_END = "mcc.game.roundend"
        const val ENTRANCE = "mcc.meltdown.entrance"
    }
    object GameOver {
        const val GAME_OVER = "mcc.game.roundend"
    }
    object Start {
        const val START_GAME_SUCCESS = "block.respawn_anchor.set_spawn"
        const val START_GAME_FAIL = "entity.enderman.teleport"
    }
    object Queue {
        const val QUEUE_JOIN = "block.note_block.flute"
        const val QUEUE_LEAVE = "block.note_block.didgeridoo"
        const val QUEUE_FIND_GAME = "block.end_portal.spawn"
        const val QUEUE_TELEPORT = "block.portal.trigger"
        const val QUEUE_TICK = "block.note_block.pling"
    }
    object Bow {
        const val FROST_BOW_SHOOT = "mcc.meltdown.freeze_bow_fire"
        const val FROST_ARROW_HIT = "mcc.meltdown.freeze_bow_hit"
    }
    object Heater {
        const val HEATER_LOOP = "block.beacon.ambient"
        const val HEATER_PLACE = "block.anvil.place"
        const val HEATER_BREAK = "block.anvil.destroy"
        const val HEATER_RECHARGE = "block.amethyst_block.resonate"
    }
    object Telepickaxe {
        const val CLAIM = "entity.iron_golem.repair"
    }
    object Freeze {
        const val FROZEN = "mcc.meltdown.frozen"
        const val UNFREEZE = "block.lava.extinguish"
    }
    object Melting {
        const val BEGIN_MELTING = "mcc.meltdown.alarm_start"
        const val MELTING_LOOP = "mcc.meltdown.alarm_loop"
    }
    object Score {
        const val ACQUIRED = "mcc.game.score.acquired"
        const val BIG_ACQUIRED = "mcc.game.score.bigacquired"
        const val UNDO_ELIMINATION = "entity.enderman.teleport"
        const val FALL_INTO_LAVA = "entity.player.hurt_on_fire"
        const val SQUASHED_BY_DOOR = "entity.iron_golem.death"
        const val MELTED_BY_MELTDOWN = "entity.blaze.death"
        const val TEAM_ELIMINATED = "mcc.game.team_eliminated"
        const val MINE_COIN_CRATE = "mcc.game.score.small_coins"
    }
    object Alert {
        const val GENERAL_ALERT = "mcc.game.map_alert"
        const val GENERAL_UPDATE = "mcc.game.map_update"
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
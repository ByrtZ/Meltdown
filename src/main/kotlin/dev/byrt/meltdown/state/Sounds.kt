package dev.byrt.meltdown.state

class Sounds {
    object Music {
        const val GAME_MUSIC = "mcc.meltdown"
        const val OVERTIME_INTRO_MUSIC = "mcc.overtime_intro"
        const val OVERTIME_MUSIC = "mcc.overtime"
        const val GAME_OVER_MUSIC = "mcc.game_over"
        const val ROUND_OVER_MUSIC = ""
    }
    object Timer {
        const val STARTING_123 = "block.note_block.pling"
        const val CLOCK_TICK = "block.note_block.bass"
        const val CLOCK_TICK_HIGH = "block.note_block.bass"
    }
    object Round {
        const val ROUND_START_PLING = "block.note_block.pling"
        const val ROUND_END_PLING = "block.note_block.pling"
        const val ROUND_END_JINGLE = "block.respawn_anchor.deplete"
        const val ROUND_STARTING = ""
        const val WIN_ROUND = "ui.toast.challenge_complete"
        const val LOSE_ROUND = "entity.ender_dragon.growl"
        const val DRAW_ROUND = "entity.wither.spawn"
        const val ENTRANCE = "block.end_portal.spawn"
    }
    object GameOver {
        const val GAME_OVER_PLING = "block.note_block.pling"
        const val GAME_OVER_JINGLE = "block.respawn_anchor.deplete"
        const val GAME_OVER_COMPLETE = "ui.toast.challenge_complete"
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
        const val FROST_BOW_SHOOT = "entity.player.hurt_freeze"
        const val FROST_ARROW_HIT = "event.bow_hit"
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
        const val FROZEN_1 = "entity.player.hurt_freeze"
        const val FROZEN_2 = "entity.generic.extinguish_fire"
        const val UNFREEZE = "block.lava.extinguish"
    }
    object Melting {
        const val BEGIN_MELTING = "item.trident.thunder"
        const val MELTING_LOOP = "block.amethyst_block.resonate"
    }
    object Score {
        const val ELIMINATION = "entity.player.levelup"
        const val UNDO_ELIMINATION = "entity.enderman.teleport"
        const val FALL_INTO_LAVA = "entity.player.hurt_on_fire"
        const val SQUASHED_BY_DOOR = "entity.iron_golem.death"
        const val MELTED_BY_MELTDOWN = "entity.blaze.death"
        const val TEAM_ELIMINATED = "block.beacon.deactivate"
        const val MINE_COIN_CRATE = "entity.experience_orb.pickup"
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
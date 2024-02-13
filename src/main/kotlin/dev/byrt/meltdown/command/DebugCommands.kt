package dev.byrt.meltdown.command

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.util.DevStatus

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player

@Suppress("unused")
class DebugCommands : BaseCommand {
    @CommandMethod("debug next_phase")
    @CommandDescription("Debug command to set game to its next phase.")
    @CommandPermission("meltdown.debug")
    fun debugNextPhase(sender : Player) {
        Main.getGame().dev.parseDevMessage("Game pushed to next phase by ${sender.name}.", DevStatus.WARNING)
        Main.getGame().gameManager.nextState()
    }

    @CommandMethod("debug force_state <state>")
    @CommandDescription("Debug command to force set game state.")
    @CommandPermission("meltdown.debug")
    fun debugForceState(sender : Player, @Argument("state") state : GameState) {
        Main.getGame().dev.parseDevMessage("Game state updated to $state by ${sender.name}.", DevStatus.SEVERE)
        Main.getGame().gameManager.forceState(state)
    }

    @CommandMethod("debug freeze <player>")
    @CommandDescription("Force freezes a player")
    @CommandPermission("meltdown.freeze")
    fun debugFreeze(sender : Player, @Argument("player") player : Player) {
        Main.getGame().dev.parseDevMessage("${sender.name} force froze ${player.name}.", DevStatus.WARNING)
        Main.getGame().freezeManager.freezePlayer(player, null)
    }

    @CommandMethod("debug unfreeze <player>")
    @CommandDescription("Force unfreezes a player")
    @CommandPermission("meltdown.unfreeze")
    fun debugUnfreeze(sender : Player, @Argument("player") player : Player) {
        Main.getGame().dev.parseDevMessage("${sender.name} force unfroze ${player.name}.", DevStatus.WARNING)
        Main.getGame().freezeManager.unfreezePlayer(player)
    }

    @CommandMethod("debug data entrances")
    @CommandDescription("Debug command to get data.")
    @CommandPermission("meltdown.debug")
    fun debugEntranceData(sender : Player) {
        sender.sendMessage(Component.text("Entrance Data\n${Main.getGame().entranceManager.getEntrancesList()}", NamedTextColor.YELLOW))
    }

    @CommandMethod("debug data heaters")
    @CommandDescription("Debug command to get data.")
    @CommandPermission("meltdown.debug")
    fun debugHeaterData(sender : Player) {
        sender.sendMessage(Component.text("Heater Data\n${Main.getGame().heaterManager.getHeaterList()}", NamedTextColor.YELLOW))
    }

    @CommandMethod("debug data frozen")
    @CommandDescription("Debug command to get data.")
    @CommandPermission("meltdown.debug")
    fun debugFrozenData(sender : Player) {
        sender.sendMessage(Component.text("Frozen Players\n${Main.getGame().freezeManager.getFrozenPlayers()}", NamedTextColor.YELLOW))
    }
}
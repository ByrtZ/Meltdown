package dev.byrt.meltdown.command

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.util.DevStatus

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import cloud.commandframework.types.tuples.Triplet

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
        sender.sendMessage(Component.text("Frozen Players\n${Main.getGame().lifestates.getFrozenPlayers()}", NamedTextColor.YELLOW))
    }

    @CommandMethod("debug data lifestates")
    @CommandDescription("Debug command to get data.")
    @CommandPermission("meltdown.debug")
    fun debugLifestateData(sender : Player) {
        sender.sendMessage(Component.text("Players Alive:\n${Main.getGame().lifestates.getAlivePlayers()}\nTeams Alive:\n${Main.getGame().lifestates.getAliveTeams()}", NamedTextColor.GREEN))
        sender.sendMessage(Component.text("Players Frozen:\n${Main.getGame().lifestates.getFrozenPlayers()}", NamedTextColor.AQUA))
        sender.sendMessage(Component.text("Players Eliminated:\n${Main.getGame().lifestates.getEliminatedPlayers()}\nTeams Eliminated:\n${Main.getGame().lifestates.getEliminatedTeams()}", NamedTextColor.RED))
    }

    @CommandMethod("admin toggle_hud")
    @CommandDescription("Admin command to show the debug information HUD.")
    @CommandPermission("meltdown.admin")
    fun toggleAdminHud(sender : Player) {
        if(Main.getGame().admin.getAdminHudUsers()[sender.uniqueId] == false || Main.getGame().admin.getAdminHudUsers()[sender.uniqueId] == null) {
            Main.getGame().admin.showAdminHud(sender)
            sender.sendMessage(Component.text("\uD002 ").append(Component.text("Enabled admin HUD.", NamedTextColor.GREEN)))
        } else {
            Main.getGame().admin.removeAdminHudUser(sender)
            sender.sendMessage(Component.text("\uD002 ").append(Component.text("Disabled admin HUD.", NamedTextColor.RED)))
        }
    }

    @CommandMethod("debug door open all")
    @CommandDescription("Debug command to change doors.")
    @CommandPermission("meltdown.debug")
    fun debugOpenAllDoors(sender : Player) {
        Main.getGame().doorManager.openAllDoors()
        Main.getGame().dev.parseDevMessage("All doors opened by ${sender.name}.", DevStatus.INFO)
    }

    @CommandMethod("debug melt <x, y, z>")
    @CommandDescription("Debug command to test melting.")
    @CommandPermission("meltdown.debug")
    fun debugMelting(sender : Player, @Argument("x, y, z") coords : Triplet<Int, Int, Int>) {
        Main.getGame().dev.parseDevMessage("Melting started at ${coords.first}, ${coords.second}, ${coords.third} by ${sender.name}.", DevStatus.INFO)

    }
}
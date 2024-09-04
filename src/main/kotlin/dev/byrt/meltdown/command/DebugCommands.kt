package dev.byrt.meltdown.command

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.util.DevStatus

import io.papermc.paper.command.brigadier.CommandSourceStack

import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class DebugCommands {
    @Command("debug next_phase")
    @CommandDescription("Debug command to set game to its next phase.")
    @Permission("meltdown.debug")
    fun debugNextPhase(css: CommandSourceStack) {
        Main.getGame().dev.parseDevMessage("Game pushed to next phase by ${css.sender.name}.", DevStatus.WARNING)
        Main.getGame().gameManager.nextState()
    }

    @Command("debug force_state <state>")
    @CommandDescription("Debug command to force set game state.")
    @Permission("meltdown.debug")
    fun debugForceState(css: CommandSourceStack, @Argument("state") state : GameState) {
        Main.getGame().dev.parseDevMessage("Game state updated to $state by ${css.sender.name}.", DevStatus.SEVERE)
        Main.getGame().gameManager.forceState(state)
    }

    @Command("debug freeze <player>")
    @CommandDescription("Force freezes a player")
    @Permission("meltdown.freeze")
    fun debugFreeze(css: CommandSourceStack, @Argument("player") player : Player) {
        Main.getGame().dev.parseDevMessage("${css.sender.name} force froze ${player.name}.", DevStatus.WARNING)
        Main.getGame().freezeManager.freezePlayer(player, null)
    }

    @Command("debug unfreeze <player>")
    @CommandDescription("Force unfreezes a player")
    @Permission("meltdown.unfreeze")
    fun debugUnfreeze(css: CommandSourceStack, @Argument("player") player : Player) {
        Main.getGame().dev.parseDevMessage("${css.sender.name} force unfroze ${player.name}.", DevStatus.WARNING)
        Main.getGame().freezeManager.unfreezePlayer(player)
    }

    @Command("data entrances")
    @CommandDescription("Debug command to get data.")
    @Permission("meltdown.debug")
    fun debugEntranceData(css: CommandSourceStack) {
        css.sender.sendMessage(Component.text("Entrance Data\n${Main.getGame().entranceManager.getEntrancesList()}", NamedTextColor.YELLOW))
    }

    @Command("data heaters")
    @CommandDescription("Debug command to get data.")
    @Permission("meltdown.debug")
    fun debugHeaterData(css: CommandSourceStack) {
        css.sender.sendMessage(Component.text("Heater Data\n${Main.getGame().heaterManager.getHeaterList()}", NamedTextColor.YELLOW))
    }

    @Command("data frozen")
    @CommandDescription("Debug command to get data.")
    @Permission("meltdown.debug")
    fun debugFrozenData(css: CommandSourceStack) {
        css.sender.sendMessage(Component.text("Frozen Players\n${Main.getGame().lifestates.getFrozenPlayers()}", NamedTextColor.YELLOW))
    }

    @Command("data lifestates")
    @CommandDescription("Debug command to get data.")
    @Permission("meltdown.debug")
    fun debugLifestateData(css: CommandSourceStack) {
        css.sender.sendMessage(Component.text("Players Alive:\n${Main.getGame().lifestates.getAlivePlayers()}\nTeams Alive:\n${Main.getGame().lifestates.getAliveTeams()}", NamedTextColor.GREEN))
        css.sender.sendMessage(Component.text("Players Frozen:\n${Main.getGame().lifestates.getFrozenPlayers()}", NamedTextColor.AQUA))
        css.sender.sendMessage(Component.text("Players Eliminated:\n${Main.getGame().lifestates.getEliminatedPlayers()}\nTeams Eliminated:\n${Main.getGame().lifestates.getEliminatedTeams()}", NamedTextColor.RED))
    }

    @Command("admin hud")
    @CommandDescription("Admin command to show the debug information HUD.")
    @Permission("meltdown.admin")
    fun toggleAdminHud(css: CommandSourceStack) {
        if(css.sender is Player) {
            val player = css.sender as Player
            if(Main.getGame().admin.getAdminHudUsers()[player.uniqueId] == false || Main.getGame().admin.getAdminHudUsers()[player.uniqueId] == null) {
                Main.getGame().admin.showAdminHud(player)
                css.sender.sendMessage(Component.text("\uD002 ").append(Component.text("Enabled admin HUD.", NamedTextColor.GREEN)))
            } else {
                Main.getGame().admin.removeAdminHudUser(player)
                css.sender.sendMessage(Component.text("\uD002 ").append(Component.text("Disabled admin HUD.", NamedTextColor.RED)))
            }
        }
    }

    @Command("debug door open all")
    @CommandDescription("Debug command to change doors.")
    @Permission("meltdown.debug")
    fun debugOpenAllDoors(css: CommandSourceStack) {
        Main.getGame().doorManager.openAllDoors()
        Main.getGame().dev.parseDevMessage("All doors opened by ${css.sender.name}.", DevStatus.INFO)
    }

    @Command("debug melt <x> <y> <z>")
    @CommandDescription("Debug command to test melting.")
    @Permission("meltdown.debug")
    fun debugMelting(css: CommandSourceStack, @Argument("x") x : Int, @Argument("y") y : Int, @Argument("z") z : Int) {
        if(css.sender is Player && css.sender.name == "Byrt") {
            val player = css.sender as Player
            Main.getGame().dev.parseDevMessage("Melting started at $x, $y, $z by ${css.sender.name}.", DevStatus.INFO)
            val block = player.world.getBlockAt(x, y, z)
            Main.getGame().meltingManager.melt(block)
        }
    }

    @Command("debug melt reset")
    @CommandDescription("Debug command to test melting.")
    @Permission("meltdown.debug")
    fun debugMeltingReset(css: CommandSourceStack) {
        if(css.sender is Player) {
            Main.getGame().dev.parseDevMessage("Melted blocks reset by ${css.sender.name}.", DevStatus.INFO)
            Main.getGame().meltingManager.resetMeltedBlocks()
        }
    }

    @Command("debug meltable get")
    @CommandDescription("Get materials in the meltable blocks list.")
    @Permission("meltdown.debug")
    fun debugMeltingGet(css: CommandSourceStack) {
        css.sender.sendMessage(Component.text("Meltable blocks: ${Main.getGame().meltingManager.getMeltableBlockTypes()}"))
    }

    @Command("debug melted get")
    @CommandDescription("Get list of all melted blocks.")
    @Permission("meltdown.debug")
    fun debugMeltedGet(css: CommandSourceStack) {
        css.sender.sendMessage(Component.text("Melted blocks: ${Main.getGame().meltingManager.getMeltedBlocks()}"))
    }
}
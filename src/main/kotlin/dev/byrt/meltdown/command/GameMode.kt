package dev.byrt.meltdown.command

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.util.DevStatus
import io.papermc.paper.command.brigadier.CommandSourceStack

import org.bukkit.GameMode
import org.bukkit.entity.Player

import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class GameMode {
    @Command("gm <mode>")
    @CommandDescription("Puts the executing player into the specified gamemode.")
    @Permission("meltdown.gm")
    fun gm(css: CommandSourceStack, @Argument("mode") mode : PlayerGameModes) {
        if(css.sender is Player) {
            val player = css.sender as Player
            when(mode) {
                PlayerGameModes.S -> {
                    if(player.gameMode != GameMode.SURVIVAL) {
                        player.gameMode = GameMode.SURVIVAL
                        Main.getGame().dev.parseDevMessage("${player.name} set own game mode to Survival mode.", DevStatus.INFO)
                    }
                }
                PlayerGameModes.C -> {
                    if(player.gameMode != GameMode.CREATIVE) {
                        player.gameMode = GameMode.CREATIVE
                        Main.getGame().dev.parseDevMessage("${player.name} set own game mode to Creative mode.", DevStatus.INFO)
                    }
                }
                PlayerGameModes.A -> {
                    if(player.gameMode != GameMode.ADVENTURE) {
                        player.gameMode = GameMode.ADVENTURE
                        Main.getGame().dev.parseDevMessage("${player.name} set own game mode to Adventure mode.", DevStatus.INFO)
                    }
                }
                PlayerGameModes.SP -> {
                    if(player.gameMode != GameMode.SPECTATOR) {
                        player.gameMode = GameMode.SPECTATOR
                        Main.getGame().dev.parseDevMessage("${player.name} set own game mode to Spectator mode.", DevStatus.INFO)
                    }
                }
            }
        }
    }
}

enum class PlayerGameModes {
    S,
    C,
    A,
    SP
}
package me.byrt.meltdown.command

import me.byrt.meltdown.Main
import me.byrt.meltdown.manager.GameState

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player

@Suppress("unused")
class StartGame : BaseCommand {
    @CommandMethod("startgame")
    @CommandDescription("Starts a game of Meltdown.")
    @CommandPermission("meltdown.startgame")
    fun start(sender : Player) {
        if(Main.getGame().getGameState() == GameState.IDLE) {
            sender.sendMessage(Component.text("Starting Meltdown game!").color(NamedTextColor.GREEN))
            Main.getGame().setGameState(GameState.STARTING)
        } else if(Main.getGame().getGameState() == GameState.GAME_END) {
            sender.sendMessage(Component.text("A restart is required for a new game of Meltdown.").color(NamedTextColor.RED))
        } else {
            sender.sendMessage(Component.text("There is already a game of Meltdown running, you numpty!").color(NamedTextColor.RED))
        }
    }
}
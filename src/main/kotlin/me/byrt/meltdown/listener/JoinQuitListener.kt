package me.byrt.meltdown.listener

import me.byrt.meltdown.Main
import me.byrt.meltdown.manager.GameState

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor

import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@Suppress("unused")
class JoinQuitListener : Listener {
    @EventHandler
    private fun onJoin(e : PlayerJoinEvent) {
        Main.getGame().getInfoBoardManager().showScoreboard(e.player)
        e.joinMessage(Component.text("${e.player.name} joined the game.").color(TextColor.fromHexString("#ffff00")))
        if(Main.getGame().getGameState() == GameState.IDLE) {
            e.player.gameMode = GameMode.ADVENTURE
        }
    }

    @EventHandler
    private fun onQuit(e : PlayerQuitEvent) {
        e.quitMessage(Component.text("${e.player.name} left the game.").color(TextColor.fromHexString("#ffff00")))
    }
}
package dev.byrt.meltdown.event

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.state.Teams
import dev.byrt.meltdown.task.Music

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@Suppress("unused")
class JoinQuitEvent : Listener {
    @EventHandler
    private fun onPlayerJoin(e : PlayerJoinEvent) {
        e.joinMessage(Component.text("${e.player.name} joined the game.").color(TextColor.fromHexString("#ffff00")))
        Main.getGame().infoBoardManager.showScoreboard(e.player)
        e.player.teleport(Location(Bukkit.getWorld("Cheese"), 0.5, -52.0 ,0.5, 0.0f, 0.0f))
        e.player.inventory.clear()
        e.player.sendPlayerListHeaderAndFooter(Main.getGame().tabListManager.getTabHeader(), Main.getGame().tabListManager.getTabFooter())
        Main.getGame().teamManager.addToTeam(e.player, e.player.uniqueId, Teams.SPECTATOR)
        e.player.gameMode = if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            GameMode.ADVENTURE
        } else {
            GameMode.SPECTATOR
        }
    }

    @EventHandler
    private fun onPlayerQuit(e : PlayerQuitEvent) {
        Main.getGame().teamManager.disableTeamGlowing(e.player, Main.getGame().teamManager.getPlayerTeam(e.player.uniqueId))
        Main.getGame().teamManager.getPlayerTeam(e.player.uniqueId).let { Main.getGame().teamManager.removeFromTeam(e.player, e.player.uniqueId, it)}
        Main.getGame().musicTask.stopMusicLoop(e.player, Music.NULL)
        Main.getGame().freezeTask.cancelFreezeLoop(e.player)
        if(Main.getGame().gameManager.getGameState() != GameState.IDLE) {
            Main.getGame().eliminationManager.onPlayerQuit(e.player)
        }
        if(Main.getGame().queue.getQueue().contains(e.player.uniqueId)) {
            Main.getGame().queue.leaveQueue(e.player)
        }
        e.quitMessage(Component.text("${e.player.name} left the game.").color(TextColor.fromHexString("#ffff00")))
    }
}
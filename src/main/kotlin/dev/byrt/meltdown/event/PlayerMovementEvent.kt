package dev.byrt.meltdown.event

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.manager.PlayerLifeState
import dev.byrt.meltdown.state.Sounds

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

@Suppress("unused")
class PlayerMovementEvent : Listener {
    @EventHandler
    private fun onPlayerMove(e : PlayerMoveEvent) {
        if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME || Main.getGame().gameManager.getGameState() == GameState.OVERTIME) {
            if(Main.getGame().eliminationManager.getFrozenPlayers().contains(e.player.uniqueId)) {
                val to = e.from
                to.pitch = e.to.pitch
                to.yaw = e.to.yaw
                e.to = to
            }
            if(e.player.location.block.type == Material.LAVA && e.player.gameMode == GameMode.SURVIVAL && !Main.getGame().teamManager.isSpectator(e.player.uniqueId)) {
                Main.getGame().eliminationManager.changePlayerLifeState(e.player, PlayerLifeState.ELIMINATED)
                e.player.playSound(e.player.location, Sounds.Score.FALL_INTO_LAVA, 1f, 1f)
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(
                        Component.text("[")
                            .append(Component.text("▶").color(NamedTextColor.YELLOW))
                            .append(Component.text("] "))
                            .append(Component.text(e.player.name).color(Main.getGame().teamManager.getPlayerTeam(e.player.uniqueId).textColor))
                            .append(Component.text(" fell into molten lava!", NamedTextColor.WHITE))
                    )
                }
            }
        }
    }
}
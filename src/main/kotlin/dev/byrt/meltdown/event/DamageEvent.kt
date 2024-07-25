package dev.byrt.meltdown.event

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.manager.PlayerLifeState
import dev.byrt.meltdown.state.Sounds
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

@Suppress("unused")
class DamageEvent : Listener {
    @EventHandler
    private fun onDamage(e : EntityDamageEvent) {
        if(e.cause == EntityDamageEvent.DamageCause.HOT_FLOOR
            || e.cause == EntityDamageEvent.DamageCause.DROWNING
            || e.cause == EntityDamageEvent.DamageCause.CRAMMING
            || e.cause == EntityDamageEvent.DamageCause.LAVA
            || e.cause == EntityDamageEvent.DamageCause.FIRE
            || e.cause == EntityDamageEvent.DamageCause.FIRE_TICK
            || e.cause == EntityDamageEvent.DamageCause.CRAMMING
            || e.cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
            || e.cause == EntityDamageEvent.DamageCause.LIGHTNING
            || e.cause == EntityDamageEvent.DamageCause.WITHER
            || e.cause == EntityDamageEvent.DamageCause.VOID
            || e.cause == EntityDamageEvent.DamageCause.DRAGON_BREATH
            || e.cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
            || e.cause == EntityDamageEvent.DamageCause.POISON
            || e.cause == EntityDamageEvent.DamageCause.FREEZE
            || e.cause == EntityDamageEvent.DamageCause.SONIC_BOOM) {
            e.isCancelled = true
        }
        if(e.cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
            if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME || Main.getGame().gameManager.getGameState() == GameState.OVERTIME) {
                if(e.entity is Player) {
                    val playerSquished = e.entity as Player
                    if(playerSquished.location.block.type == Material.NETHERITE_BLOCK && !Main.getGame().teamManager.isSpectator(playerSquished.uniqueId) && !Main.getGame().lifestates.getEliminatedPlayers().contains(playerSquished)) {
                        Main.getGame().lifestates.changePlayerLifeState(playerSquished, PlayerLifeState.ELIMINATED)
                        playerSquished.playSound(playerSquished.location, Sounds.Score.SQUASHED_BY_DOOR, 1f, 0f)
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.sendMessage(
                                Component.text("[")
                                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                                    .append(Component.text("] "))
                                    .append(Component.text(playerSquished.name).color(Main.getGame().teamManager.getPlayerTeam(playerSquished.uniqueId).textColor))
                                    .append(Component.text(" got squashed by a door!", NamedTextColor.WHITE))
                            )
                        }
                    }
                }
            }
            e.isCancelled = true
        }
    }

    @EventHandler
    private fun onEntityDamageEntity(e : EntityDamageByEntityEvent) {
        e.isCancelled = true
    }
}
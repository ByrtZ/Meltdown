package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Sounds
import dev.byrt.meltdown.state.Teams
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player

import java.util.*

class SharedItemManager(private val game : Game) {
    private var telepickaxeOwners = mutableMapOf<Teams, UUID>()
    fun setTeamTelepickaxeOwner(player : Player, team : Teams, isSilent : Boolean) {
        telepickaxeOwners[team] = player.uniqueId
        if(!isSilent) {
            game.teamManager.sendTeamMessage(
                Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text(player.name).color(game.teamManager.getPlayerTeam(player.uniqueId).textColor))
                    .append(Component.text(" has claimed your team's telepickaxe.")
                    ), team
            )
            player.playSound(player.location, Sounds.Telepickaxe.CLAIM, 1f, 1f)
        }

        for(teamPlayer in game.teamManager.getTeamPlayers(team)) {
            teamPlayer.setCooldown(Material.GRAY_DYE, 12 * 20)
            if(!game.eliminationManager.isFrozen(teamPlayer) && teamPlayer.gameMode != GameMode.SPECTATOR) {
                if(teamPlayer.uniqueId != telepickaxeOwners[team]) {
                    teamPlayer.inventory.remove(Material.NETHERITE_PICKAXE)
                    game.itemManager.giveUnusableTelepickaxe(teamPlayer)
                } else {
                    teamPlayer.inventory.remove(Material.GRAY_DYE)
                    game.itemManager.giveUsableTelepickaxe(teamPlayer)
                }
            }
        }
    }

    fun getTeamTelepickaxeOwner(player : Player) : Player {
        return telepickaxeOwners[game.teamManager.getPlayerTeam(player.uniqueId)]?.let { Bukkit.getPlayer(it) }!!
    }

    fun getTelepickaxeOwners() : Map<Teams, UUID> {
        return telepickaxeOwners
    }

    fun clearTelepickaxeOwners() {
        telepickaxeOwners.clear()
    }
}
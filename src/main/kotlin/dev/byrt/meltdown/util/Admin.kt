package dev.byrt.meltdown.util

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Teams

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

import java.util.*

class Admin(private val game : Game) {
    private var adminHudUsers = mutableMapOf<UUID, Boolean>()
    private var adminHudTaskMap = mutableMapOf<UUID, BukkitRunnable>()

    fun showAdminHud(player : Player) {
        addAdminHudUser(player)
        val adminHudRunnable = object : BukkitRunnable() {
            private var adminHudInfo = BossBar.bossBar(
                Component.text("Loading...", NamedTextColor.GRAY),
                0.0f,
                BossBar.Color.WHITE,
                BossBar.Overlay.PROGRESS
            )
            override fun run() {
                if(!adminHudInfo.viewers().contains(player)) adminHudInfo.addViewer(Audience.audience(player))
                if(adminHudUsers[player.uniqueId] == true) {
                    adminHudInfo.name(Component.text("State", NamedTextColor.RED)
                        .append(Component.text(": [${game.gameManager.getGameState()}]", NamedTextColor.WHITE))
                        .append(Component.text(" |||||||| ", NamedTextColor.DARK_GRAY))
                        .append(Component.text("Teams", NamedTextColor.GOLD))
                        .append(Component.text(": [", NamedTextColor.WHITE))
                        .append(Component.text("${game.teamManager.getRedTeam().size}", NamedTextColor.RED))
                        .append(Component.text("{", NamedTextColor.GRAY))
                        .append(Component.text(if(game.teamManager.isTeamActive(Teams.RED)) "a" else "n/a", NamedTextColor.GRAY))
                        .append(Component.text("}, ", NamedTextColor.GRAY))
                        .append(Component.text("${game.teamManager.getYellowTeam().size}", NamedTextColor.YELLOW))
                        .append(Component.text("{", NamedTextColor.GRAY))
                        .append(Component.text(if(game.teamManager.isTeamActive(Teams.YELLOW)) "a" else "n/a", NamedTextColor.GRAY))
                        .append(Component.text("}, ", NamedTextColor.GRAY))
                        .append(Component.text("${game.teamManager.getLimeTeam().size}", NamedTextColor.GREEN))
                        .append(Component.text("{", NamedTextColor.GRAY))
                        .append(Component.text(if(game.teamManager.isTeamActive(Teams.LIME)) "a" else "n/a", NamedTextColor.GRAY))
                        .append(Component.text("}, ", NamedTextColor.GRAY))
                        .append(Component.text("${game.teamManager.getBlueTeam().size}", NamedTextColor.BLUE))
                        .append(Component.text("{", NamedTextColor.GRAY))
                        .append(Component.text(if(game.teamManager.isTeamActive(Teams.BLUE)) "a" else "n/a", NamedTextColor.GRAY))
                        .append(Component.text("}", NamedTextColor.GRAY))
                        .append(Component.text("]", NamedTextColor.WHITE))
                        .append(Component.text(" |||||||| ", NamedTextColor.DARK_GRAY))
                        .append(Component.text("Data", TextColor.fromHexString("#db0060")))
                        .append(Component.text(": ", NamedTextColor.WHITE))
                        .append(Component.text("H", NamedTextColor.GOLD))
                        .append(Component.text(";[${game.heaterManager.getHeaterList().size}], ", NamedTextColor.WHITE))
                        .append(Component.text("F", NamedTextColor.AQUA))
                        .append(Component.text(";[${game.lifestates.getFrozenPlayers().size}], ", NamedTextColor.WHITE))
                        .append(Component.text("E", NamedTextColor.GREEN))
                        .append(Component.text(";[${game.entranceManager.getEntrancesList().size}], ", NamedTextColor.WHITE))
                        .append(Component.text("RM", NamedTextColor.LIGHT_PURPLE))
                        .append(Component.text(";[${game.roomManager.getRoomsList().size}],", NamedTextColor.WHITE))
                        .append(Component.text("DR", NamedTextColor.BLUE))
                        .append(Component.text(";[${game.doorManager.getDoorsList().size}]", NamedTextColor.WHITE))
                    )
                } else {
                    adminHudInfo.removeViewer(Audience.audience(player))
                    hideAdminHud(player)
                }
            }
        }
        adminHudRunnable.runTaskTimer(game.plugin, 0L, 5L)
        adminHudTaskMap[player.uniqueId] = adminHudRunnable
    }

    fun hideAdminHud(player : Player) {
        adminHudTaskMap.remove(player.uniqueId)?.cancel()
    }

    fun addAdminHudUser(player : Player) {
        adminHudUsers[player.uniqueId] = true
    }

    fun removeAdminHudUser(player : Player) {
        adminHudUsers[player.uniqueId] = false
    }

    fun getAdminHudUsers() : Map<UUID, Boolean> {
        return adminHudUsers
    }

    fun reset() {
        for(task in adminHudTaskMap) {
            task.value.cancel()
        }
    }
}
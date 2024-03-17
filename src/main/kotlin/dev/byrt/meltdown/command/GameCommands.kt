package dev.byrt.meltdown.command

import dev.byrt.meltdown.state.Sounds

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import cloud.commandframework.annotations.Confirmation
import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.state.TimerState
import dev.byrt.meltdown.util.DevStatus

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.time.Duration

@Suppress("unused")
class GameCommands : BaseCommand {
    private val startGameFailSound: Sound = Sound.sound(Key.key(Sounds.Start.START_GAME_FAIL), Sound.Source.MASTER, 1f, 0f)
    private val reloadStartSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_START), Sound.Source.MASTER, 1f, 1f)
    private val reloadCompleteSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_COMPLETE), Sound.Source.MASTER, 1f, 2f)

    @CommandMethod("game start")
    @CommandDescription("Starts a game of Meltdown.")
    @CommandPermission("meltdown.startgame")
    @Confirmation
    fun start(sender : Player) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            if(Main.getGame().teamManager.getRedTeam().size >= 1 && Main.getGame().teamManager.getBlueTeam().size >= 1 && Main.getGame().teamManager.getYellowTeam().size >= 1 && Main.getGame().teamManager.getLimeTeam().size >= 1) {
                if(Main.getGame().gameManager.isOvertimeActive()) {
                    Main.getGame().dev.parseDevMessage("Overtime must be disabled in order to start a game.", DevStatus.ERROR)
                } else {
                    Main.getGame().dev.parseDevMessage("${sender.name} started a Meltdown game!", DevStatus.INFO_SUCCESS)
                    for(player in Bukkit.getOnlinePlayers()) {
                        player.sendMessage(Component.text("\nA Meltdown game is starting!\n").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                    }
                    Main.getGame().startGame()
                }
            } else {
                sender.sendMessage(Component.text("There are not enough players on teams to start a Meltdown game.").color(NamedTextColor.RED))
                sender.playSound(startGameFailSound)
            }
        } else if(Main.getGame().gameManager.getGameState() == GameState.GAME_END) {
            sender.sendMessage(Component.text("A restart is required for a new Meltdown game.").color(NamedTextColor.RED))
            sender.playSound(startGameFailSound)
        } else {
            sender.sendMessage(Component.text("There is already a Meltdown game running, you numpty!").color(NamedTextColor.RED))
            sender.playSound(startGameFailSound)
        }
    }

    @CommandMethod("game force start")
    @CommandDescription("Force starts the game, may have unintended consequences.")
    @CommandPermission("meltdown.force.start")
    @Confirmation
    fun forceStartGame(sender : Player) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            if(Main.getGame().gameManager.isOvertimeActive()) {
                Main.getGame().dev.parseDevMessage("Overtime must be disabled in order to start a game.", DevStatus.ERROR)
            } else {
                Main.getGame().dev.parseDevMessage("${sender.name} force started a Meltdown game!", DevStatus.INFO_SUCCESS)
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("\nA Meltdown game is starting!\n").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                }
                Main.getGame().startGame()
            }
        } else {
            sender.sendMessage(Component.text("Game already running or restart required.").color(NamedTextColor.RED))
            sender.playSound(startGameFailSound)
        }
    }

    @CommandMethod("game force stop")
    @CommandDescription("Force stops the game, may have unintended consequences.")
    @CommandPermission("meltdown.force.stop")
    @Confirmation
    fun forceStopGame(sender : Player) {
        if(Main.getGame().gameManager.getGameState() != GameState.IDLE) {
            if(Main.getGame().gameManager.getGameState() != GameState.GAME_END) {
                Main.getGame().dev.parseDevMessage("${sender.name} force stopped the current Meltdown game.", DevStatus.WARNING)
                Main.getGame().stopGame()
            } else {
                sender.sendMessage(Component.text("Game currently ending.").color(NamedTextColor.RED))
            }
        } else {
            sender.sendMessage(Component.text("Game not running or restart required.").color(NamedTextColor.RED))
            sender.playSound(startGameFailSound)
        }
    }

    @CommandMethod("game reload")
    @CommandDescription("Allows the executing player to reset the game.")
    @CommandPermission("meltdown.reloadgame")
    @Confirmation
    fun reloadGame(sender : Player) {
        if(Main.getGame().gameManager.getGameState() == GameState.GAME_END && Main.getGame().timerManager.getTimerState() == TimerState.INACTIVE) {
            sender.showTitle(Title.title(Component.text(""), Component.text("Reloading...", NamedTextColor.RED), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))))
            sender.playSound(reloadStartSound)
            Main.getGame().reload()
            sender.showTitle(Title.title(Component.text(""), Component.text("Game reloaded!", NamedTextColor.GREEN), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
            sender.playSound(reloadCompleteSound)
            Main.getGame().dev.parseDevMessage("${sender.name} reloaded the game.", DevStatus.INFO)
        } else {
            sender.sendMessage(Component.text("Unable to reload game.", NamedTextColor.RED))
        }
    }

    @CommandMethod("game toggle overtime")
    @CommandDescription("Toggles whether overtime should occur or not.")
    @CommandPermission("meltdown.overtime")
    fun overtime(sender : Player) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            if(Main.getGame().gameManager.isOvertimeActive()) {
                Main.getGame().gameManager.setOvertimeState(false)
                Main.getGame().dev.parseDevMessage("Overtime disabled for next game by ${sender.name}.", DevStatus.INFO_FAIL)
                sender.playSound(reloadStartSound)
            } else {
                Main.getGame().gameManager.setOvertimeState(true)
                Main.getGame().dev.parseDevMessage("Overtime enabled for next game by ${sender.name}.", DevStatus.INFO_SUCCESS)
                Main.getGame().dev.parseDevMessage("WARNING! Overtime is unconfigured for this game mode and therefore must be disabled in order to start a game.", DevStatus.SEVERE)
                sender.playSound(reloadCompleteSound)
            }
        } else {
            sender.sendMessage(Component.text("Unable to change overtime toggle in this state.").color(NamedTextColor.RED))
            sender.playSound(startGameFailSound)
        }
    }
}
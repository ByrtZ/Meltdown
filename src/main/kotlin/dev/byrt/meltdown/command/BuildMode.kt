package dev.byrt.meltdown.command

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.state.Sounds
import dev.byrt.meltdown.util.DevStatus

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.entity.Player

@Suppress("unused")
class BuildMode : BaseCommand {
    private val buildToggleSuccessSound: Sound = Sound.sound(Key.key(Sounds.Command.BUILDMODE_SUCCESS), Sound.Source.MASTER, 1f, 1f)
    private val buildToggleFailSound: Sound = Sound.sound(Key.key(Sounds.Command.BUILDMODE_FAIL), Sound.Source.MASTER, 1f, 0f)
    @CommandMethod("buildmode")
    @CommandDescription("Toggles the ability to build for operators while not in game.")
    @CommandPermission("meltdown.buildmode")
    fun start(sender : Player) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            if(Main.getGame().getBuildMode()) {
                for(player in Bukkit.getOnlinePlayers()) {
                    if(player.isOp) {
                        player.playSound(buildToggleSuccessSound)
                    }
                }
                Main.getGame().dev.parseDevMessage("Building disabled by ${sender.name}.", DevStatus.INFO_FAIL)
                Main.getGame().setBuildMode(false)
            } else {
                for(player in Bukkit.getOnlinePlayers()) {
                    if(player.isOp) {
                        player.playSound(buildToggleSuccessSound)
                    }
                }
                Main.getGame().dev.parseDevMessage("Building enabled by ${sender.name}.", DevStatus.INFO_SUCCESS)
                Main.getGame().setBuildMode(true)
            }
        } else {
            sender.sendMessage(Component.text("You can only toggle Build Mode when the game is idle.").color(NamedTextColor.RED))
            sender.playSound(buildToggleFailSound)
        }
    }
}
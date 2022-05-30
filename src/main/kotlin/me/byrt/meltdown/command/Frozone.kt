package me.byrt.meltdown.command

import me.byrt.meltdown.Main

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player

@Suppress("unused")
class Frozone : BaseCommand {
    @CommandMethod("freeze <player>")
    @CommandDescription("Force freezes a player")
    @CommandPermission("meltdown.freeze")
    fun freeze(sender : Player, @Argument("player") player : Player) {
        sender.sendMessage(Component.text("Force froze ${player.name}.").color(NamedTextColor.GREEN))
        Main.getGame().getFreezeManager().freezePlayer(player)
    }

    @CommandMethod("unfreeze <player>")
    @CommandDescription("Force unfreezes a player")
    @CommandPermission("meltdown.unfreeze")
    fun unfreeze(sender : Player, @Argument("player") player : Player) {
        sender.sendMessage(Component.text("Force unfroze ${player.name}.").color(NamedTextColor.GREEN))
        Main.getGame().getFreezeManager().unfreezePlayer(player)
    }
}
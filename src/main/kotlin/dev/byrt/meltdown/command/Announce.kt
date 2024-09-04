package dev.byrt.meltdown.command

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.util.DevStatus
import dev.byrt.meltdown.state.Sounds
import io.papermc.paper.command.brigadier.CommandSourceStack

import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.Argument

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage

import org.bukkit.Bukkit

import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Announce {
    private val mm = MiniMessage.miniMessage()
    @Command("announce <text>")
    @CommandDescription("Puts a formatted announcement message in chat.")
    @Permission("meltdown.announce")
    fun announce(css: CommandSourceStack, @Argument("text") text: Array<String>) {
        Main.getGame().dev.parseDevMessage("Announcement sent by ${css.sender.name}.", DevStatus.INFO)
        val rawAnnounceMessage = text.joinToString(" ")
        for(player in Bukkit.getServer().onlinePlayers) {
            player.playSound(player.location, Sounds.Alert.GENERAL_UPDATE, 1.0f, 1.0f)
            player.sendMessage(Component.text("\n---------------------------------------------------\n\n", NamedTextColor.GREEN, TextDecoration.STRIKETHROUGH))
            player.sendMessage(
                Component.text("[", NamedTextColor.WHITE).decoration(TextDecoration.STRIKETHROUGH, false)
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] ", NamedTextColor.WHITE))
                    .append(mm.deserialize(rawAnnounceMessage))
            )
            player.sendMessage(Component.text("\n\n---------------------------------------------------\n", NamedTextColor.GREEN, TextDecoration.STRIKETHROUGH))
        }
    }
}
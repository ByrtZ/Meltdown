package dev.byrt.meltdown.util

import dev.byrt.meltdown.game.Game

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

import org.bukkit.Bukkit

import java.util.*

class Dev(private val game : Game) {
    fun parseDevMessage(message : String, status : DevStatus) {
        val parsed = Component.text("\uD001 ", NamedTextColor.WHITE)
            .append(Component.text(message, status.colour))
        sendAdminMessage(parsed)
    }

    fun parseTempWhitelistPrompt(name : String) {
        val tempWhitelistPrompt = Component.text("\uD001 ", NamedTextColor.WHITE).append(Component.text("Want to add $name to temporary whitelist? ", NamedTextColor.AQUA).append(Component.text("[Click here].", TextColor.fromHexString("#ffff00")).clickEvent(
            ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/whitelist tempadd $name"))))
        sendAdminMessage(tempWhitelistPrompt)
    }

    fun parseJoinClientBrandMessage(playerName : String, brand : String, status : DevStatus) {
        val brandCapitalised = brand.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val parsed = Component.text("\uD001 ", NamedTextColor.WHITE)
            .append(Component.text("$playerName ", status.colour)
                .append(Component.text("joined on ", NamedTextColor.DARK_GRAY))
                .append(Component.text(brandCapitalised, status.colour))
                .append(Component.text(".", NamedTextColor.DARK_GRAY)))
        sendAdminMessage(parsed)
    }

    private fun sendAdminMessage(message : Component) {
        for(admin in Bukkit.getOnlinePlayers().filter { player -> player.isOp }) {
            admin.sendMessage(message)
        }
    }
}

@Suppress("unused")
enum class DevStatus(val colour : TextColor) {
    CLIENT_BRAND_INFO(TextColor.fromHexString("#db0060")!!),
    INFO(NamedTextColor.DARK_GRAY),
    INFO_SUCCESS(NamedTextColor.GREEN),
    INFO_FAIL(NamedTextColor.RED),
    WARNING(NamedTextColor.GOLD),
    SEVERE(NamedTextColor.RED),
    ERROR(NamedTextColor.DARK_RED)
}
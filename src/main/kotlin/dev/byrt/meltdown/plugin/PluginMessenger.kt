package dev.byrt.meltdown.plugin

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.util.DevStatus

import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

import java.io.UnsupportedEncodingException

import java.nio.charset.Charset

class PluginMessenger : PluginMessageListener {
    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        try {
            Main.getGame().dev.parseJoinClientBrandMessage(player.name, String(message, Charset.defaultCharset()).substring(1), DevStatus.CLIENT_BRAND_INFO)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

}
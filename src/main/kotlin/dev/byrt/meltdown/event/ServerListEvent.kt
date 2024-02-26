package dev.byrt.meltdown.event

import com.destroystokyo.paper.event.server.PaperServerListPingEvent

import net.kyori.adventure.text.Component

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Suppress("unused")
class ServerListEvent : Listener {
    @EventHandler
    private fun onServerListPing(e : PaperServerListPingEvent) {
        e.setHidePlayers(false)
        e.version = "Byrtrium v1"
        e.motd(Component.text("§4■§c■§4■§c■§4■§c■§4■§c■§4■§c■§4■§c■§4■§6§l Byrt's Server§r§4 ■§c■§4■§c■§4■§c■§4■§c■§4■§c■§4■§c■§4■§r\n§8v1.0.0§f ● §b§lMeltdown§f ●§e It's gaming time."))
    }
}
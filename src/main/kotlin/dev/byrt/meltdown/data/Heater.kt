package dev.byrt.meltdown.data

import dev.byrt.meltdown.state.Teams
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Location

import java.util.*

data class Heater(val id : Int, val owner : UUID, val location : Location, val team : Teams) {
    companion object {
        const val HEATER_RADIUS = 3.5
    }
}

enum class HeaterBreakReason(val reasonName : String, val reason : Component) {
    SELF("Self", Component.text("[").append(Component.text("▶").color(NamedTextColor.YELLOW)).append(Component.text("] ").append(Component.text("You retrieved your heater.", NamedTextColor.WHITE)))),
    NOT_IN_GAME("Not in game", Component.text("[").append(Component.text("▶").color(NamedTextColor.YELLOW)).append(Component.text("] ").append(Component.text("Your heater returned itself to you using dark magic.", NamedTextColor.LIGHT_PURPLE)))),
    EXPIRED("Expired", Component.text("[").append(Component.text("▶").color(NamedTextColor.YELLOW)).append(Component.text("] ").append(Component.text("Your heater has expired.", NamedTextColor.RED)))),
    ENEMY("Enemy", Component.text("[").append(Component.text("▶").color(NamedTextColor.YELLOW)).append(Component.text("] ").append(Component.text("Your heater was broken by an enemy.", NamedTextColor.RED)))),
    DEATH("Death", Component.text("[").append(Component.text("▶").color(NamedTextColor.YELLOW)).append(Component.text("] ").append(Component.text("Your heater was sad that you died, so it did too.", NamedTextColor.RED))))
}
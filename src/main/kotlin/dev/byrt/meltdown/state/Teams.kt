package dev.byrt.meltdown.state

import net.kyori.adventure.text.format.NamedTextColor

enum class Teams(val teamName : String, val teamGlyph : String, val textColor : NamedTextColor) {
    RED("Red Team", "\uD004", NamedTextColor.RED),
    YELLOW("Yellow Team", "\uD007", NamedTextColor.YELLOW),
    LIME("Lime Team", "\uD008", NamedTextColor.GREEN),
    BLUE("Blue Team", "\uD005", NamedTextColor.BLUE),
    SPECTATOR("Spectators", "\uD003", NamedTextColor.GRAY)
}
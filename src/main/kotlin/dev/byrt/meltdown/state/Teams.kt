package dev.byrt.meltdown.state

import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Color

enum class Teams(val teamName : String, val teamGlyph : String, val textColor : NamedTextColor, val color : Color, var score : Int) {
    RED("Red Team","\uD004", NamedTextColor.RED, Color.RED,0),
    YELLOW("Yellow Team","\uD007", NamedTextColor.YELLOW, Color.YELLOW,0),
    LIME("Lime Team","\uD008", NamedTextColor.GREEN, Color.LIME,0),
    BLUE("Blue Team","\uD005", NamedTextColor.BLUE, Color.BLUE,0),
    SPECTATOR("Spectators","\uD003", NamedTextColor.GRAY, Color.GRAY,0)
}
package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import kotlin.collections.ArrayList
import kotlin.random.Random

class TabListManager(private var game : Game) {
    private var meltdownPuns: ArrayList<String> = arrayListOf()
    private var header: Component = Component.text("")
    private var footer: Component = Component.text("")

    private fun assignRandomPun() {
        val randomIndex = Random.nextInt(meltdownPuns.size)
        val randomPun = meltdownPuns[randomIndex]
        header = Component.text("Meltdown").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true)
            .append(Component.text("\n               An MCC original masterpiece, brought to you by ").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false).append(Component.text("Byrt").color(NamedTextColor.RED))
                .append(Component.text(".               \n").color(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false)))
        footer = Component.text(randomPun).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, true)
    }

    fun populateMeltdownPuns() {
        meltdownPuns.add("\n'Honestly I don't know what to put here yet'\n")
        assignRandomPun()
    }

    fun getTabHeader(): Component {
        return header
    }

    fun getTabFooter(): Component {
        return footer
    }
}
package dev.byrt.meltdown.manager

import dev.byrt.meltdown.state.Teams
import dev.byrt.meltdown.game.Game

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta

class ItemManager(private val game : Game) {
    fun givePlayerTeamBoots(player : Player, team : Teams) {
        when(team) {
            Teams.RED -> {
                val teamABoots = ItemStack(Material.LEATHER_BOOTS)
                val teamABootsMeta: ItemMeta = teamABoots.itemMeta
                teamABootsMeta.displayName(Component.text("Red Team Boots").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                teamABootsMeta.isUnbreakable = true
                teamABootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                teamABoots.itemMeta = teamABootsMeta
                val lm: LeatherArmorMeta = teamABoots.itemMeta as LeatherArmorMeta
                lm.setColor(Color.RED)
                teamABoots.itemMeta = lm
                player.inventory.boots = teamABoots
            }
            Teams.YELLOW -> {
                val teamCBoots = ItemStack(Material.LEATHER_BOOTS)
                val teamCBootsMeta: ItemMeta = teamCBoots.itemMeta
                teamCBootsMeta.displayName(Component.text("Yellow Team Boots").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
                teamCBootsMeta.isUnbreakable = true
                teamCBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                teamCBoots.itemMeta = teamCBootsMeta
                val lm: LeatherArmorMeta = teamCBoots.itemMeta as LeatherArmorMeta
                lm.setColor(Color.YELLOW)
                teamCBoots.itemMeta = lm
                player.inventory.boots = teamCBoots
            }
            Teams.LIME -> {
                val teamDBoots = ItemStack(Material.LEATHER_BOOTS)
                val teamDBootsMeta: ItemMeta = teamDBoots.itemMeta
                teamDBootsMeta.displayName(Component.text("Lime Team Boots").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                teamDBootsMeta.isUnbreakable = true
                teamDBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                teamDBoots.itemMeta = teamDBootsMeta
                val lm: LeatherArmorMeta = teamDBoots.itemMeta as LeatherArmorMeta
                lm.setColor(Color.LIME)
                teamDBoots.itemMeta = lm
                player.inventory.boots = teamDBoots
            }
            Teams.BLUE -> {
                val teamBBoots = ItemStack(Material.LEATHER_BOOTS)
                val teamBBootsMeta: ItemMeta = teamBBoots.itemMeta
                teamBBootsMeta.displayName(Component.text("Blue Team Boots").color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false))
                teamBBootsMeta.isUnbreakable = true
                teamBBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                teamBBoots.itemMeta = teamBBootsMeta
                val lm: LeatherArmorMeta = teamBBoots.itemMeta as LeatherArmorMeta
                lm.setColor(Color.BLUE)
                teamBBoots.itemMeta = lm
                player.inventory.boots = teamBBoots
            }
            Teams.SPECTATOR -> {
                val spectatorBoots = ItemStack(Material.LEATHER_BOOTS)
                val spectatorBootsMeta: ItemMeta = spectatorBoots.itemMeta
                spectatorBootsMeta.displayName(Component.text("Spectator Boots").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false))
                spectatorBootsMeta.isUnbreakable = true
                spectatorBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                spectatorBoots.itemMeta = spectatorBootsMeta
                val lm: LeatherArmorMeta = spectatorBoots.itemMeta as LeatherArmorMeta
                if(player.isOp) {
                    lm.setColor(Color.fromRGB(170, 0, 0))
                } else {
                    lm.setColor(Color.GRAY)
                }
                spectatorBoots.itemMeta = lm
                player.inventory.boots = spectatorBoots
            }
        }
    }

    fun givePlayerKit(player : Player) {
        giveFrostBowItem(player)
        giveHeaterItem(player)
        giveFrostArrowItem(player)
    }

    fun giveFrostBowItem(player : Player) {
        val frostBow = ItemStack(Material.BOW, 1)
        val frostBowMeta: ItemMeta = frostBow.itemMeta
        frostBowMeta.displayName(Component.text("Frost Bow").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
        frostBowMeta.isUnbreakable = true
        frostBowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true)
        frostBowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        frostBow.itemMeta = frostBowMeta
        player.inventory.addItem(frostBow)
    }

    fun giveHeaterItem(player : Player) {
        val heater = ItemStack(Material.NETHERITE_SHOVEL, 1)
        val heaterMeta: ItemMeta = heater.itemMeta
        heaterMeta.displayName(Component.text("Heater").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
        heaterMeta.isUnbreakable = true
        heaterMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        heater.itemMeta = heaterMeta
        player.inventory.addItem(heater)
    }

    fun giveFrostArrowItem(player : Player) {
        val frostArrow = ItemStack(Material.ARROW, 1)
        val frostArrowMeta: ItemMeta = frostArrow.itemMeta
        frostArrowMeta.displayName(Component.text("Frost Tipped Arrow").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
        frostArrowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
        frostArrow.itemMeta = frostArrowMeta
        player.inventory.setItem(35, frostArrow)
    }
}
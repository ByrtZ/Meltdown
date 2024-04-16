package dev.byrt.meltdown.manager

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.MapCanvas
import org.bukkit.map.MapCursor
import org.bukkit.map.MapRenderer
import org.bukkit.map.MapView

import java.util.*

import kotlin.random.Random

class ItemManager(private val game : Game) {
    fun givePlayerTeamBoots(player : Player, team : Teams) {
        when(team) {
            Teams.RED -> {
                val teamABoots = ItemStack(Material.LEATHER_BOOTS)
                val teamABootsMeta = teamABoots.itemMeta
                teamABootsMeta.displayName(Component.text("Red Team Boots").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                val teamABootsRarity = ItemRarity.COMMON
                val teamABootsType = ItemType.ARMOUR
                val teamABootsLore = listOf(
                    Component.text("${teamABootsRarity.rarityGlyph}${teamABootsType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                    Component.text("A snazzy pair of Red Team's boots.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                )
                teamABootsMeta.lore(teamABootsLore)
                teamABootsMeta.isUnbreakable = true
                teamABootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                teamABoots.itemMeta = teamABootsMeta
                val lm = teamABoots.itemMeta as LeatherArmorMeta
                lm.setColor(Color.RED)
                teamABoots.itemMeta = lm
                player.inventory.boots = teamABoots
            }
            Teams.YELLOW -> {
                val teamCBoots = ItemStack(Material.LEATHER_BOOTS)
                val teamCBootsMeta = teamCBoots.itemMeta
                teamCBootsMeta.displayName(Component.text("Yellow Team Boots").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
                val teamCBootsRarity = ItemRarity.COMMON
                val teamCBootsType = ItemType.ARMOUR
                val teamCBootsLore = listOf(
                    Component.text("${teamCBootsRarity.rarityGlyph}${teamCBootsType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                    Component.text("A snazzy pair of Red Team's boots.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                )
                teamCBootsMeta.lore(teamCBootsLore)
                teamCBootsMeta.isUnbreakable = true
                teamCBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                teamCBoots.itemMeta = teamCBootsMeta
                val lm = teamCBoots.itemMeta as LeatherArmorMeta
                lm.setColor(Color.YELLOW)
                teamCBoots.itemMeta = lm
                player.inventory.boots = teamCBoots
            }
            Teams.LIME -> {
                val teamDBoots = ItemStack(Material.LEATHER_BOOTS)
                val teamDBootsMeta = teamDBoots.itemMeta
                teamDBootsMeta.displayName(Component.text("Lime Team Boots").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                val teamDBootsRarity = ItemRarity.COMMON
                val teamDBootsType = ItemType.ARMOUR
                val teamDBootsLore = listOf(
                    Component.text("${teamDBootsRarity.rarityGlyph}${teamDBootsType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                    Component.text("A snazzy pair of Lime Team's boots.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                )
                teamDBootsMeta.lore(teamDBootsLore)
                teamDBootsMeta.isUnbreakable = true
                teamDBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                teamDBoots.itemMeta = teamDBootsMeta
                val lm = teamDBoots.itemMeta as LeatherArmorMeta
                lm.setColor(Color.LIME)
                teamDBoots.itemMeta = lm
                player.inventory.boots = teamDBoots
            }
            Teams.BLUE -> {
                val teamBBoots = ItemStack(Material.LEATHER_BOOTS)
                val teamBBootsMeta = teamBBoots.itemMeta
                teamBBootsMeta.displayName(Component.text("Blue Team Boots").color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false))
                val teamBBootsRarity = ItemRarity.COMMON
                val teamBBootsType = ItemType.ARMOUR
                val teamBBootsLore = listOf(
                    Component.text("${teamBBootsRarity.rarityGlyph}${teamBBootsType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                    Component.text("A snazzy pair of Blue Team's boots.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                )
                teamBBootsMeta.lore(teamBBootsLore)
                teamBBootsMeta.isUnbreakable = true
                teamBBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                teamBBoots.itemMeta = teamBBootsMeta
                val lm = teamBBoots.itemMeta as LeatherArmorMeta
                lm.setColor(Color.BLUE)
                teamBBoots.itemMeta = lm
                player.inventory.boots = teamBBoots
            }
            Teams.SPECTATOR -> {
                val spectatorBoots = ItemStack(Material.LEATHER_BOOTS)
                val spectatorBootsMeta = spectatorBoots.itemMeta
                spectatorBootsMeta.isUnbreakable = true
                spectatorBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                if(player.isOp) {
                    val spectatorBootsRarity = ItemRarity.SPECIAL
                    val spectatorBootsType = ItemType.ARMOUR
                    spectatorBootsMeta.displayName(Component.text("Admin Boots").color(TextColor.fromHexString(spectatorBootsRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
                    val spectatorBootsLore = listOf(
                        Component.text("${spectatorBootsRarity.rarityGlyph}${spectatorBootsType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                        Component.text("A very special pair of Admin boots.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                    )
                    spectatorBootsMeta.lore(spectatorBootsLore)
                    spectatorBoots.itemMeta = spectatorBootsMeta
                    val lm = spectatorBoots.itemMeta as LeatherArmorMeta
                    lm.setColor(Color.fromRGB(170, 0, 0))
                    spectatorBoots.itemMeta = lm
                } else {
                    val spectatorBootsRarity = ItemRarity.COMMON
                    val spectatorBootsType = ItemType.ARMOUR
                    spectatorBootsMeta.displayName(Component.text("Spectator Boots").color(TextColor.fromHexString(spectatorBootsRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
                    val spectatorBootsLore = listOf(
                        Component.text("${spectatorBootsRarity.rarityGlyph}${spectatorBootsType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                        Component.text("A boring old pair of Spectator boots.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                    )
                    spectatorBootsMeta.lore(spectatorBootsLore)
                    spectatorBoots.itemMeta = spectatorBootsMeta
                    val lm = spectatorBoots.itemMeta as LeatherArmorMeta
                    lm.setColor(Color.GRAY)
                    spectatorBoots.itemMeta = lm
                }
                player.inventory.boots = spectatorBoots
            }
        }
    }

    fun givePlayerKit(player : Player) {
        giveFrostBowItem(player)
        giveHeaterItem(player)
        giveFrostArrowItem(player)
        giveMapItem(player)
    }

    fun giveFrostBowItem(player : Player) {
        val frostBow = ItemStack(Material.BOW, 1)
        val frostBowMeta: ItemMeta = frostBow.itemMeta
        frostBowMeta.displayName(Component.text("Frost Bow").color(TextColor.fromHexString(ItemRarity.RARE.rarityColour)).decoration(TextDecoration.ITALIC, false))
        frostBowMeta.lore(listOf(
            Component.text("${ItemRarity.RARE.rarityGlyph}${ItemType.WEAPON.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A carefully engineered ranged", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("weapon that can utilise", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("Frost Tipped Arrows.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        ))
        frostBowMeta.isUnbreakable = true
        frostBowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true)
        frostBowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        frostBow.itemMeta = frostBowMeta
        player.inventory.addItem(frostBow)
    }

    fun giveFrostArrowItem(player : Player) {
        val frostArrow = ItemStack(Material.ARROW, 1)
        val frostArrowMeta: ItemMeta = frostArrow.itemMeta
        frostArrowMeta.displayName(Component.text("Frost Tipped Arrow").color(TextColor.fromHexString(ItemRarity.RARE.rarityColour)).decoration(TextDecoration.ITALIC, false))
        frostArrowMeta.lore(listOf(
            Component.text("${ItemRarity.RARE.rarityGlyph}${ItemType.CONSUMABLE.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A special, deadly frost arrow", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("that instantly puts enemies into", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("a frozen state.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        ))
        frostArrowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
        frostArrow.itemMeta = frostArrowMeta
        player.inventory.setItem(35, frostArrow)
    }

    fun giveHeaterItem(player : Player) {
        val heater = ItemStack(Material.NETHERITE_SHOVEL, 1)
        val heaterMeta: ItemMeta = heater.itemMeta
        heaterMeta.displayName(Component.text("Heater").color(TextColor.fromHexString(ItemRarity.LEGENDARY.rarityColour)).decoration(TextDecoration.ITALIC, false))
        heaterMeta.lore(listOf(
            Component.text("${ItemRarity.LEGENDARY.rarityGlyph}${ItemType.TOOL.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A device that can thaw", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("frozen teammates.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        ))
        heaterMeta.isUnbreakable = true
        heaterMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        heater.itemMeta = heaterMeta
        player.inventory.addItem(heater)
    }

    fun giveUsableTelepickaxe(player : Player) {
        val telepickaxe = ItemStack(Material.NETHERITE_PICKAXE, 1)
        val telepickaxeMeta = telepickaxe.itemMeta
        telepickaxeMeta.displayName(Component.text("Telepickaxe", TextColor.fromHexString(ItemRarity.MYTHIC.rarityColour)).decoration(TextDecoration.ITALIC , false))
        telepickaxeMeta.lore(listOf(
            Component.text("${ItemRarity.MYTHIC.rarityGlyph}${ItemType.TOOL.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("An ancient pickaxe that can be", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("called between a group of people,", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("who have a strong bond.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        ))
        telepickaxeMeta.isUnbreakable = true
        telepickaxeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        telepickaxe.itemMeta = telepickaxeMeta
        player.inventory.addItem(telepickaxe)
    }

    fun giveUnusableTelepickaxe(player : Player) {
        val telepickaxe = ItemStack(Material.GRAY_DYE, 1)
        val telepickaxeMeta = telepickaxe.itemMeta
        telepickaxeMeta.displayName(Component.text("Telepickaxe", TextColor.fromHexString(ItemRarity.COMMON.rarityColour)).decoration(TextDecoration.ITALIC , false))
        telepickaxeMeta.lore(listOf(
            Component.text("${ItemRarity.COMMON.rarityGlyph}${ItemType.CONSUMABLE.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("Telepickaxe? What Telepickaxe?", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        ))
        telepickaxeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        telepickaxe.itemMeta = telepickaxeMeta
        player.inventory.addItem(telepickaxe)
    }

    fun distributeTelepickaxe() {
        if(game.teamManager.getRedTeam().isNotEmpty()) {
            game.sharedItemManager.setTeamTelepickaxeOwner(game.teamManager.getTeamPlayers(Teams.RED)[Random.nextInt(game.teamManager.getRedTeam().size)], Teams.RED, true)
        }
        if(game.teamManager.getYellowTeam().isNotEmpty()) {
            game.sharedItemManager.setTeamTelepickaxeOwner(game.teamManager.getTeamPlayers(Teams.YELLOW)[Random.nextInt(game.teamManager.getYellowTeam().size)], Teams.YELLOW, true)
        }
        if(game.teamManager.getLimeTeam().isNotEmpty()) {
            game.sharedItemManager.setTeamTelepickaxeOwner(game.teamManager.getTeamPlayers(Teams.LIME)[Random.nextInt(game.teamManager.getLimeTeam().size)], Teams.LIME, true)
        }
        if(game.teamManager.getBlueTeam().isNotEmpty()) {
            game.sharedItemManager.setTeamTelepickaxeOwner(game.teamManager.getTeamPlayers(Teams.BLUE)[Random.nextInt(game.teamManager.getBlueTeam().size)], Teams.BLUE, true)
        }
    }

    fun giveMapItem(player : Player) {
        val arenaMap = ItemStack(Material.FILLED_MAP, 1)
        val arenaMapMeta = arenaMap.itemMeta as MapMeta
        arenaMapMeta.displayName(Component.text("Laboratory Map", TextColor.fromHexString(ItemRarity.COMMON.rarityColour)).decoration(TextDecoration.ITALIC , false))
        arenaMapMeta.lore(listOf(
            Component.text("${ItemRarity.COMMON.rarityGlyph}${ItemType.UTILITY.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A simple blueprint of the", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("laboratory you have mysteriously", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("ended up in.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        ))
        arenaMapMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS)
        val arenaMapView = game.plugin.server.createMap(player.world)
        arenaMapView.scale = MapView.Scale.CLOSE
        arenaMapView.centerX = -2000
        arenaMapView.centerZ = -2000
        arenaMapView.isUnlimitedTracking = false
        arenaMapView.isTrackingPosition = false
        arenaMapView.renderers.clear()
        //TODO: PRE-RENDER MAP CHUNKS
        arenaMapView.addRenderer(object : MapRenderer() {
            override fun render(map : MapView, canvas : MapCanvas, player : Player) {
                while(canvas.cursors.size() > 0) {
                    canvas.cursors.removeCursor(canvas.cursors.getCursor(0))
                }
                val x = (player.location.blockX - map.centerX).toByte()
                val z = (player.location.blockZ - map.centerZ).toByte()
                canvas.cursors.addCursor(MapCursor(
                        x,
                        z,
                        direction(player),
                        MapCursor.Type.WHITE_POINTER, true)
                    )
                }
            }
        )
        arenaMapMeta.mapView = arenaMapView
        arenaMap.itemMeta = arenaMapMeta
        player.inventory.addItem(arenaMap)
    }

    fun direction(player : Player) : Byte {
        var normalizedYaw = player.location.yaw % 360
        if(normalizedYaw < 0) {
            normalizedYaw += 360
        }
        val byteValue = ((normalizedYaw + 11.25) % 360 / 22.5).toInt() and 0xF
        return byteValue.toByte()
    }

    fun getQueueItem() : ItemStack {
        val queueLeaveItem = ItemStack(Material.RED_DYE, 1)
        val queueLeaveItemMeta: ItemMeta = queueLeaveItem.itemMeta
        queueLeaveItemMeta.displayName(Component.text("Leave Queue").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
        queueLeaveItemMeta.lore(Collections.singletonList(Component.text("Right-Click to leave the queue.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)) as MutableList<out Component>)
        queueLeaveItem.itemMeta = queueLeaveItemMeta
        return queueLeaveItem
    }
}

enum class ItemRarity(val rarityName : String, val rarityColour : String, val rarityGlyph : String) {
    COMMON("Common", "#ffffff", "\uF001"),
    UNCOMMON("Uncommon", "#0ed145", "\uF002"),
    RARE("Rare", "#00a8f3", "\uF003"),
    EPIC("Epic", "#b83dba", "\uF004"),
    LEGENDARY("Legendary", "#ff7f27", "\uF005"),
    MYTHIC("Mythic", "#ff3374", "\uF006"),
    SPECIAL("Special", "#ec1c24", "\uF007"),
    UNREAL("Unreal", "#8666e6", "\uF008")
}

enum class ItemType(val typeName : String, val typeGlyph : String) {
    ARMOUR("Armour", "\uF009"),
    CONSUMABLE("Consumable", "\uF010"),
    TOOL("Tool", "\uF011"),
    UTILITY("Utility", "\uF012"),
    WEAPON("Weapon", "\uF013")
}
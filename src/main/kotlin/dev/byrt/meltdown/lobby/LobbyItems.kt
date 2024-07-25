package dev.byrt.meltdown.lobby

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.manager.ItemRarity
import dev.byrt.meltdown.manager.ItemType

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Cow
import org.bukkit.entity.Creeper
import org.bukkit.entity.Fireball
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.BlockIterator

import kotlin.math.cos
import kotlin.math.sin

class LobbyItems(private val game : Game) {
    fun useRocketLauncher(player : Player) {
        val fireball = player.launchProjectile(Fireball::class.java, player.location.direction.multiply(8))
        fireball.setIsIncendiary(false)
        fireball.yield = 0f
        fireball.customName(Component.text("rocket"))
        fireball.velocity = player.location.direction

        dev.byrt.meltdown.Main.getPlugin().let {
            object : BukkitRunnable() {
                override fun run() {
                    if(fireball.isDead) {
                        val fireballDeathLoc = fireball.location.clone()
                        fireballDeathLoc.y += 1.75
                        fireball.remove()
                        val creeper = fireball.world.spawn(fireballDeathLoc, Creeper::class.java)
                        creeper.isInvisible = true
                        creeper.isInvulnerable = true
                        creeper.explode()
                        cancel()
                    }
                }
            }.runTaskTimer(it, 0L, 1L)
        }
    }

    fun createRocketLauncher(player : Player) {
        val rocketLauncher = ItemStack(Material.GOLDEN_HORSE_ARMOR)
        val rocketLauncherMeta = rocketLauncher.itemMeta
        val rocketLauncherRarity = ItemRarity.SPECIAL
        val rocketLauncherType = ItemType.UTILITY
        rocketLauncherMeta.displayName(Component.text("Rocket Launcher").color(TextColor.fromHexString(rocketLauncherRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
        val rocketLauncherLore = listOf(
            Component.text("${rocketLauncherRarity.rarityGlyph}${rocketLauncherType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A powerful rocket launcher,", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("which shoots knockback", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("inducing rockets.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        )
        rocketLauncherMeta.lore(rocketLauncherLore)
        rocketLauncherMeta.isUnbreakable = true
        rocketLauncherMeta.addEnchant(Enchantment.MENDING, 1, false)
        rocketLauncherMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        rocketLauncher.itemMeta = rocketLauncherMeta
        player.inventory.addItem(ItemStack(rocketLauncher))
        player.sendMessage(Component.text("You received a ${LobbyCustomItem.ROCKET_LAUNCHER} from an unknown source.").color(NamedTextColor.GREEN))
    }

    fun useLightningWand(player : Player) {
        val blocksToAdd = BlockIterator(player.eyeLocation, 0.0, 80)
        while(blocksToAdd.hasNext()) {
            val loc  = blocksToAdd.next().location
            if(loc.block.type.isSolid) {
                val world  = player.world
                world.strikeLightningEffect(loc)
                break
            } else {
                val world  = player.world
                world.spawnParticle(Particle.FIREWORKS_SPARK, loc, 1, 0.0, 0.0, 0.0, 0.05)
            }
        }
    }

    fun createLightningWand(player : Player) {
        val lightningWand = ItemStack(Material.BLAZE_ROD)
        val lightningWandMeta = lightningWand.itemMeta
        val lightningWandRarity = ItemRarity.SPECIAL
        val lightningWandType = ItemType.UTILITY
        lightningWandMeta.displayName(Component.text("Lightning Wand").color(TextColor.fromHexString(lightningWandRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
        val teleportSpoonLore = listOf(
            Component.text("${lightningWandRarity.rarityGlyph}${lightningWandType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A shard of Zeus' lightning bolt,", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("which allows the striking of", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("non-lethal thunder.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        )
        lightningWandMeta.lore(teleportSpoonLore)
        lightningWandMeta.isUnbreakable = true
        lightningWandMeta.addEnchant(Enchantment.MENDING, 1, false)
        lightningWandMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        lightningWand.itemMeta = lightningWandMeta
        player.inventory.addItem(ItemStack(lightningWand))
        player.sendMessage(Component.text("You received a ${LobbyCustomItem.LIGHTNING_WAND} from an unknown source.").color(NamedTextColor.GREEN))
    }

    fun useTeleportSpoon(player : Player) {
        val maxDistance = if(player.isSneaking) 80 else 12
        val block: Block = player.getTargetBlock(null, maxDistance)
        val location: Location = block.location
        val pitch = player.eyeLocation.pitch
        val yaw = player.eyeLocation.yaw
        location.add(0.5, 1.0, 0.5)
        location.yaw = yaw
        location.pitch = pitch
        player.teleport(location)
        player.playSound(player.location, "entity.enderman.teleport", 1.0f, 1.0f)

        if(maxDistance == 80) {
            player.playSound(player.location, "entity.elder_guardian.ambient", 1.0f, 2.0f)
            val sphereLoc = Location(player.location.world, player.location.x, player.location.y, player.location.z)
            val r = 1.5
            run {
                var phi = 0.0
                while (phi <= Math.PI) {
                    val y = r * cos(phi) + 1.5
                    var theta = 0.0
                    while (theta <= 2 * Math.PI) {
                        val x = r * cos(theta) * sin(phi)
                        val z = r * sin(theta) * sin(phi)
                        sphereLoc.add(x, y, z)
                        sphereLoc.world.spawnParticle(Particle.SPELL_WITCH, sphereLoc, 1, 0.0, 0.0, 0.0, 0.0)
                        sphereLoc.subtract(x, y, z)
                        theta += Math.PI / 30
                    }
                    phi += Math.PI / 15
                }
            }
        }
    }

    fun createTeleportSpoon(player : Player) {
        val teleportSpoon = ItemStack(Material.DIAMOND_SHOVEL)
        val teleportSpoonMeta = teleportSpoon.itemMeta
        val teleportSpoonRarity = ItemRarity.SPECIAL
        val teleportSpoonType = ItemType.UTILITY
        teleportSpoonMeta.displayName(Component.text("Aspect of the Void").color(TextColor.fromHexString(teleportSpoonRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
        val teleportSpoonLore = listOf(
            Component.text("${teleportSpoonRarity.rarityGlyph}${teleportSpoonType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("An out of this world wand that grants the", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("ability to teleport upon interaction.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        )
        teleportSpoonMeta.lore(teleportSpoonLore)
        teleportSpoonMeta.isUnbreakable = true
        teleportSpoonMeta.addEnchant(Enchantment.MENDING, 1, false)
        teleportSpoonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        teleportSpoon.itemMeta = teleportSpoonMeta
        player.inventory.addItem(ItemStack(teleportSpoon))
        player.sendMessage(Component.text("You received a ${LobbyCustomItem.TELEPORT_SPOON} from an unknown source.").color(NamedTextColor.GREEN))
    }

    fun createCowWand(player : Player) {
        val cowWand = ItemStack(Material.BONE)
        val cowWandMeta = cowWand.itemMeta
        val cowWandRarity = ItemRarity.SPECIAL
        val cowWandType = ItemType.UTILITY
        cowWandMeta.displayName(Component.text("Beefinator 1000").color(TextColor.fromHexString(cowWandRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
        val cowWandLore = listOf(
            Component.text("${cowWandRarity.rarityGlyph}${cowWandType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("moo.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        )
        cowWandMeta.lore(cowWandLore)
        cowWandMeta.isUnbreakable = true
        cowWandMeta.addEnchant(Enchantment.MENDING, 1, false)
        cowWandMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        cowWand.itemMeta = cowWandMeta
        player.inventory.addItem(ItemStack(cowWand))
        player.sendMessage(Component.text("You received a ${LobbyCustomItem.COW_WAND} from an unknown source.").color(NamedTextColor.GREEN))
    }

    fun useCowWand(player : Player) {
        val cow = player.world.spawn(player.eyeLocation, Cow::class.java)
        cow.isInvulnerable = true
        cow.velocity = player.eyeLocation.direction.multiply(6.0)
        player.world.playSound(player.location, "entity.firework_rocket.blast", 0.75f, 0f)
        object : BukkitRunnable() {
            override fun run() {
                cow.isInvulnerable = false
                if(!cow.isDead) cow.damage(20.0)
                val tnt = player.world.spawn(cow.location, TNTPrimed::class.java)
                tnt.fuseTicks = 0

            }
        }.runTaskLater(game.plugin, 10L)
    }
}

enum class LobbyCustomItem {
    ROCKET_LAUNCHER,
    LIGHTNING_WAND,
    TELEPORT_SPOON,
    COW_WAND
}
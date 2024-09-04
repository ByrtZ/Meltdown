package dev.byrt.meltdown.lobby

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.manager.ItemRarity
import dev.byrt.meltdown.manager.ItemType
import dev.byrt.meltdown.util.DevStatus

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

import kotlin.random.Random

class Fishington(private val game : Game) {
    private val fishingLootTypeChances = ArrayList<FishingLootType>()
    private val fishLootChances = ArrayList<FishDrops>()
    private val junkLootChances = ArrayList<JunkDrops>()
    private val treasureLootChances = ArrayList<TreasureDrops>()
    private val specialLootChances = ArrayList<SpecialDrops>()

    /** Initialises the fishing system. **/
    fun setup() {
        game.plugin.logger.info("Populating drop chances...")
        populateFishingLootTypeChances()
        populateFishLootChances()
        populateJunkLootChances()
        populateTreasureLootChances()
        populateSpecialLootChances()
    }

    /** Master method for rolling drops. This method rolls for a loot type, a random drop within such loot type and subsequently spawning the drop for the player. **/
    fun rollRandomDrop() : ItemStack {
        when(fishingLootTypeChances.shuffled().random()) {
            FishingLootType.FISH_LOOT -> {
                val fish = fishLootChances.shuffled().random()
                val drop = ItemStack(fish.material, 1)
                val dropMeta = drop.itemMeta
                dropMeta.displayName(fish.nameComponent.color(TextColor.fromHexString(fish.itemRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
                dropMeta.lore(listOf(
                    Component.text("${fish.itemRarity.rarityGlyph}${fish.itemType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                ) + fish.loreComponents)
                dropMeta.isUnbreakable = true
                dropMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
                drop.itemMeta = dropMeta
                return drop
            }
            FishingLootType.JUNK_LOOT -> {
                val junk = junkLootChances.shuffled().random()
                val drop = ItemStack(junk.material, 1)
                val dropMeta = drop.itemMeta
                dropMeta.displayName(junk.nameComponent.color(TextColor.fromHexString(junk.itemRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
                dropMeta.lore(listOf(
                    Component.text("${junk.itemRarity.rarityGlyph}${junk.itemType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                ) + junk.loreComponents)
                dropMeta.isUnbreakable = true
                dropMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
                drop.itemMeta = dropMeta
                return drop
            }
            FishingLootType.TREASURE_LOOT -> {
                val treasure = treasureLootChances.shuffled().random()
                return if(treasure == TreasureDrops.ARMOUR_TRIM || treasure == TreasureDrops.MUSIC_DISC || treasure == TreasureDrops.ENCHANTED_BOOK) {
                    rollRandomTreasure(treasure)
                } else {
                    val drop = ItemStack(treasure.material, 1)
                    val dropMeta = drop.itemMeta
                    dropMeta.displayName(treasure.nameComponent.color(TextColor.fromHexString(treasure.itemRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
                    dropMeta.lore(listOf(
                        Component.text("${treasure.itemRarity.rarityGlyph}${treasure.itemType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                    ) + treasure.loreComponents)
                    dropMeta.isUnbreakable = true
                    dropMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
                    drop.itemMeta = dropMeta
                    drop
                }
            }
            FishingLootType.SPECIAL_LOOT -> {
                val special = specialLootChances.shuffled().random()
                val drop = ItemStack(special.material, 1)
                val dropMeta = drop.itemMeta
                dropMeta.displayName(special.nameComponent.color(TextColor.fromHexString(special.itemRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
                dropMeta.lore(listOf(
                    Component.text("${special.itemRarity.rarityGlyph}${special.itemType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                ) + special.loreComponents)
                dropMeta.isUnbreakable = true
                dropMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
                drop.itemMeta = dropMeta
                return drop
            }
        }
    }

    /** Rolls a drop in the treasure category if a rolled drop has a sub-loot pool.
     * @return an ItemStack to be spawned for the player.
    **/
    private fun rollRandomTreasure(treasureDrop : TreasureDrops) : ItemStack {
        when(treasureDrop) {
            TreasureDrops.ENCHANTED_BOOK -> {
                val enchantmentsList = ArrayList<Enchantment>()
                Registry.ENCHANTMENT.forEach{ enchantment -> enchantmentsList.add(enchantment) }
                val enchantedBook = ItemStack(Material.ENCHANTED_BOOK, 1)
                val randomEnchantments = enchantmentsList.shuffled().subList(0, 2)
                val enchantedBookMeta = enchantedBook.itemMeta
                for(enchant in randomEnchantments) {
                    enchantedBookMeta.addEnchant(enchant, Random.nextInt(1, 10), true)
                }
                enchantedBookMeta.displayName((treasureDrop.nameComponent).color(TextColor.fromHexString(treasureDrop.itemRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
                val lore = listOf(
                    Component.text("${treasureDrop.itemRarity.rarityGlyph}${treasureDrop.itemType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                ) + treasureDrop.loreComponents
                enchantedBookMeta.lore(lore)
                enchantedBook.itemMeta = enchantedBookMeta
                return enchantedBook
            }
            TreasureDrops.ARMOUR_TRIM -> {
                val armourTrim = ItemStack(getRandomTrimFromKey(), 1)
                val armourTrimMeta = armourTrim.itemMeta
                armourTrimMeta.displayName((treasureDrop.nameComponent).color(TextColor.fromHexString(treasureDrop.itemRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
                val lore = listOf(
                    Component.text("${treasureDrop.itemRarity.rarityGlyph}${treasureDrop.itemType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                ) + treasureDrop.loreComponents
                armourTrimMeta.lore(lore)
                armourTrim.itemMeta = armourTrimMeta
                return armourTrim
            }
            TreasureDrops.MUSIC_DISC -> {
                val musicDisc = ItemStack(getRandomMusicDisc(), 1)
                val musicDiscMeta = musicDisc.itemMeta
                musicDiscMeta.displayName((treasureDrop.nameComponent).color(TextColor.fromHexString(treasureDrop.itemRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
                val lore = listOf(
                    Component.text("${treasureDrop.itemRarity.rarityGlyph}${treasureDrop.itemType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                ) + treasureDrop.loreComponents
                musicDiscMeta.lore(lore)
                musicDisc.itemMeta = musicDiscMeta
                return musicDisc
            }
            else -> {
                game.dev.parseDevMessage("Fishington failed to roll a random ${treasureDrop}.", DevStatus.WARNING)
                return ItemStack(Material.POTATO, 1)
            }
        }
    }

    /** Populates fishing loot type chances. These chances are for rolling a random category of loot and are added into the list x amount of times as per their weight. **/
    private fun populateFishingLootTypeChances() {
        game.plugin.logger.info("Populating loot type chances...")
        for(lootType in FishingLootType.entries) {
            for(i in 1..lootType.lootTypeChance) {
                fishingLootTypeChances.add(lootType)
            }
        }
    }

    /** Populates fish loot chances. These chances are for rolling a random category of loot and are added into the list x amount of times as per their weight. **/
    private fun populateFishLootChances() {
        game.plugin.logger.info("Populating fish drop chances...")
        for(fishDrop in FishDrops.entries) {
            for(i in 1..fishDrop.dropChance) {
                fishLootChances.add(fishDrop)
            }
        }
    }

    /** Populates junk loot chances. These chances are for rolling a random category of loot and are added into the list x amount of times as per their weight. **/
    private fun populateJunkLootChances() {
        game.plugin.logger.info("Populating junk drop chances...")
        for(junkDrop in JunkDrops.entries) {
            for(i in 1..junkDrop.dropChance) {
                junkLootChances.add(junkDrop)
            }
        }
    }

    /** Populates treasure loot chances. These chances are for rolling a random category of loot and are added into the list x amount of times as per their weight. **/
    private fun populateTreasureLootChances() {
        game.plugin.logger.info("Populating treasure drop chances...")
        for(treasureDrop in TreasureDrops.entries) {
            for(i in 1..treasureDrop.dropChance) {
                treasureLootChances.add(treasureDrop)
            }
        }
    }

    /** Populates special loot chances. These chances are for rolling a random category of loot and are added into the list x amount of times as per their weight. **/
    private fun populateSpecialLootChances() {
        game.plugin.logger.info("Populating special drop chances...")
        for(specialDrop in SpecialDrops.entries) {
            for(i in 1..specialDrop.dropChance) {
                specialLootChances.add(specialDrop)
            }
        }
    }

    /** Returns a random Music Disc as a Material.
     * @return a random Music Disc Material.
     **/
    private fun getRandomMusicDisc() : Material {
        val musicDiscs = ArrayList<Material>()
        musicDiscs.add(Material.MUSIC_DISC_11)
        musicDiscs.add(Material.MUSIC_DISC_13)
        musicDiscs.add(Material.MUSIC_DISC_5)
        musicDiscs.add(Material.MUSIC_DISC_BLOCKS)
        musicDiscs.add(Material.MUSIC_DISC_CAT)
        musicDiscs.add(Material.MUSIC_DISC_CHIRP)
        musicDiscs.add(Material.MUSIC_DISC_BLOCKS)
        musicDiscs.add(Material.MUSIC_DISC_FAR)
        musicDiscs.add(Material.MUSIC_DISC_MALL)
        musicDiscs.add(Material.MUSIC_DISC_MELLOHI)
        musicDiscs.add(Material.MUSIC_DISC_OTHERSIDE)
        musicDiscs.add(Material.MUSIC_DISC_PIGSTEP)
        musicDiscs.add(Material.MUSIC_DISC_RELIC)
        musicDiscs.add(Material.MUSIC_DISC_STAL)
        musicDiscs.add(Material.MUSIC_DISC_STRAD)
        musicDiscs.add(Material.MUSIC_DISC_WAIT)
        musicDiscs.add(Material.MUSIC_DISC_WARD)
        return musicDiscs[Random.nextInt(musicDiscs.size)]
    }

    /** Returns a random Armour Trim as a Material.
     * @return a random Armour Trim Material.
    **/
    private fun getRandomTrimFromKey() : Material {
        val keys = ArrayList<NamespacedKey>()
        Registry.TRIM_PATTERN.forEach{ trim -> Registry.TRIM_PATTERN.get(trim.key() as NamespacedKey)}
        return when(keys[Random.nextInt(keys.size)].key) {
            "sentry" -> Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE
            "dune" -> Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE
            "coast" -> Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE
            "wild" -> Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE
            "ward" -> Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE
            "eye" -> Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE
            "vex" -> Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE
            "tide" -> Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE
            "snout" -> Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE
            "rib" -> Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE
            "spire" -> Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE
            "wayfinder" -> Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE
            "shaper" -> Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE
            "silence" -> Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE
            "host" -> Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE
            else -> Material.POTATO
        }
    }
}

/** Defines a loot type to be randomly rolled by fishing.
 * @param lootTypeName Name of the loot type.
 * @param lootTypeChance Chance of the loot type occurring, relative to all other loot types.
 **/
enum class FishingLootType(val lootTypeName : String, val lootTypeChance : Int) {
    FISH_LOOT("Fish", 80),
    JUNK_LOOT("Junk", 50),
    TREASURE_LOOT("Treasure", 20),
    SPECIAL_LOOT("Special", 1)
}

/** Defines a Fish loot item.
 * @param material Item material.
 * @param nameComponent Display name of the item.
 * @param loreComponents Lore of the item.
 * @param itemRarity Item's rarity.
 * @param itemType Item's type.
 * @param dropChance Drop chance of the item, relative to all other drops.
 **/
enum class FishDrops(val material : Material, val nameComponent : Component, val loreComponents : List<Component>, val itemRarity : ItemRarity, val itemType : ItemType, val dropChance : Int) {
    COD(Material.COD, Component.text("Cod"), listOf(Component.text("A regular, average, run of the mill, cod.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.COMMON, ItemType.CONSUMABLE, 4),
    SALMON(Material.SALMON, Component.text("Salmon"), listOf(Component.text("A silly goofy little salmon.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.COMMON, ItemType.CONSUMABLE, 3),
    CLOWNFISH(Material.TROPICAL_FISH, Component.text("Clownfish"), listOf(Component.text("HAHAHAHAHAHAHAHA!", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.UNCOMMON, ItemType.CONSUMABLE, 2),
    PUFFERFISH(Material.PUFFERFISH, Component.text("Pufferfish"), listOf(Component.text("Not so bad once you get to know him.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.UNCOMMON, ItemType.CONSUMABLE, 1)
}

/** Defines a Junk loot item.
 * @param material Item material.
 * @param nameComponent Display name of the item.
 * @param loreComponents Lore of the item.
 * @param itemRarity Item's rarity.
 * @param itemType Item's type.
 * @param dropChance Drop chance of the item, relative to all other drops.
 **/
enum class JunkDrops(val material : Material, val nameComponent : Component, val loreComponents : List<Component>, val itemRarity : ItemRarity, val itemType : ItemType, val dropChance : Int) {
    LEATHER_BOOTS(Material.LEATHER_BOOTS, Component.text("Old Shoes"), listOf(Component.text("A pair of soggy, smelly shoes.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.COMMON, ItemType.ARMOUR, 4),
    STRING(Material.STRING, Component.text("String"), listOf(Component.text("How long do you think it is?", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.COMMON, ItemType.CONSUMABLE, 4),
    ROTTEN_FLESH(Material.ROTTEN_FLESH, Component.text("Rotten Flesh"), listOf(Component.text("Gross.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.COMMON, ItemType.CONSUMABLE, 4),
    BONE_MEAL(Material.BONE_MEAL, Component.text("Bone Meal"), listOf(Component.text("Perfect for your lawn!", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.COMMON, ItemType.CONSUMABLE, 4),
    STICK(Material.STICK, Component.text("Stick"), listOf(Component.text("What is something brown and sticky?", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.COMMON, ItemType.WEAPON, 4),
    MESSAGE_IN_A_BOTTLE(Material.POTION, Component.text("Message in a Bottle"), listOf(Component.text("There seems to be a crumbled piece", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), Component.text("of paper inside...", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.UNCOMMON, ItemType.CONSUMABLE, 1),
    INK_SAC(Material.INK_SAC, Component.text("Ink Sac"), listOf(Component.text("Need to refill your pen?", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.COMMON, ItemType.CONSUMABLE, 4),
    KELP(Material.KELP, Component.text("Kelp"), listOf(Component.text("Like grass, but from the sea.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.COMMON, ItemType.CONSUMABLE, 4),
    HONEYCOMB(Material.HONEYCOMB, Component.text("Honeycomb"), listOf(Component.text("Fresh from local bees.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.COMMON, ItemType.CONSUMABLE, 4),
    FEATHER(Material.FEATHER, Component.text("Feather"), listOf(Component.text("Soft, floaty and tickly.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.COMMON, ItemType.CONSUMABLE, 4),
    FLINT(Material.FLINT, Component.text("Flint"), listOf(Component.text("Doesn't drop in your speedruns.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.COMMON, ItemType.CONSUMABLE, 4)
}

/** Defines a Treasure loot item.
 * @param material Item material.
 * @param nameComponent Display name of the item.
 * @param loreComponents Lore of the item.
 * @param itemRarity Item's rarity.
 * @param itemType Item's type.
 * @param dropChance Drop chance of the item, relative to all other drops.
**/
enum class TreasureDrops(val material : Material, val nameComponent : Component, val loreComponents : List<Component>, val itemRarity : ItemRarity, val itemType : ItemType, val dropChance : Int) {
    ENCHANTED_BOOK(Material.POTATO, Component.text("Enchanted Book"), listOf(Component.text("Imbued with ancient, powerful effects.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.RARE, ItemType.CONSUMABLE, 6),
    NAUTILUS_SHELL(Material.NAUTILUS_SHELL, Component.text("Nautilus Shell"), listOf(Component.text("The former home of a hermit crab.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.RARE, ItemType.CONSUMABLE, 6),
    SADDLE(Material.SADDLE, Component.text("Saddle"), listOf(Component.text("A perfect fit for your horse.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.UNCOMMON, ItemType.CONSUMABLE, 7),
    NAME_TAG(Material.NAME_TAG, Component.text("Name Tag"), listOf(Component.text("Need one of your animals to", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), Component.text("know their own name?", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.UNCOMMON, ItemType.CONSUMABLE, 7),
    COPPER_INGOT(Material.COPPER_INGOT, Component.text("Copper Ingot"), listOf(Component.text("A great conductor.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.RARE, ItemType.CONSUMABLE, 6),
    GOLD_INGOT(Material.GOLD_INGOT, Component.text("Gold Ingot"), listOf(Component.text("Valuable but not very strong.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.EPIC, ItemType.CONSUMABLE, 5),
    IRON_INGOT(Material.IRON_INGOT, Component.text("Iron Ingot"), listOf(Component.text("Widely used by miners and crafters.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.RARE, ItemType.CONSUMABLE, 5),
    HEART_OF_THE_SEA(Material.HEART_OF_THE_SEA, Component.text("Heart of the Sea"), listOf(Component.text("Found at the deepest depths of the ocean.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.LEGENDARY, ItemType.CONSUMABLE, 4),
    ARMOUR_TRIM(Material.POTATO, Component.text("Armour Trim"), listOf(Component.text("An extremely rare template, which can", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), Component.text("decorate any armour piece of your choosing.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.MYTHIC, ItemType.CONSUMABLE, 1),
    DIAMOND(Material.DIAMOND, Component.text("Diamond"), listOf(Component.text("Glistens with a sacred luster.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.LEGENDARY, ItemType.CONSUMABLE, 2),
    DIAMOND_BLOCK(Material.DIAMOND_BLOCK, Component.text("Diamond Block"), listOf(Component.text("A very dense amalgamation of diamonds.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.MYTHIC, ItemType.CONSUMABLE, 1),
    MUSIC_DISC(Material.POTATO, Component.text("Music Disc"), listOf(Component.text("Plays your favourite bops!", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.EPIC, ItemType.CONSUMABLE, 4),
}

/** Defines a Special loot item.
 * @param material Item material.
 * @param nameComponent Display name of the item.
 * @param loreComponents Lore of the item.
 * @param itemRarity Item's rarity.
 * @param itemType Item's type.
 * @param dropChance Drop chance of the item, relative to all other drops.
 **/
enum class SpecialDrops(val material : Material, val nameComponent : Component, val loreComponents : List<Component>, val itemRarity : ItemRarity, val itemType : ItemType, val dropChance : Int) {
    TELEPORT_SPOON(Material.DIAMOND_SHOVEL, Component.text("Aspect of the Void"), listOf(Component.text("An out of this world wand that grants the", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), Component.text("ability to teleport upon interaction.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.SPECIAL, ItemType.UTILITY, 1),
    ROCKET_LAUNCHER(Material.GOLDEN_HORSE_ARMOR, Component.text("Rocket Launcher"), listOf(Component.text("A powerful rocket launcher,", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), Component.text("which shoots knockback", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), Component.text("inducing rockets.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.SPECIAL, ItemType.UTILITY, 5),
    LIGHTNING_WAND(Material.BLAZE_ROD, Component.text("Lightning Wand"), listOf(Component.text("A shard of Zeus' lightning bolt,", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), Component.text("which allows the striking of", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), Component.text("non-lethal thunder.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.SPECIAL, ItemType.UTILITY, 8),
    COW_WAND(Material.BONE, Component.text("Beefinator 1000"), listOf(Component.text("moo.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)), ItemRarity.SPECIAL, ItemType.UTILITY, 4)
}
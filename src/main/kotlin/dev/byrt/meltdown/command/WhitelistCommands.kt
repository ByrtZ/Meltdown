package dev.byrt.meltdown.command

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.state.Sounds
import dev.byrt.meltdown.manager.WhitelistGroup
import dev.byrt.meltdown.util.DevStatus
import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import org.incendo.cloud.processors.confirmation.annotation.Confirmation

import java.time.Duration
import java.util.*

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class WhitelistCommands {
    private val whitelistStartSound: Sound = Sound.sound(Key.key(Sounds.Command.WHITELIST_START), Sound.Source.MASTER, 1f, 1f)
    private val whitelistCompleteSound: Sound = Sound.sound(Key.key(Sounds.Command.WHITELIST_COMPLETE), Sound.Source.MASTER, 1f, 2f)
    private val whitelistFailSound: Sound = Sound.sound(Key.key(Sounds.Command.WHITELIST_FAIL), Sound.Source.MASTER, 1f, 0f)

    @Command("whitelist set <group>")
    @CommandDescription("Sets whitelisted players to the specified group.")
    @Permission("meltdown.whitelist.setgroup")
    @Confirmation
    fun whitelistGroup(css: CommandSourceStack, @Argument("group") group : WhitelistGroup) {
        try {
            Main.getGame().whitelistManager.setWhitelist(group)
            Main.getGame().dev.parseDevMessage("Whitelist group set to $group by ${css.sender.name}.", DevStatus.INFO_SUCCESS)
        } catch (e : Exception) {
            Main.getGame().dev.parseDevMessage("Unable to whitelist group $group as an error occurred.", DevStatus.INFO_FAIL)
        }
    }

    private fun whitelistStartDisplay(player : Player, group : WhitelistGroup) {
        player.showTitle(Title.title(Component.text(""), Component.text("Whitelisting $group group...").color(NamedTextColor.RED), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))))
        player.playSound(whitelistStartSound)
    }

    private fun whitelistCompleteDisplay(player : Player, group : WhitelistGroup) {
        player.showTitle(Title.title(Component.text(""), Component.text("Successfully whitelisted $group group!").color(NamedTextColor.GREEN), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
        player.playSound(whitelistCompleteSound)

        Main.getGame().dev.parseDevMessage("Whitelist group set to $group by ${player.name}.", DevStatus.INFO_SUCCESS)
    }

    private fun whitelistFailDisplay(player : Player, group : WhitelistGroup) {
        player.showTitle(Title.title(Component.text(""), Component.text("Whitelisting $group group failed.").color(NamedTextColor.RED), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
        player.playSound(whitelistFailSound)
    }

    @Command("whitelist add <player> <group>")
    @CommandDescription("Adds the specified player to the specified whitelist group.")
    @Permission("meltdown.whitelist.add")
    fun addPlayerToWhitelistGroup(css: CommandSourceStack, @Argument("player") player : String, @Argument("group") group : WhitelistGroup) {
        css.sender.sendMessage(Component.text("Attempting to add $player to $group whitelist group...", NamedTextColor.GRAY))
        try {
            when(group) {
                WhitelistGroup.OFF -> {
                    css.sender.sendMessage(Component.text("Unable to modify this group as it is not used to store players.", NamedTextColor.RED))
                }
                WhitelistGroup.CLEAR -> {
                    css.sender.sendMessage(Component.text("Unable to modify this group as it is not used to store players.", NamedTextColor.RED))
                } else -> {
                val whitelist: ArrayList<String> = Main.getGame().configManager.getWhitelistConfig().getStringList("group.${group.toString().lowercase()}") as ArrayList<String>
                whitelist.add(player)
                Main.getGame().configManager.getWhitelistConfig().set("group.${group.toString().lowercase()}", whitelist)
                Main.getGame().configManager.saveWhitelistConfig()
                Main.getGame().dev.parseDevMessage("$player successfully added to $group whitelist group by ${css.sender.name}.", DevStatus.INFO_SUCCESS)
            }
            }

        } catch (e : Exception) {
            css.sender.sendMessage(Component.text("Failed to add $player to $group group.", NamedTextColor.RED))
        }
    }

    @Command("whitelist remove <player> <group>")
    @CommandDescription("Removes the specified player from the specified whitelist group.")
    @Permission("meltdown.whitelist.remove")
    fun removePlayerFromWhitelistGroup(css: CommandSourceStack, @Argument("player") player : String, @Argument("group") group : WhitelistGroup) {
        css.sender.sendMessage(Component.text("Attempting to remove $player to $group whitelist group...", NamedTextColor.GRAY))
        try {
            when(group) {
                WhitelistGroup.OFF -> {
                    css.sender.sendMessage(Component.text("Unable to modify this group as it is not used to store players.", NamedTextColor.RED))
                }
                WhitelistGroup.CLEAR -> {
                    css.sender.sendMessage(Component.text("Unable to modify this group as it is not used to store players.", NamedTextColor.RED))
                } else -> {
                val whitelist: ArrayList<String> = Main.getGame().configManager.getWhitelistConfig().getStringList("group.${group.toString().lowercase()}") as ArrayList<String>
                whitelist.remove(player)
                Main.getGame().configManager.getWhitelistConfig().set("group.${group.toString().lowercase()}", whitelist)
                Main.getGame().configManager.saveWhitelistConfig()
                Main.getGame().dev.parseDevMessage("$player successfully removed from $group whitelist group by ${css.sender.name}.", DevStatus.INFO_SUCCESS)
            }
            }

        } catch (e : Exception) {
            css.sender.sendMessage(Component.text("Failed to add $player to $group group.", NamedTextColor.RED))
        }
    }

    @Command("whitelist tempadd <player>")
    @CommandDescription("Temporarily adds the specified player to the server whitelist until removal or restart.")
    @Permission("meltdown.whitelist.tempadd")
    fun addPlayerTempWhitelist(css: CommandSourceStack, @Argument("player") player : String) {
        css.sender.sendMessage(Component.text("Attempting to temporarily whitelist $player...", NamedTextColor.GRAY))
        try {
            if(!Main.getPlugin().server.whitelistedPlayers.contains(Bukkit.getOfflinePlayer(player))) {
                Main.getGame().whitelistManager.tempWhitelistPlayer(player)
                Main.getGame().dev.parseDevMessage("$player successfully added to temporary whitelist by ${css.sender.name}.", DevStatus.INFO_SUCCESS)
            } else {
                css.sender.sendMessage(Component.text("$player is already on whitelist.", NamedTextColor.RED))
            }
        } catch (e : Exception) {
            css.sender.sendMessage(Component.text("Failed to add $player to whitelist.", NamedTextColor.RED))
        }
    }

    @Command("whitelist tempremove <player>")
    @CommandDescription("Temporarily remove the specified player to the server whitelist.")
    @Permission("meltdown.whitelist.tempremove")
    fun removePlayerTempWhitelist(css: CommandSourceStack, @Argument("player") player : String) {
        css.sender.sendMessage(Component.text("Attempting to temporarily unwhitelist $player...", NamedTextColor.GRAY))
        try {
            if(Main.getPlugin().server.whitelistedPlayers.contains(Bukkit.getOfflinePlayer(player))) {
                Main.getGame().whitelistManager.removeTempWhitelistPlayer(player)
                Main.getGame().dev.parseDevMessage("$player successfully removed from temporary whitelist by ${css.sender.name}.", DevStatus.INFO_SUCCESS)
            } else {
                css.sender.sendMessage(Component.text("$player is not on whitelist.", NamedTextColor.RED))
            }
        } catch (e : Exception) {
            css.sender.sendMessage(Component.text("Failed to remove $player from whitelist.", NamedTextColor.RED))
        }
    }

    @Command("whitelist reload")
    @CommandDescription("Reloads the whitelist.yml file.")
    @Permission("meltdown.whitelist.reload")
    @Confirmation
    fun whitelistReload(css: CommandSourceStack) {
        css.sender.sendMessage(Component.text("Reloading whitelist configuration...", NamedTextColor.RED))
        try {
            Main.getGame().configManager.saveWhitelistConfig()
            Main.getGame().configManager.reloadWhitelistConfig()
            Main.getGame().dev.parseDevMessage("Whitelist configuration reloaded by ${css.sender.name}.", DevStatus.INFO)

        } catch (e : Exception) {
            css.sender.sendMessage(Component.text("Failed to reload whitelist configuration.", NamedTextColor.RED))
        }
    }

    @Command("whitelist list")
    @CommandDescription("Lists the currently whitelisted players and group.")
    @Permission("meltdown.whitelist.list")
    fun whitelistList(css: CommandSourceStack) {
        try {
            css.sender.sendMessage(Component.text("Current whitelisted group: ${Main.getGame().whitelistManager.getWhitelistedGroup()}\n", NamedTextColor.GOLD)
                .append(Component.text("Players: ${Main.getGame().configManager.getWhitelistConfig().getStringList("group.${Main.getGame().whitelistManager.getWhitelistedGroup().toString().lowercase()}")}", NamedTextColor.YELLOW)))

        } catch (e : Exception) {
            css.sender.sendMessage(Component.text("Failed to list currently whitelisted players and group.", NamedTextColor.RED))
        }
    }
}
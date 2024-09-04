package dev.byrt.meltdown.command

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.util.DevStatus

import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component

import org.bukkit.Material
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

import java.util.ArrayList

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class ConfigCommands {
    @Command("config meltable add <material>")
    @CommandDescription("Configuration command to add materials to the meltable blocks list.")
    @Permission("meltdown.config.meltable")
    fun meltableConfigAdd(css: CommandSourceStack, @Argument("material") material : Material) {
        Main.getGame().dev.parseDevMessage("Material ${material.name} added to Meltable configuration by ${css.sender.name}.", DevStatus.INFO)
        val meltable: ArrayList<String> = Main.getGame().configManager.getMeltableConfig().getStringList("meltable") as ArrayList<String>
        meltable.add(material.name)
        Main.getGame().configManager.getMeltableConfig().set("meltable", meltable)
        Main.getGame().configManager.saveMeltableConfig()
        Main.getGame().configManager.reloadMeltableConfig()
    }

    @Command("config meltable remove <material>")
    @CommandDescription("Configuration command to remove materials from the meltable blocks list.")
    @Permission("meltdown.config.meltable")
    fun meltableConfigRemove(css: CommandSourceStack, @Argument("material") material : Material) {
        Main.getGame().dev.parseDevMessage("Material ${material.name} removed from Meltable configuration by ${css.sender.name}.", DevStatus.INFO)
        val meltable: ArrayList<String> = Main.getGame().configManager.getMeltableConfig().getStringList("meltable") as ArrayList<String>
        meltable.remove(material.name)
        Main.getGame().configManager.getMeltableConfig().set("meltable", meltable)
        Main.getGame().configManager.saveMeltableConfig()
        Main.getGame().configManager.reloadMeltableConfig()
    }

    @Command("config meltable get")
    @CommandDescription("Configuration command to get materials in the meltable blocks list.")
    @Permission("meltdown.config.meltable")
    fun meltableConfigGet(css: CommandSourceStack) {
        css.sender.sendMessage(Component.text("Meltable blocks: ${Main.getGame().configManager.getMeltableConfig().getStringList("meltable")}"))
    }
}
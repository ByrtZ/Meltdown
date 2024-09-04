package dev.byrt.meltdown.command

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.util.DevStatus

import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.entity.Player

import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class QueueCommands {
    @Command("queue data")
    @CommandDescription("Debug command to get queue data.")
    @Permission("meltdown.debug")
    fun debugQueueData(css: CommandSourceStack) {
        css.sender.sendMessage(
            Component.text("QUEUE DATA\n", NamedTextColor.GOLD, TextDecoration.BOLD).append(
                Component.text("Queue", NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false).append(
                    Component.text(": ${Main.getGame().queue.getQueue()}\n", NamedTextColor.WHITE).append(
                        Component.text("State", NamedTextColor.RED).append(
                            Component.text(": ${Main.getGame().queue.getQueueState()}", NamedTextColor.WHITE)
                        )
                    )
                )
            )
        )
    }

    @Command("queue join")
    @CommandDescription("Debug command for queues.")
    @Permission("meltdown.debug")
    fun debugJoinQueue(css: CommandSourceStack) {
        if(css.sender is Player) {
            val player = css.sender as Player
            Main.getGame().queue.joinQueue(player)
        }
    }

    @Command("queue leave")
    @CommandDescription("Debug command for queues.")
    @Permission("meltdown.debug")
    fun debugLeaveQueue(css: CommandSourceStack) {
        if(css.sender is Player) {
            val player = css.sender as Player
            Main.getGame().queue.leaveQueue(player)
        }
    }

    @Command("queue force join <player>")
    @CommandDescription("Debug command for queues.")
    @Permission("meltdown.debug")
    fun debugForceJoinQueue(css: CommandSourceStack, @Argument("player") player : Player) {
        Main.getGame().dev.parseDevMessage("${css.sender.name} pushed ${player.name} into the Queue.", DevStatus.INFO)
        Main.getGame().queue.joinQueue(player)
    }

    @Command("queue force leave <player>")
    @CommandDescription("Debug command for queues.")
    @Permission("meltdown.debug")
    fun debugForceLeaveQueue(css: CommandSourceStack, @Argument("player") player : Player) {
        Main.getGame().dev.parseDevMessage("${css.sender.name} threw ${player.name} out of the Queue.", DevStatus.INFO)
        Main.getGame().queue.leaveQueue(player)
    }

    @Command("queue set max <int>")
    @CommandDescription("Debug command for queues.")
    @Permission("meltdown.queue")
    fun queueSetMax(@Argument("int") int : Int) {
        Main.getGame().queue.setMaxPlayers(int)
        Main.getGame().queueVisuals.updateQueueStatus()
        Main.getGame().queue.checkQueueCanStart()
    }

    @Command("queue set min <int>")
    @CommandDescription("Debug command for queues.")
    @Permission("meltdown.queue")
    fun queueSetMin(@Argument("int") int : Int) {
        Main.getGame().queue.setMinPlayers(int)
        Main.getGame().queueVisuals.updateQueueStatus()
        Main.getGame().queue.checkQueueCanStart()
    }
}
package dev.byrt.meltdown.command

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.util.DevStatus

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

@Suppress("unused")
class QueueCommands : BaseCommand {
    @CommandMethod("queue data")
    @CommandDescription("Debug command to get queue data.")
    @CommandPermission("meltdown.debug")
    fun debugQueueData(sender : Player) {
        sender.sendMessage(
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

    @CommandMethod("queue join")
    @CommandDescription("Debug command for queues.")
    @CommandPermission("meltdown.debug")
    fun debugJoinQueue(sender : Player) {
        Main.getGame().queue.joinQueue(sender)
    }

    @CommandMethod("queue leave")
    @CommandDescription("Debug command for queues.")
    @CommandPermission("meltdown.debug")
    fun debugLeaveQueue(sender : Player) {
        Main.getGame().queue.leaveQueue(sender)
    }

    @CommandMethod("queue force join <player>")
    @CommandDescription("Debug command for queues.")
    @CommandPermission("meltdown.debug")
    fun debugForceJoinQueue(sender : Player, @Argument("player") player : Player) {
        Main.getGame().dev.parseDevMessage("${sender.name} pushed ${player.name} into the Queue.", DevStatus.INFO)
        Main.getGame().queue.joinQueue(player)
    }

    @CommandMethod("queue force leave <player>")
    @CommandDescription("Debug command for queues.")
    @CommandPermission("meltdown.debug")
    fun debugForceLeaveQueue(sender : Player, @Argument("player") player : Player) {
        Main.getGame().dev.parseDevMessage("${sender.name} threw ${player.name} out of the Queue.", DevStatus.INFO)
        Main.getGame().queue.leaveQueue(player)
    }

    @CommandMethod("queue set max <int>")
    @CommandDescription("Debug command for queues.")
    @CommandPermission("meltdown.queue")
    fun queueSetMax(@Argument("int") int : Int) {
        Main.getGame().queue.setMaxPlayers(int)
        Main.getGame().queueVisuals.updateQueueStatus()
        Main.getGame().queue.checkQueueCanStart()
    }

    @CommandMethod("queue set min <int>")
    @CommandDescription("Debug command for queues.")
    @CommandPermission("meltdown.queue")
    fun queueSetMin(@Argument("int") int : Int) {
        Main.getGame().queue.setMinPlayers(int)
        Main.getGame().queueVisuals.updateQueueStatus()
        Main.getGame().queue.checkQueueCanStart()
    }
}
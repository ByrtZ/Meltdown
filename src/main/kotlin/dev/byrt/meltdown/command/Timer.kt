package dev.byrt.meltdown.command

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.state.TimerState
import dev.byrt.meltdown.util.DevStatus

import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Timer {
    @Command("timer <setting> [seconds]")
    @CommandDescription("Allows timer modification.")
    @Permission("meltdown.timer")
    fun timer(css: CommandSourceStack, @Argument("setting") option : TimerOptions, @Argument("seconds") time : Int?) {
        if(Main.getGame().timerManager.getTimerState() == TimerState.INACTIVE) {
            css.sender.sendMessage(Component.text("Unable to manipulate timer when inactive.").color(NamedTextColor.RED))
        } else {
            when(option) {
                TimerOptions.SET -> {
                    if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
                        css.sender.sendMessage(Component.text("Unable to set timer when no game active.").color(NamedTextColor.RED))
                    } else {
                        if(time == null || time <= 0) {
                            css.sender.sendMessage(Component.text("Unable to set timer to zero, less than zero or null.").color(NamedTextColor.RED))
                        } else {
                            Main.getGame().gameTask.setTimeLeft(time, css.sender)
                        }
                    }
                }
                TimerOptions.PAUSE -> {
                    if(Main.getGame().timerManager.getTimerState() != TimerState.ACTIVE) {
                        css.sender.sendMessage(Component.text("Unable to pause timer when inactive.").color(NamedTextColor.RED))
                    } else {
                        Main.getGame().timerManager.setTimerState(TimerState.PAUSED)
                        Main.getGame().dev.parseDevMessage("Timer state updated to ${Main.getGame().timerManager.getTimerState()} by ${css.sender.name}.", DevStatus.INFO)
                    }
                }
                TimerOptions.RESUME -> {
                    if(Main.getGame().timerManager.getTimerState() != TimerState.PAUSED) {
                        css.sender.sendMessage(Component.text("Unable to resume timer when already active.").color(NamedTextColor.RED))
                    } else {
                        Main.getGame().timerManager.setTimerState(TimerState.ACTIVE)
                        Main.getGame().dev.parseDevMessage("Timer state updated to ${Main.getGame().timerManager.getTimerState()} by ${css.sender.name}.", DevStatus.INFO)
                    }
                }
                TimerOptions.SKIP -> {
                    if(Main.getGame().timerManager.getTimerState() != TimerState.INACTIVE) {
                        Main.getGame().gameTask.setTimeLeft(1, css.sender)
                    } else {
                        css.sender.sendMessage(Component.text("Unable to skip timer during this state.").color(NamedTextColor.RED))
                    }
                }
            }
        }
    }

    @Command("p")
    @CommandDescription("Allows quick timer pausing.")
    @Permission("meltdown.timer.pause")
    fun quickPause(css: CommandSourceStack) {
        if(Main.getGame().timerManager.getTimerState() != TimerState.ACTIVE) {
            css.sender.sendMessage(Component.text("Unable to pause timer when timer is not active.").color(NamedTextColor.RED))
        } else {
            Main.getGame().timerManager.setTimerState(TimerState.PAUSED)
            Main.getGame().dev.parseDevMessage("Timer state updated to ${Main.getGame().timerManager.getTimerState()} by ${css.sender.name}.", DevStatus.INFO)
        }
    }

    @Command("s")
    @CommandDescription("Allows quick timer resuming.")
    @Permission("meltdown.timer.resume")
    fun quickResume(css: CommandSourceStack) {
        if(Main.getGame().timerManager.getTimerState() != TimerState.PAUSED) {
            css.sender.sendMessage(Component.text("Unable to resume timer when already active.").color(NamedTextColor.RED))
        } else {
            Main.getGame().timerManager.setTimerState(TimerState.ACTIVE)
            Main.getGame().dev.parseDevMessage("Timer state updated to ${Main.getGame().timerManager.getTimerState()} by ${css.sender.name}.", DevStatus.INFO)
        }
    }
}

enum class TimerOptions {
    SET,
    PAUSE,
    RESUME,
    SKIP
}
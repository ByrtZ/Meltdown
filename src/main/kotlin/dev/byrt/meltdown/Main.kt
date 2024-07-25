package dev.byrt.meltdown

import dev.byrt.meltdown.command.BaseCommand
import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.manager.WhitelistGroup
import dev.byrt.meltdown.plugin.PluginMessenger

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.extra.confirmation.CommandConfirmationManager
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.meta.SimpleCommandMeta
import cloud.commandframework.paper.PaperCommandManager

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.Messenger

import org.reflections.Reflections

import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

private lateinit var game : Game
private lateinit var messenger : Messenger

class Main : JavaPlugin() {
    override fun onEnable() {
        logger.info("Starting Meltdown plugin...")
        game = Game(this)
        game.setup()
        setupCommands()
        setupEventListeners()
        setupConfigs()
        setupPluginMessageListener()
        game.lobbyFishing.setup()
    }

    override fun onDisable() {
        logger.info("Cleaning up Meltdown plugin...")
        game.cleanUp()
    }

    private fun setupCommands() {
        val commandManager : PaperCommandManager<CommandSender> = try {
            PaperCommandManager.createNative(
                this,
                CommandExecutionCoordinator.simpleCoordinator()
            )
        } catch (e : Exception) {
            logger.severe("Failed to initialize the command manager.")
            server.pluginManager.disablePlugin(this)
            return
        }

        commandManager.registerAsynchronousCompletions()
        commandManager.registerBrigadier()
        setupCommandConfirmation(commandManager)

        // Thanks broccolai <3 https://github.com/broccolai/tickets/commit/e8c227abc298d1a34094708a24601d006ec25937
        commandManager.commandSuggestionProcessor { context, strings ->
            var input : String = if (context.inputQueue.isEmpty()) {
                ""
            } else {
                context.inputQueue.peek()
            }
            input = input.lowercase(Locale.getDefault())
            val suggestions : MutableList<String> = LinkedList()
            for(suggestion in strings) {
                val suggestionLower = suggestion.lowercase(Locale.getDefault())
                if(suggestionLower.startsWith(input)) {
                    suggestions.add(suggestion)
                }
            }
            suggestions
        }

        val reflections = Reflections("dev.byrt.meltdown.command")
        val commands = reflections.getSubTypesOf(BaseCommand::class.java)

        val annotationParser = AnnotationParser(
            commandManager,
            CommandSender::class.java
        ) { SimpleCommandMeta.empty() }

        commands.forEach { command ->
            run {
                val instance = command.getConstructor().newInstance()
                annotationParser.parse(instance)
            }
        }
    }

    private fun setupCommandConfirmation(commandManager : PaperCommandManager<CommandSender>) {
        try {
            val confirmationManager : CommandConfirmationManager<CommandSender> = CommandConfirmationManager(
                30L, TimeUnit.SECONDS,
                { context -> context.commandContext.sender.sendMessage(Component.text("Confirm command ", NamedTextColor.RED).append(Component.text("'/${context.command}' ", NamedTextColor.GREEN)).append(Component.text("by running ", NamedTextColor.RED)).append(Component.text("'/confirm' ", NamedTextColor.YELLOW)).append(Component.text("to execute.", NamedTextColor.RED))) },
                { sender -> sender.sendMessage(Component.text("You do not have any pending commands.", NamedTextColor.RED)) }
            )
            confirmationManager.registerConfirmationProcessor(commandManager)

            commandManager.command(commandManager.commandBuilder("confirm")
                .meta(CommandMeta.DESCRIPTION, "Confirm a pending command.")
                .handler(confirmationManager.createConfirmationExecutionHandler())
                .permission("meltdown.confirm"))

        } catch (e : Exception) {
            logger.severe("Failed to initialize command confirmation manager.")
            return
        }
    }

    private fun setupEventListeners() {
        val reflections = Reflections("dev.byrt.meltdown.event")
        val listeners = reflections.getSubTypesOf(Listener::class.java)

        listeners.forEach(Consumer { listener : Class<out Listener> ->
            try {
                val instance = listener.getConstructor().newInstance()
                server.pluginManager.registerEvents(instance, this)
            } catch (e : java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        )
    }

    private fun setupPluginMessageListener() {
        messenger = Bukkit.getMessenger()
        messenger.registerIncomingPluginChannel(this, "minecraft:brand", PluginMessenger())
    }

    private fun setupConfigs() {
        game.configManager.setup()
        game.whitelistManager.setWhitelist(WhitelistGroup.ADMIN)
    }

    companion object {
        fun getPlugin(): Plugin { return Bukkit.getPluginManager().getPlugin("Meltdown") as Plugin }
        fun getGame(): Game { return game }
        fun getMessenger(): Messenger { return messenger }
    }
}
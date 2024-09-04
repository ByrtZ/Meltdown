package dev.byrt.meltdown

import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.manager.WhitelistGroup
import dev.byrt.meltdown.plugin.PluginMessenger

import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.Messenger

import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.description.CommandDescription
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.PaperCommandManager
import org.incendo.cloud.processors.cache.SimpleCache
import org.incendo.cloud.processors.confirmation.ConfirmationConfiguration
import org.incendo.cloud.processors.confirmation.ConfirmationManager
import org.incendo.cloud.processors.confirmation.annotation.ConfirmationBuilderModifier

import org.reflections.Reflections

import java.time.Duration
import java.util.function.Consumer

private lateinit var game : Game
private lateinit var messenger : Messenger

@Suppress("unstableApiUsage")
class Main : JavaPlugin() {
    private lateinit var commandManager: PaperCommandManager<CommandSourceStack>
    private lateinit var annotationParser: AnnotationParser<CommandSourceStack>

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
        logger.info("Registering commands.")
        commandManager = PaperCommandManager.builder()
            .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
            .buildOnEnable(this)

        annotationParser = AnnotationParser(commandManager,  CommandSourceStack::class.java)
        annotationParser.parseContainers()

        setupCommandConfirmation()
    }

    private fun setupCommandConfirmation() {
        logger.info("Setting up command confirmation.")
        val confirmationConfig = ConfirmationConfiguration.builder<CommandSourceStack>()
            .cache(SimpleCache.of())
            .noPendingCommandNotifier { css ->
                css.sender.sendMessage(
                    Component.text(
                        "You do not have any pending commands.",
                        NamedTextColor.RED
                    )
                ) }
            .confirmationRequiredNotifier { css, ctx ->
                css.sender.sendMessage(
                    Component.text("Confirm command ", NamedTextColor.RED).append(
                        Component.text("'/${ctx.commandContext()}' ", NamedTextColor.GREEN)
                    ).append(Component.text("by running ", NamedTextColor.RED)).append(
                        Component.text("'/confirm' ", NamedTextColor.YELLOW)
                    ).append(Component.text("to execute.", NamedTextColor.RED))
                ) }
            .expiration(Duration.ofSeconds(30))
            .build()

        val confirmationManager = ConfirmationManager.confirmationManager(confirmationConfig)
        commandManager.registerCommandPostProcessor(confirmationManager.createPostprocessor())

        commandManager.command(
            commandManager.commandBuilder("confirm")
                .handler(confirmationManager.createExecutionHandler())
                .commandDescription(CommandDescription.commandDescription("Confirm a pending command."))
                .permission("meltdown.confirm")
                .build()
        )
        ConfirmationBuilderModifier.install(annotationParser)
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
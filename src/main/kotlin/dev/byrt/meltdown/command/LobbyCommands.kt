package dev.byrt.meltdown.command

import dev.byrt.meltdown.Main
import dev.byrt.meltdown.game.GameState
import dev.byrt.meltdown.lobby.LobbyCustomItem
import dev.byrt.meltdown.util.DevStatus

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import com.destroystokyo.paper.profile.PlayerProfile
import com.destroystokyo.paper.profile.ProfileProperty

import com.google.gson.JsonObject
import com.google.gson.JsonParser

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.util.Vector

import java.io.InputStreamReader
import java.net.URL

@Suppress("unused")
class LobbyCommands : BaseCommand {
    @CommandMethod("lobby secret good_morning_bats")
    @CommandDescription("Lobby command to stop all instances of the lobby secret.")
    @CommandPermission("meltdown.lobby")
    fun lobbyStartSecret(sender : Player) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            Main.getGame().dev.parseDevMessage("Lobby secret task started by ${sender.name}.", DevStatus.INFO)
            Main.getGame().lobbySecret.beginLobbySecret()
        }
    }

    @CommandMethod("lobby secret night_night_bats")
    @CommandDescription("Lobby command to stop all instances of the lobby secret.")
    @CommandPermission("meltdown.lobby")
    fun lobbyStopSecret(sender : Player) {
        Main.getGame().dev.parseDevMessage("Lobby secret task cancelled by ${sender.name}.", DevStatus.INFO)
        Main.getGame().lobbySecret.reset()
    }

    @CommandMethod("lobby item <item> <player>")
    @CommandDescription("Lobby command to get a custom item.")
    @CommandPermission("meltdown.lobby")
    fun lobbyCustomItem(sender : Player, @Argument("item") item : LobbyCustomItem, @Argument("player") player : Player) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            Main.getGame().dev.parseDevMessage("${sender.name} gave ${player.name} one ${item}.", DevStatus.INFO)
            when(item) {
                LobbyCustomItem.ROCKET_LAUNCHER -> {
                    Main.getGame().lobbyItems.createRocketLauncher(player)
                }
                LobbyCustomItem.LIGHTNING_WAND -> {
                    Main.getGame().lobbyItems.createLightningWand(player)
                }
                LobbyCustomItem.TELEPORT_SPOON -> {
                    Main.getGame().lobbyItems.createTeleportSpoon(player)
                }
            }
        }
    }

    @CommandMethod("smite all")
    @CommandDescription("Lobby command to perform a smite effect on all online players.")
    @CommandPermission("meltdown.lobby")
    fun lobbySmiteAll(sender : Player) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            Main.getGame().dev.parseDevMessage("${sender.name} smote everyone.", DevStatus.INFO)
            for(player in Bukkit.getOnlinePlayers()) {
                smitePlayer(player)
            }
        }
    }

    @CommandMethod("smite <player>")
    @CommandDescription("Lobby command to perform a smite effect on a specified player.")
    @CommandPermission("meltdown.lobby")
    fun lobbySmitePlayer(sender : Player, @Argument("player") player : Player) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            Main.getGame().dev.parseDevMessage("${sender.name} smote ${player.name}.", DevStatus.INFO)
            smitePlayer(player)
        }
    }

    private fun smitePlayer(player : Player) {
        player.location.world.strikeLightningEffect(player.location)
        player.location.world.spawnParticle(Particle.CLOUD, player.location, 10, 0.0, 0.0, 0.0, 0.5)
        player.velocity = Vector(player.velocity.x, 1.5, player.velocity.z)
    }

    @CommandMethod("setskin <player> <skin>")
    @CommandDescription("Allows skin modification.")
    @CommandPermission("meltdown.setskin")
    fun skin(sender : Player, @Argument("player") player : Player, @Argument("skin") skin : String) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            try {
                setPlayerSkin(player, skin)
                if(player.name == skin) {
                    Main.getGame().dev.parseDevMessage("${sender.name} reset ${player.name}'s skin.", DevStatus.INFO)
                } else {
                    Main.getGame().dev.parseDevMessage("${sender.name} changed ${player.name}'s skin to ${skin}'s skin.", DevStatus.INFO)
                }
            } catch(e : Exception) {
                sender.sendMessage(Component.text("An error occurred when attempting to change a player's skin.").color(NamedTextColor.RED))
                e.printStackTrace()
            }
        }
    }

    private fun setPlayerSkin(playerToChange : Player, playerSkinToGrabName : String) {
        try {
            val uuid = URL("https://api.mojang.com/users/profiles/minecraft/${playerSkinToGrabName}")
            val userReader = InputStreamReader(uuid.openStream())
            val id = JsonParser.parseReader(userReader).asJsonObject.get("id").asString
            val profile: PlayerProfile = Bukkit.createProfile(playerToChange.uniqueId, id)
            val mojang = URL("https://sessionserver.mojang.com/session/minecraft/profile/$id?unsigned=false")
            val reader = InputStreamReader(mojang.openStream())
            val textureProperty : JsonObject = JsonParser.parseReader(reader).asJsonObject.get("properties").asJsonArray.get(0).asJsonObject
            if (textureProperty.get("value").asString != null && textureProperty.get("signature").asString != null) {
                val texture = textureProperty.get("value").asString
                val signature = textureProperty.get("signature").asString
                profile.clearProperties()
                profile.setProperty(ProfileProperty("textures", texture, signature))
                playerToChange.playerProfile = profile
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
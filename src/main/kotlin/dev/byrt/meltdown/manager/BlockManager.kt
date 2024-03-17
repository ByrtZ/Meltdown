package dev.byrt.meltdown.manager

import dev.byrt.meltdown.data.CoinCrateLocation
import dev.byrt.meltdown.game.Game
import dev.byrt.meltdown.state.Sounds
import dev.byrt.meltdown.state.Teams
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World

class BlockManager(private val game : Game) {
    fun entranceOpenStageOne(team : Teams, world : World, x1 : Int, x2 : Int, startingY : Int, z1 : Int, z2 : Int) {
        for(x in x1..x2) {
            for(z in z1..z2) {
                for(y in startingY..startingY + 1) {
                    removeGlassBlock(world, x, y, z, team)
                }
            }
        }
    }

    fun entranceOpenStageTwo(team : Teams, world : World, x1 : Int, x2 : Int, startingY : Int, z1 : Int, z2 : Int) {
        for(x in x1..x2) {
            for(z in z1..z2) {
                for(y in startingY + 2..startingY + 2) {
                    removeGlassBlock(world, x, y, z, team)
                }
            }
        }
    }

    fun entranceOpenStageThree(team : Teams, world : World, x1 : Int, x2 : Int, startingY : Int, z1 : Int, z2 : Int) {
        for(x in x1..x2) {
            for(z in z1..z2) {
                for(y in startingY + 3..startingY + 4) {
                    removeGlassBlock(world, x, y, z, team)
                }
            }
        }
    }

    private fun removeGlassBlock(world : World, x : Int, y : Int, z : Int, team : Teams) {
        if(world.getBlockAt(x, y, z).type == getTeamGlass(team)) {
            world.getBlockAt(x, y, z).type = Material.AIR
        }
    }

    private fun getTeamGlass(team : Teams) : Material {
        return when(team) {
            Teams.RED -> {
                Material.RED_STAINED_GLASS
            }
            Teams.YELLOW -> {
                Material.YELLOW_STAINED_GLASS
            }
            Teams.LIME -> {
                Material.LIME_STAINED_GLASS
            }
            Teams.BLUE -> {
                Material.BLUE_STAINED_GLASS
            }
            Teams.SPECTATOR -> {
                Material.GRAY_STAINED_GLASS
            }
        }
    }

    fun resetEntrance(team : Teams, corner1 : Location, corner2 : Location) {
        for(x in corner1.blockX.. corner2.blockX) {
            for(y in corner1.blockY.. corner2.blockY) {
                for(z in corner1.blockZ.. corner2.blockZ) {
                    game.locationManager.getWorld().getBlockAt(x, y, z).type = getTeamGlass(team)
                }
            }
        }
    }

    fun setCoinCrates(material : Material) {
        for(coinCrate in game.coinCrateManager.getCoinCrates()) {
            for(x in coinCrate.corner1.blockX..coinCrate.corner2.blockX) {
                for(y in coinCrate.corner1.blockY..coinCrate.corner2.blockY) {
                    for(z in coinCrate.corner1.blockZ..coinCrate.corner2.blockZ) {
                        if(game.locationManager.getWorld().getBlockAt(x, y, z).type == Material.AIR) {
                            game.locationManager.getWorld().getBlockAt(x, y, z).type = material
                        }
                    }
                }
            }
        }
    }

    fun setCoinCratesBarriers(material : Material) {
        for(coinCrateBarrier in game.coinCrateManager.getCoinCrateBarriers()) {
            if(coinCrateBarrier.coinCrateLocation == CoinCrateLocation.CORNER) {
                for(x in coinCrateBarrier.corner1.blockX..coinCrateBarrier.corner2.blockX) {
                    for(y in coinCrateBarrier.corner1.blockY..coinCrateBarrier.corner2.blockY) {
                        for(z in coinCrateBarrier.corner1.blockZ..coinCrateBarrier.corner2.blockZ) {
                            if(game.locationManager.getWorld().getBlockAt(x, y, z).type != Material.RAW_GOLD_BLOCK) {
                                game.locationManager.getWorld().getBlockAt(x, y, z).type = material
                            }
                        }
                    }
                }
            }
        }
        if(material == Material.AIR) {
            for(player in Bukkit.getOnlinePlayers()) {
                player.playSound(player.location, Sounds.Alert.GENERAL_ALERT, 1f, 1f)
                player.sendMessage(
                    Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text("Coin crates are now accessible!", NamedTextColor.AQUA, TextDecoration.BOLD)
                    )
                )
            }
        }
    }

    fun setCentreCoinCrateBarriers(material : Material) {
        for(coinCrateBarrier in game.coinCrateManager.getCoinCrateBarriers()) {
            if(coinCrateBarrier.coinCrateLocation == CoinCrateLocation.CENTRE) {
                for(x in coinCrateBarrier.corner1.blockX..coinCrateBarrier.corner2.blockX) {
                    for(y in coinCrateBarrier.corner1.blockY..coinCrateBarrier.corner2.blockY) {
                        for(z in coinCrateBarrier.corner1.blockZ..coinCrateBarrier.corner2.blockZ) {
                            if(game.locationManager.getWorld().getBlockAt(x, y, z).type != Material.RAW_GOLD_BLOCK) {
                                game.locationManager.getWorld().getBlockAt(x, y, z).type = material
                            }
                        }
                    }
                }
            }
        }
        if(material == Material.AIR) {
            for(player in Bukkit.getOnlinePlayers()) {
                player.playSound(player.location, Sounds.Alert.GENERAL_ALERT, 1f, 1f)
                player.sendMessage(
                    Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text("Central coin crate is now accessible!", NamedTextColor.AQUA, TextDecoration.BOLD)
                    )
                )
            }
        }
    }
}
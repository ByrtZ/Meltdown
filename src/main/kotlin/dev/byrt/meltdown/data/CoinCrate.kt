package dev.byrt.meltdown.data

import org.bukkit.Location

data class CoinCrate(val id : Int, val corner1 : Location, val corner2 : Location, val coinCrateLocation : CoinCrateLocation)

data class CoinCrateBarrier(val id : Int, val corner1 : Location, val corner2 : Location, val coinCrateLocation : CoinCrateLocation)

enum class CoinCrateLocation {
    CORNER,
    CENTRE
}

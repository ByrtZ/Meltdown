package dev.byrt.meltdown.arena

import dev.byrt.meltdown.data.CoinCrate
import dev.byrt.meltdown.data.CoinCrateBarrier
import dev.byrt.meltdown.data.CoinCrateLocation
import dev.byrt.meltdown.game.Game

import org.bukkit.Location

class CoinCrateManager(private val game : Game) {
    private var coinCrateList = ArrayList<CoinCrate>()
    private var latestCoinCrateId = 0

    private var coinCrateBarrierList = ArrayList<CoinCrateBarrier>()
    private var latestCoinCrateBarrierId = 0

    fun populateCoinCrates() {
        game.plugin.logger.info("Populating coin crates..")
        /**Corner crate**/
        addCoinCrate(
            CoinCrate(
                incrementCoinCrateId(),
                Location(game.locationManager.getWorld(), -1948.0, 70.0, -2053.0),
                Location(game.locationManager.getWorld(), -1946.0, 71.0, -2051.0),
                CoinCrateLocation.CORNER
            )
        )
        /**Corner crate**/
        addCoinCrate(
            CoinCrate(
                incrementCoinCrateId(),
                Location(game.locationManager.getWorld(), -2054.0, 70.0, -1949.0),
                Location(game.locationManager.getWorld(), -2052.0, 71.0, -1947.0),
                CoinCrateLocation.CORNER
            )
        )
        /**Centre crate**/
        addCoinCrate(
            CoinCrate(
                incrementCoinCrateId(),
                Location(game.locationManager.getWorld(), -2001.0, 70.0, -2001.0),
                Location(game.locationManager.getWorld(), -1999.0, 71.0, -1999.0),
                CoinCrateLocation.CENTRE
            )
        )
    }

    private fun addCoinCrate(coinCrate : CoinCrate) {
        coinCrateList.add(coinCrate)
    }

    private fun removeCoinCrate(coinCrate : CoinCrate) {
        coinCrateList.remove(coinCrate)
    }

    private fun incrementCoinCrateId() : Int {
        return latestCoinCrateId++
    }

    fun populateCoinCrateBarriers() {
        game.plugin.logger.info("Populating coin crate barriers..")
        /**Corner crate barrier**/
        addCoinCrateBarrier(
            CoinCrateBarrier(
                incrementCoinCrateBarrierId(),
                Location(game.locationManager.getWorld(), -1949.0, 70.0, -2054.0),
                Location(game.locationManager.getWorld(), -1945.0, 72.0, -2050.0),
                CoinCrateLocation.CORNER
            )
        )
        /**Corner crate barrier**/
        addCoinCrateBarrier(
            CoinCrateBarrier(
                incrementCoinCrateBarrierId(),
                Location(game.locationManager.getWorld(), -2055.0, 70.0, -1950.0),
                Location(game.locationManager.getWorld(), -2051.0, 72.0, -1946.0),
                CoinCrateLocation.CORNER
            )
        )
        /**Centre crate barrier**/
        addCoinCrateBarrier(
            CoinCrateBarrier(
                incrementCoinCrateBarrierId(),
                Location(game.locationManager.getWorld(), -2002.0, 70.0, -2002.0),
                Location(game.locationManager.getWorld(), -1998.0, 72.0, -1998.0),
                CoinCrateLocation.CENTRE
            )
        )
    }

    private fun addCoinCrateBarrier(coinCrateBarrier : CoinCrateBarrier) {
        coinCrateBarrierList.add(coinCrateBarrier)
    }

    private fun removeCoinCrateBarrier(coinCrateBarrier : CoinCrateBarrier) {
        coinCrateBarrierList.remove(coinCrateBarrier)
    }

    private fun incrementCoinCrateBarrierId() : Int {
        return latestCoinCrateBarrierId++
    }

    fun getCoinCrates() : ArrayList<CoinCrate> {
        return coinCrateList
    }

    fun getCoinCrateBarriers() : ArrayList<CoinCrateBarrier> {
        return coinCrateBarrierList
    }
}
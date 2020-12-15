package day15

// https://adventofcode.com/2020/day/15

import getFileFromArgs

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)
    val seed = file.readLines().first().split((",")).map { it.toLong() }.toList()

    val game = Game(seed)
    game.playRounds(30000000)

    println(game.lastShout)
}

class Game(private val seed: List<Long>) {

    var lastShout = seed.last()

    private val shoutedNumbers: MutableMap<Long, LastShout> = seed
            .mapIndexed { index, seedValue -> seedValue to LastShout(index + 1L) }
            .toMap().toMutableMap()

    fun playRounds(maxRound: Long) {
        for (currentRound in (seed.size + 1)..maxRound) {
            processNext(currentRound)
        }
    }

    private fun processNext(round: Long) {
        val nextShout = shoutedNumbers[lastShout]!!.getNextShout()

        shoutedNumbers[nextShout]?.setLastShout(round)
                ?: run { shoutedNumbers[nextShout] = LastShout(round) }

        lastShout = nextShout
    }
}

data class LastShout(
        private var lastShout: Long,
        private var wasFirst: Boolean = true,
        private var previousShout: Long = 0
) {
    fun setLastShout(shout: Long) {
        wasFirst = false
        previousShout = lastShout
        lastShout = shout
    }

    fun getNextShout() = if (wasFirst) 0L else lastShout - previousShout
}
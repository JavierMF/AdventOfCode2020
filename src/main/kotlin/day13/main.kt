package day13

// https://adventofcode.com/2020/day/13

import getFileFromArgs
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)

    val lines = file.readLines()
    //problem1(lines)

    val rules = lines.last().split(",")
            .mapIndexedNotNull { index, value -> if (value == "x") null else Pair(value.toLong(), index) }

    //problem2bruteforce(first, buses)

    var candidateTimestamp = 0L
    var increment = 1L
    for (i in 2..rules.size) {
        val busesSublist = rules.subList(0, i)

        do {
            candidateTimestamp += increment
            val finished = busesSublist.all { ((candidateTimestamp + it.second) % it.first) == 0L }
        } while (!finished)
        increment = busesSublist.map { it.first }.fold(1) { a, b -> a * b }
    }
    println(candidateTimestamp)
}

private fun problem2bruteforce(first: Long, buses: List<Pair<Long, Int>>) {
    var x = 0L
    var finished: Boolean
    do {
        x += 1
        val candidateTimestamp = first * x
        finished = buses.all { ((candidateTimestamp + it.second) % it.first) == 0L }
    } while (!finished)

    println(x * first)
}

private fun problem1(lines: List<String>) {
    val referenceTimestamp = lines.first().toLong()
    val min = lines.last().split(",")
            .mapNotNull {
                if (it == "x") null else {
                    val busId = it.toLong()
                    Pair(busId, busId * ((referenceTimestamp / busId) + 1))
                }
            }.minByOrNull { it.second } ?: exitProcess(-1)
    val waiting = min.second - referenceTimestamp
    println(min.first * waiting)
}
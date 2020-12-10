package day10

// https://adventofcode.com/2020/day/10

import getFileFromArgs

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)

    val fileNumbers = file.readLines().map { it.toLong() }.sorted()
    val numbers = listOf(0L) + fileNumbers + (fileNumbers.last() + 3)
    problem1(numbers)
    problem2(numbers)
}

fun problem2(numbers: List<Long>) {
    val map = numbers.map { it to Node(it) }.toMap()
    map.forEach {
        it.key.nexts(numbers).forEach { next ->
            it.value.addNext(map[next]!!)
        }
    }
    val paths = map[numbers.first()]!!.paths()
    println(paths)
}

data class Node(val value: Long) {
    val next = mutableSetOf<Node>()
    var paths: Long? = null

    fun addNext(n: Node) {
        next.add(n)
    }

    fun paths(): Long {
        if (next.isEmpty()) return 1L
        if (paths == null) paths = next.map { it.paths() }.sum()
        return paths!!
    }
}

private fun Long.nexts(numbers: List<Long>): List<Long> =
        ((this + 1)..(this + 3)).filter { it in numbers }.filter { it.next(numbers) != null }

private fun Long.next(numbers: List<Long>): Long? =
        ((this + 1)..(this + 3)).firstOrNull { it in numbers }

private fun problem1(numbers: List<Long>) {
    var diff1 = 0
    var diff3 = 0
    numbers.forEachIndexed { index, current ->
        run {
            if (index != 0) {
                when (current - numbers[index - 1]) {
                    3L -> diff3 += 1
                    1L -> diff1 += 1
                }
            }
        }
    }

    println("${diff1 * diff3}")
}

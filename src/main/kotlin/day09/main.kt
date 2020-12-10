package day09

// https://adventofcode.com/2020/day/9

import getFileFromArgs
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)

    val preamble = 25
    val numbers = file.readLines().map { it.toLong() }

    val result1 = problem1(preamble, numbers)
    if (result1 != null) println(result1) else {
        println("Not found"); exitProcess(-1)
    }

    var index = numbers.size
    do {
        val result2 = sumContiguous(result1, numbers.takeLast(index))
        if (result2 != null) {
            println("${result2.first + result2.second}"); exitProcess(0)
        }
        index -= 1
    } while (index > 0)
    println("Not found")
}

private fun sumContiguous(target: Long, number: List<Long>): Pair<Long, Long>? {
    var sum = 0L
    var index = 0
    while (sum < target) {
        sum += number[index]
        if (sum == target) {
            val sumNumbers = number.subList(0, index)
            return Pair(sumNumbers.minOrNull()!!, sumNumbers.maxOrNull()!!)
        }
        index += 1
    }
    return null
}

private fun problem1(preamble: Int, numbers: List<Long>): Long? {
    var index = preamble
    do {
        val candidates = numbers.subList(index - preamble, index)
        if (!hasASum(numbers[index], candidates)) {
            return numbers[index]
        }
        index += 1
    } while (index < numbers.size)

    return null
}

fun hasASum(target: Long, candidates: List<Long>): Boolean {
    candidates.forEach {
        val desired = target - it
        if (desired in candidates && (desired * 2) != target) return true
    }
    return false
}

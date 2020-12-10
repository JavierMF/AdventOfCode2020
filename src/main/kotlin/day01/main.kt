package day01

// https://adventofcode.com/2020/day/1

import getFileFromArgs
import kotlin.system.exitProcess

private const val target = 2020

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)
    val allNumbers = file.readLines().map { it.toInt() }

    problem1(allNumbers)
    problem2(allNumbers)
}

private fun problem2(allNumbers: List<Int>) {
    allNumbers.forEach {
        val desiredNumber = target - it
        val pair = allNumbers.findTwoThatSum(desiredNumber)
        if (pair != null) {
            println("$it * ${pair.first} * ${pair.second} = ${it * pair.first * pair.second}")
            exitProcess(0)
        }
    }
}

private fun problem1(numbers: List<Int>) {
    val pair = numbers.findTwoThatSum(target)
    if (pair != null)
        println("${pair.first} * ${pair.second} = ${pair.first * pair.second}")
    else
        println("No pair found")
}

private fun List<Int>.findTwoThatSum(target: Int): Pair<Int, Int>? {
    for (value in this) {
        val desiredValue = target - value
        if (this.contains(desiredValue)) {
            return Pair(value, desiredValue)
        }
    }
    return null
}

package day12

// https://adventofcode.com/2020/day/12

import getFileFromArgs
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)

    val shipPosition = ShipPosition()
    file.readLines()
            .map { Instruction(it) }
            .forEach { shipPosition.apply(it) }
    val coordinates = shipPosition.coords()

    println("${coordinates.first + coordinates.second}")
}

class ShipPosition {
    private var x = 0
    private var y = 0
    private var wpX = 10
    private var wpY = 1

    fun apply(inst: Instruction) {
        when (inst.action) {
            'N' -> moveWaypoint(Direction.North, inst.value)
            'S' -> moveWaypoint(Direction.South, inst.value)
            'E' -> moveWaypoint(Direction.East, inst.value)
            'W' -> moveWaypoint(Direction.West, inst.value)
            'F' -> moveShip(inst.value)
            'L' -> turnWaypoint(-1, inst.value)
            'R' -> turnWaypoint(1, inst.value)
            else -> throw IllegalArgumentException()
        }
    }

    private fun moveWaypoint(dir: Direction, value: Int) {
        wpX += dir.dirX * value
        wpY += dir.dirY * value
    }

    private fun moveShip(value: Int) {
        x += wpX * value
        y += wpY * value
    }

    private fun turnWaypoint(dir: Int, value: Int) {
        val angle = (360 + (dir * value)) % 360

        if (angle == 90 || angle == 270) wpX = wpY.also { wpY = wpX } // swap

        val sign = when (angle) {
            90 -> Pair(1, -1)
            180 -> Pair(-1, -1)
            270 -> Pair(-1, 1)
            else -> Pair(1, 1)
        }

        wpX *= sign.first
        wpY *= sign.second
    }

    fun coords() = Pair(x.absoluteValue, y.absoluteValue)
}

class Instruction(line: String) {
    val action: Char = line.first()
    val value: Int = line.substring(1).toInt()
}

enum class Direction(val dirX: Int, val dirY: Int) {
    North(0, 1),
    East(1, 0),
    South(0, -1),
    West(-1, 0)
}
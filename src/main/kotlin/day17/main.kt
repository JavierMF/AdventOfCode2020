package day17

// https://adventofcode.com/2020/day/17

import getFileFromArgs

fun main(args: Array<String>) {
    val lines = getFileFromArgs(args).readLines()

    val config = buildConfig(lines)
    repeat(6) { config.nextCycle() }

    println(config.activeCubes())
}

fun buildConfig(lines: List<String>) = CubesConfig(
        lines.flatMapIndexed { y, line ->
            line.mapIndexed { x, char -> Cube(Position(x, y, 0, 0), char == '#') }
        }
)

class CubesConfig(cubes: List<Cube>) {

    var cubesMap = cubes.map { it.position to it }.toMap().toMutableMap()

    fun nextCycle() {
        val nextCubesMap = mutableMapOf<Position, Cube>()
        cubesMap.keys.forEach {
            it.neighbourPositions()
                    .filterNot { nextCubesMap.containsKey(it) }
                    .map { Cube(it, it.isActive()) }
                    .forEach { nextCubesMap.put(it.position, it) }
        }
        cubesMap = nextCubesMap
    }

    private fun Position.isActive(): Boolean {
        val activeNeighbours = this.neighbourPositions().count { cubesMap[it]?.isActive ?: false }

        return when (cubesMap[this]?.isActive ?: false) {
            true -> activeNeighbours in 2..3
            false -> activeNeighbours == 3
        }
    }

    fun activeCubes(): Int = cubesMap.values.count { it.isActive }
}

data class Cube(val position: Position, var isActive: Boolean)

data class Position(val x: Int, val y: Int, val z: Int, val w: Int) {
    fun neighbourPositions(): List<Position> {
        val neighbours = mutableListOf<Position>()
        for (newX in x - 1..x + 1) {
            for (newY in y - 1..y + 1) {
                for (newZ in z - 1..z + 1) {
                    for (newW in w - 1..w + 1)
                        neighbours.add(Position(newX, newY, newZ, newW))
                }
            }
        }
        neighbours.remove(Position(x, y, z, w))
        return neighbours
    }
}
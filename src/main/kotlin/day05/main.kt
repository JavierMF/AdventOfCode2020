package day05

// https://adventofcode.com/2020/day/5

import getFileFromArgs
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)

    problem1(file)
    problem2(file)
}

private fun problem2(file: File) {
    val passes = file.readLines().map { BoardingPass(it) }

    val seats = passes.map { Pair(it.row, it.column) to it }.toMap()
    val ids = passes.map { it.id }.toSet()

    for (row in 1 until BoardingPass.maxRow) {
        for (column in 0..BoardingPass.maxColumn) {
            val candidate = Pair(row, column)
            if (!seats.containsKey(candidate)) {
                val candidateId = (row * 8L) + column
                if (ids.contains(candidateId - 1) && ids.contains(candidateId + 1)) {
                    println(candidateId)
                    exitProcess(0)
                }
            }
        }
    }
    println("Not found")
}

private fun problem1(file: File) {
    val maxId = file.readLines()
            .map { BoardingPass(it).id }
            .maxOrNull()

    println(maxId)
}

data class BoardingPass(val code: String) {
    val row: Int
    val column: Int
    val id: Long

    init {
        var position = Position(Pair(0, maxRow), Pair(0, maxColumn))
        code.forEach { position = position.next(it) }
        row = position.row.first
        column = position.column.first
        id = (row * 8L) + column
    }

    companion object {
        const val maxColumn: Int = 7
        const val maxRow: Int = 127
    }

}

data class Position(val row: Pair<Int, Int>, val column: Pair<Int, Int>) {
    fun columnLowerHalf(): Position {
        val newColumn = Pair(column.first, column.first + column.halfLength())
        return Position(row, newColumn)
    }

    fun columnUpperHalf(): Position {
        val newColumn = Pair(column.first + column.halfLength() + 1, column.second)
        return Position(row, newColumn)
    }

    fun rowLowerHalf(): Position {
        val newRow = Pair(row.first, row.first + row.halfLength())
        return Position(newRow, column)
    }

    fun rowUpperHalf(): Position {
        val newRow = Pair(row.first + row.halfLength() + 1, row.second)
        return Position(newRow, column)
    }

    fun next(it: Char): Position {
        when (it) {
            'F' -> return this.rowLowerHalf()
            'B' -> return this.rowUpperHalf()
            'R' -> return this.columnUpperHalf()
            'L' -> return this.columnLowerHalf()
        }
        return this
    }
}

private fun Pair<Int, Int>.halfLength() = (this.second - this.first) / 2

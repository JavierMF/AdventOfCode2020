package day03

// https://adventofcode.com/2020/day/3

import getFileFromArgs

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)

    val treesMap = file.readLines()
    val maxRowLength = treesMap.first().length

    val navigators = listOf(
            Navigator(rightMoves = 1, downMoves = 1, rowLength = maxRowLength),
            Navigator(rightMoves = 3, downMoves = 1, rowLength = maxRowLength),
            Navigator(rightMoves = 5, downMoves = 1, rowLength = maxRowLength),
            Navigator(rightMoves = 7, downMoves = 1, rowLength = maxRowLength),
            Navigator(rightMoves = 1, downMoves = 2, rowLength = maxRowLength),
    )

    val totalTrees = navigators.map { findTrees(treesMap, it) }.reduce { a, b -> a * b }

    println("We found $totalTrees trees")
}

fun findTrees(treesMap: List<String>, navigator: Navigator): Long {
    val maxRows = treesMap.size
    var trees = 0L
    while (navigator.currentRow < maxRows) {
        if (treesMap[navigator.currentRow][navigator.currentColumn] == '#') {
            trees++
        }
        navigator.moveToNext()
    }
    return trees
}

class Navigator(
        private val rowLength: Int,
        private val rightMoves: Int,
        private val downMoves: Int
) {
    fun moveToNext() {
        position = Position(
                row = position.row + downMoves,
                column = (position.column + rightMoves) % rowLength
        )
    }

    private var position = Position()

    val currentRow: Int get() = position.row
    val currentColumn: Int get() = position.column
}

data class Position(
        val row: Int = 0,
        val column: Int = 0
)

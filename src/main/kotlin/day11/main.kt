package day11

// https://adventofcode.com/2020/day/11

import day11.Seat.Companion.seatFromInput
import getFileFromArgs
import java.io.File

enum class SpaceState { EMPTY, OCCUPIED, FLOOR }

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)

    val seatsMap = SeatMapBuilder(file).build()
    seatsMap.evolve()

    println(seatsMap.occupiedSeats())
}

class SeatMap(private var seatsMap: List<List<Seat>>){
    fun evolve() {
        do {
            var anyChange = false
            seatsMap = seatsMap.map {
                it.map { seat ->
                    when (seat.state) {
                        SpaceState.FLOOR -> seat
                        SpaceState.EMPTY -> if (seat.allNeighboursFree(seatsMap)) {
                            anyChange = true
                            seat.withState(SpaceState.OCCUPIED)
                        } else seat
                        SpaceState.OCCUPIED -> if (seat.tooManyNeighbours(seatsMap)) {
                            anyChange = true
                            seat.withState(SpaceState.EMPTY)
                        } else seat
                    }
                }
            }
        } while (anyChange)
    }

    fun occupiedSeats() = seatsMap.flatMap { it.map { if (it.isOccupied()) 1 else 0 } }.sum()
}

data class Seat(val state:SpaceState, val row:Int, val col:Int, val neighbours: Set<Pair<Int,Int>> = emptySet()) {
    fun isOccupied() = state == SpaceState.OCCUPIED
    fun isASeat() = state != SpaceState.FLOOR

    fun withNeighbours(newNeighbours:Set<Pair<Int,Int>>) = Seat(state, row, col, newNeighbours)
    fun withState(newState:SpaceState) = Seat(newState, row, col, neighbours)

    fun allNeighboursFree(seatsMap: List<List<Seat>>) = occupiedNeighboursSeats(seatsMap) == 0
    fun tooManyNeighbours(seatsMap: List<List<Seat>>) = occupiedNeighboursSeats(seatsMap) >= 5
    private fun occupiedNeighboursSeats(seatsMap: List<List<Seat>>) = neighbours.map { seatsMap[it.first][it.second] }.count{it.isOccupied()}

    companion object {
        fun seatFromInput(initialStateChar: Char, row:Int, col:Int): Seat {
            val initialState = when (initialStateChar) {
                '.' -> SpaceState.FLOOR
                'L' -> SpaceState.EMPTY
                '#' -> SpaceState.OCCUPIED
                else -> throw IllegalArgumentException("Invalid char")
            }
            return Seat(initialState, row, col)
        }
    }
}

class NeighboursFinder(private val seatRows: List<List<Seat>>) {
    private val rowsRange = seatRows.indices
    private val colsRange = seatRows.first().indices

    fun forSeat(seat: Seat): Set<Pair<Int, Int>> {
        return directions.mapNotNull { findSeatInDir(seat, it) }.toSet()
    }

    private fun findSeatInDir(seat: Seat, dir: Pair<Int, Int>): Pair<Int,Int>? {
        var candidateCoords = Pair(seat.row, seat.col)
        var candidateSeat: Seat?

        do {

            candidateCoords = Pair(candidateCoords.first + dir.first, candidateCoords.second + dir.second)
            candidateSeat = if (validCoords(candidateCoords)) seatRows[candidateCoords.first][candidateCoords.second] else null

        } while (candidateSeat != null && !candidateSeat.isASeat())

        return if (candidateSeat == null) null else Pair(candidateSeat.row, candidateSeat.col)
    }

    private fun validCoords(coords: Pair<Int, Int>) =
            coords.first in rowsRange && coords.second in colsRange

    companion object {
        val directions = setOf(-1, 0, 1).flatMap { rowDir -> setOf(-1, 0, 1).map { colDir -> rowDir to colDir  } }.filterNot { it == Pair(0,0) }
    }
}

class SeatMapBuilder(val file: File) {

    fun build(): SeatMap {
        val seatRows = file.readLines().mapIndexed { row, line ->
            line.mapIndexed { col, char -> seatFromInput(char, row, col) }
        }
        val neighboursFinder = NeighboursFinder(seatRows)
        val seatsMap = seatRows.map { it.map { it.withNeighbours(neighboursFinder.forSeat(it)) } }
        return SeatMap(seatsMap)
    }
}

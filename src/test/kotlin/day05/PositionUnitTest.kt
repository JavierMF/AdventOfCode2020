package day05

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PositionUnitTest {

    @Test
    fun testRowLowerHalf() {
        val initialPos = Position(Pair(0,127), Pair(0,7))

        val result = initialPos.rowLowerHalf()

        assertEquals(Pair(0,63), result.row)
        assertEquals(Pair(0,7), result.column)
    }

    @Test
    fun testRowUpperHalf() {
        val initialPos = Position(Pair(0,63),Pair(0,7))

        val result = initialPos.rowUpperHalf()

        assertEquals(Pair(32,63), result.row)
        assertEquals(Pair(0,7), result.column)
    }

    @Test
    fun testColumnLowerHalf() {
        val initialPos = Position(Pair(0,127),Pair(4,7))

        val result = initialPos.columnLowerHalf()

        assertEquals(Pair(0,127), result.row)
        assertEquals(Pair(4,5), result.column)
    }

    @Test
    fun testColumnUpperHalf() {
        val initialPos = Position(Pair(0,127),Pair(0,7))

        val result = initialPos.columnUpperHalf()

        assertEquals(Pair(0,127), result.row)
        assertEquals(Pair(4,7), result.column)
    }
}
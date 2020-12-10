package day05

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BoardingPassUnitTest {

    @Test
    fun testOneBoardingPass() {
        val pass = BoardingPass("BFFFBBFRRR")

        assertEquals(70, pass.row)
        assertEquals(7, pass.column)
        assertEquals(567, pass.id)
    }
    @Test
    fun testOneBoardingPass2() {
        val pass = BoardingPass("FFFBBBFRRR")

        assertEquals(14, pass.row)
        assertEquals(7, pass.column)
        assertEquals(119, pass.id)
    }
    @Test
    fun testOneBoardingPass3() {
        val pass = BoardingPass("BBFFBBFRLL")

        assertEquals(102, pass.row)
        assertEquals(4, pass.column)
        assertEquals(820, pass.id)
    }
}
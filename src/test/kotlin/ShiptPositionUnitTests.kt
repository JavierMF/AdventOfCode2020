import day12.Instruction
import day12.ShipPosition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShiptPositionUnitTests {

    @Test
    fun testMoves() {
        val pos = ShipPosition()
        pos.apply(Instruction("F10"))
        pos.apply(Instruction("N3"))
        pos.apply(Instruction("F7"))
        pos.apply(Instruction("R90"))
        pos.apply(Instruction("F11"))

        assertEquals(pos.coords().first, 214)
        assertEquals(pos.coords().second, 72)
    }
}
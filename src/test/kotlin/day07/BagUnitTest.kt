package day07

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BagUnitTest {

    @Test
    fun testBag() {
        val bag = Bag("clear teal bags contain 2 dull cyan bags, 1 wavy cyan bag, 2 light blue bags.")

        assertEquals("clear teal", bag.color)
        assertTrue(bag.canContain("dull cyan"))
        assertTrue(bag.canContain("wavy cyan"))
        assertTrue(bag.canContain("light blue"))
        assertFalse(bag.canContain("wavy maroon"))
    }

    @Test
    fun testBagEmpty() {
        val bag = Bag("dotted black bags contain no other bags.")

        assertEquals("dotted black", bag.color)
        assertFalse(bag.canContain("dull cyan"))
        assertFalse(bag.canContain("wavy cyan"))
        assertFalse(bag.canContain("light blue"))
        assertFalse(bag.canContain("wavy maroon"))
    }
}
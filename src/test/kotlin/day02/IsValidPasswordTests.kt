package day02

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class IsValidPasswordTests {

    @ParameterizedTest
    @ValueSource(strings = [
        "1-3 a: abcde",
        "2-9 c: ccccccccc"
    ])
    fun `valid password`(line : String) = assertTrue(isValidPassword(line))

    @ParameterizedTest
    @ValueSource(strings = [
        "1-3 b: cdefg"
    ])
    fun `invalid password`(line : String) = assertFalse(isValidPassword(line))
}
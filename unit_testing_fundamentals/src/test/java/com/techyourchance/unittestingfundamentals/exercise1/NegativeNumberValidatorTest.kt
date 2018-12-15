package com.techyourchance.unittestingfundamentals.exercise1

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class NegativeNumberValidatorTest {
    private val SUT = NegativeNumberValidator()

    @Test
    fun test1() {
        val result = SUT.isNegative(-1)
        assertTrue(result)
    }

    @Test
    fun test2() {
        val result = SUT.isNegative(0)
        assertFalse(result)
    }

    @Test
    fun test3() {
        val result = SUT.isNegative(1)
        assertFalse(result)
    }
}

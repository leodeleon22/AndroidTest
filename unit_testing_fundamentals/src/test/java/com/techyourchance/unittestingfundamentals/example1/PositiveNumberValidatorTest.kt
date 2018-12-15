package com.techyourchance.unittestingfundamentals.example1

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PositiveNumberValidatorTest {

    private val SUT = PositiveNumberValidator()

    @Test
    fun test1() {
        val result = SUT.isPositive(-1)
        assertFalse(result)
    }

    @Test
    fun test2() {
        val result = SUT.isPositive(0)
        assertFalse(result)
    }

    @Test
    fun test3() {
        val result = SUT.isPositive(1)
        assertTrue(result)
    }
}
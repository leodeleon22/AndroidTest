package com.techyourchance.unittestingfundamentals.exercise2

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

class StringDuplicatorTest {
    private val SUT = StringDuplicator()

    @Test
    @DisplayName("Given empty string When duplicate Then result is empty string")
    fun test1() {
        assertEquals("",SUT.duplicate(""))
    }

    @Test
    fun test2() {
        val result = SUT.duplicate("a")
        assertEquals("aa",result)
    }
}
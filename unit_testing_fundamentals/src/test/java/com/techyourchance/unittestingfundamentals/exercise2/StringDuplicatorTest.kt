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
    @DisplayName("Given A When duplicate Then result is AA")
    fun test2() {
        val result = SUT.duplicate("A")
        assertEquals("AA",result)
    }

    @Test
    @DisplayName("Given a long string When duplicate Then return a duplicated String")
    fun test3() {
        val result = SUT.duplicate("Leonardo Deleon")
        assertEquals("Leonardo DeleonLeonardo Deleon", result)
    }
}
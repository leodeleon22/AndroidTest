package com.techyourchance.unittestingfundamentals.exercise3

import com.techyourchance.unittestingfundamentals.example3.Interval
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*


class IntervalsAdjacencyDetectorTest {
    private val SUT = IntervalsAdjacencyDetector()

    @Test
    @DisplayName("Given two intervals When first overlaps second Then return false")
    fun testFirstOverlapsSecond() {
        val first = Interval(0,3)
        val second = Interval(1,4)
        val result = SUT.isAdjacent(first,second)
        assertFalse(result)
    }

    @Test
    @DisplayName("Given two intervals When first overlaps second Then return false")
    fun testSecondOverlapsFirst() {
        val first = Interval(0,3)
        val second = Interval(-1,1)
        val result = SUT.isAdjacent(first,second)
        assertFalse(result)
    }

    @Test
    @DisplayName("Given two intervals When first before second Then return false")
    fun testFirstBeforeSecond() {
        val first = Interval(-3,0)
        val second = Interval(1,4)
        val result = SUT.isAdjacent(first,second)
        assertFalse(result)
    }

    @Test
    @DisplayName("Given two intervals When first after second Then return false")
    fun testFirstAfterSecond() {
        val first = Interval(5,7)
        val second = Interval(1,4)
        val result = SUT.isAdjacent(first,second)
        assertFalse(result)
    }

    @Test
    @DisplayName("Given two intervals When they are the same Then return false")
    fun testSame() {
        val first = Interval(1,4)
        val second = Interval(1,4)
        val result = SUT.isAdjacent(first,second)
        assertFalse(result)
    }

    @Test
    @DisplayName("Given two intervals When first contains second Then return false")
    fun testFirstContainsSecond() {
        val first = Interval(-4,4)
        val second = Interval(0,3)
        val result = SUT.isAdjacent(first,second)
        assertFalse(result)
    }

    @Test
    @DisplayName("Given two intervals When second contains first Then return false")
    fun testSecondContainsFirst() {
        val first = Interval(1,4)
        val second = Interval(0,5)
        val result = SUT.isAdjacent(first,second)
        assertFalse(result)
    }

    @Test
    @DisplayName("Given two intervals When second after first Then return false")
    fun testSecondAfterFirst() {
        val first = Interval(0,2)
        val second = Interval(3,4)
        val result = SUT.isAdjacent(first,second)
        assertFalse(result)
    }

    @Test
    @DisplayName("Given two intervals When first is adjacent to second Then return true")
    fun testFirstAdjacentSecond() {
        val first = Interval(0,2)
        val second = Interval(2,4)
        val result = SUT.isAdjacent(first,second)
        assertTrue(result)
    }
}
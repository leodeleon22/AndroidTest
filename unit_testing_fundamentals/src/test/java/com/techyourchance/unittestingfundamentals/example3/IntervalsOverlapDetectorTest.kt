package com.techyourchance.unittestingfundamentals.example3

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName


class IntervalsOverlapDetectorTest {

    private val SUT = IntervalsOverlapDetector()
    // interval1 is before interval2

    @Test
    @Throws(Exception::class)
    @DisplayName("Given two intervals When first before second Then return false")
    fun isOverlap_interval1BeforeInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(8, 12)
        val result = SUT.isOverlap(interval1, interval2)
        assertFalse(result)
    }

    // interval1 overlaps interval2 on start

    @Test
    @Throws(Exception::class)
    @DisplayName("Given two intervals When first overlap second Then return true")
    fun isOverlap_interval1OverlapsInterval2OnStart_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(3, 12)
        val result = SUT.isOverlap(interval1, interval2)
        assertTrue(result)
    }
    // interval1 is contained within interval2

    @Test
    @Throws(Exception::class)
    @DisplayName("Given two intervals When second contains first Then return true")
    fun isOverlap_interval1ContainedWithinInterval2_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-4, 12)
        val result = SUT.isOverlap(interval1, interval2)
        assertTrue(result)
    }
    // interval1 contains interval2

    @Test
    @Throws(Exception::class)
    @DisplayName("Given two intervals When first contains second Then return true")
    fun isOverlap_interval1ContainsInterval2_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(0, 3)
        val result = SUT.isOverlap(interval1, interval2)
        assertTrue(result)
    }
    // interval1 overlaps interval2 on end

    @Test
    @Throws(Exception::class)
    @DisplayName("Given two intervals When first overlaps second on end Then return true")
    fun isOverlap_interval1OverlapsInterval2OnEnd_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-4, 4)
        val result = SUT.isOverlap(interval1, interval2)
        assertTrue(result)
    }
    // interval1 is after interval2

    @Test
    @Throws(Exception::class)
    @DisplayName("Given two intervals When first is after second Then return false")
    fun isOverlap_interval1AfterInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-10, -3)
        val result = SUT.isOverlap(interval1, interval2)
        assertFalse(result)
    }

    @Test
    @Throws(Exception::class)
    @DisplayName("Given two intervals When first is before second Then return false")
    fun isOverlap_interval1BeforeAdjacentInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(5, 8)
        val result = SUT.isOverlap(interval1, interval2)
        assertFalse(result)
    }

    @Test
    @Throws(Exception::class)
    @DisplayName("Given two intervals When second is adjacent to first Then return false")
    fun isOverlap_interval1AfterAdjacentInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-3, -1)
        val result = SUT.isOverlap(interval1, interval2)
        assertFalse(result)
    }
}
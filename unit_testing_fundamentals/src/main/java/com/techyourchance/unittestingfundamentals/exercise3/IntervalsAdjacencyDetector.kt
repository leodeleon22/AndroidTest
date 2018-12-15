package com.techyourchance.unittestingfundamentals.exercise3

import com.techyourchance.unittestingfundamentals.example3.Interval

class IntervalsAdjacencyDetector {

    /**
     * @return true if the intervals are adjacent, but don't overlap
     */
    fun isAdjacent(interval1: Interval, interval2: Interval): Boolean {
        // this implementation contains two bugs:
        // 1. will erroneously report adjacent if interval1 and interval2 are the same
        // 2. will erroneously report adjacent if interval1 after interval2
        return (interval1.end == interval2.start && interval1.start <= interval2.end
                && !isSameIntervals(interval1, interval2))
    }

    private fun isSameIntervals(interval1: Interval, interval2: Interval): Boolean {
        return interval1.start == interval2.start && interval1.end == interval2.end
    }

}

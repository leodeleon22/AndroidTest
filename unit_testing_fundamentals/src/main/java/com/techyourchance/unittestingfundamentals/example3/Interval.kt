package com.techyourchance.unittestingfundamentals.example3

class Interval(val start: Int, val end: Int) {

    init {
        if (start >= end) {
            throw IllegalArgumentException("invalid interval range")
        }
    }
}

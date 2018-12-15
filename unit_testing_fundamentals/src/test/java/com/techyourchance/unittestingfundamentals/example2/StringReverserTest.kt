package com.techyourchance.unittestingfundamentals.example2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class StringReverserTest {

    private val SUT = StringReverser()

    @Test
    @Throws(Exception::class)
    fun reverse_emptyString_emptyStringReturned() {
        val result = SUT.reverse("")
        assertEquals("",result)
    }

    @Test
    @Throws(Exception::class)
    fun reverse_singleCharacter_sameStringReturned() {
        val result = SUT.reverse("a")
        assertEquals("a",result)
    }

    @Test
    @Throws(Exception::class)
    fun reverse_longString_reversedStringReturned() {
        val result = SUT.reverse("Vasiliy Zukanov")
        assertEquals("vonakuZ yilisaV", result)
    }
}
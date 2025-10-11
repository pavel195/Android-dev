package tasks.whenops

import kotlin.test.Test
import kotlin.test.assertEquals

class ToStringSafeTest {
    @Test
    fun testPrimitives() {
        assertEquals("42", toStringSafe(42))
        assertEquals("3.14", toStringSafe(3.14))
        assertEquals("true", toStringSafe(true))
    }

    @Test
    fun testString() {
        assertEquals("hello", toStringSafe("hello"))
    }

    @Test
    fun testListNestedAndNulls() {
        val v = listOf(1, null, listOf(false, 2))
        assertEquals("[1, null, [false, 2]]", toStringSafe(v))
    }

    @Test
    fun testNull() {
        assertEquals("null", toStringSafe(null))
    }

    @Test
    fun testUnexpectedType() {
        assertEquals("", toStringSafe(mapOf(1 to 2)))
    }
}



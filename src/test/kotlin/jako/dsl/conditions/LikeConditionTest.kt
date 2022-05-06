package jako.dsl.conditions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LikeConditionTest {
    @Test
    fun `like obj`() {
        val condition = Like("field", "val%ue")

        assertEquals("\"field\" LIKE ?", condition.toString())
        assertEquals(listOf("val%ue"), condition.params())
    }

    @Test
    fun `like dsl`() {
        val condition = "field" LIKE "val%ue"

        assertEquals("\"field\" LIKE ?", condition.toString())
        assertEquals(listOf("val%ue"), condition.params())
    }

    @Test
    fun `contains dsl`() {
        val condition = "field" CONTAINS "value"

        assertEquals("\"field\" LIKE ?", condition.toString())
        assertEquals(listOf("%value%"), condition.params())
    }

    @Test
    fun `starts with dsl`() {
        val condition = "field" STARTS_WITH "value"

        assertEquals("\"field\" LIKE ?", condition.toString())
        assertEquals(listOf("value%"), condition.params())
    }

    @Test
    fun `ends with dsl`() {
        val condition = "field" ENDS_WITH "value"

        assertEquals("\"field\" LIKE ?", condition.toString())
        assertEquals(listOf("%value"), condition.params())
    }
}
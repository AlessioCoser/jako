package dbhelper.query.group

import dbhelper.query.fields.Fields.Companion.COUNT
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GroupByTest {
    @Test
    fun `no group by`() {
         assertEquals("", NoGroup().toString())
    }

    @Test
    fun `groupBy single field`() {
        val groupBy = GroupBy(listOf("field1"))

        assertEquals(" GROUP BY \"field1\"", groupBy.toString())
    }

    @Test
    fun `group by multiple fields`() {
        val groupBy = GroupBy(listOf("field1", "fie.ld2"))

        assertEquals(" GROUP BY \"field1\", \"fie\".\"ld2\"", groupBy.toString())
    }

    @Test
    fun `group by aggregate function`() {
        val groupBy = GroupBy(listOf("field1", COUNT("field2")))

        assertEquals(" GROUP BY \"field1\", count(\"field2\")", groupBy.toString())
    }
}
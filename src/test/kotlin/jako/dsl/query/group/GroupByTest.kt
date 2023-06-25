package jako.dsl.query.group

import jako.dsl.Dialect.All.PSQL
import jako.dsl.fields.Column
import jako.dsl.fields.functions.COUNT
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GroupByTest {
    @Test
    fun `no group by`() {
         assertEquals("", NoGroup().toSQL(PSQL))
    }

    @Test
    fun `groupBy single field`() {
        val groupBy = GroupBy("field1")

        assertEquals("GROUP BY \"field1\"", groupBy.toSQL(PSQL))
    }

    @Test
    fun `group by multiple fields`() {
        val groupBy = GroupBy("field1", "fie.ld2")

        assertEquals("GROUP BY \"field1\", \"fie\".\"ld2\"", groupBy.toSQL(PSQL))
    }

    @Test
    fun `group by aggregate function`() {
        val groupBy = GroupBy(Column("field1"), COUNT("field2"))

        assertEquals("GROUP BY \"field1\", COUNT(\"field2\")", groupBy.toSQL(PSQL))
    }
}
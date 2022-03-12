package dbhelper.query.join

sealed class Join(
    private val type: String,
    private val table: String,
    private val field1: String,
    private val field2: String? = null
) {
    fun statement(): String {
        if(field2 == null) {
            return "$type $table USING($field1)"
        }
        return "$type $table ON $field1 = $field2"
    }

    companion object {
        @JvmStatic
        infix fun String.join(field1: String): Join {
            return innerJoin(field1)
        }

        @JvmStatic
        infix fun String.innerJoin(field1: String): Join {
            return InnerJoin(this, field1)
        }

        @JvmStatic
        infix fun String.leftJoin(field1: String): Join {
            return LeftJoin(this, field1)
        }

        @JvmStatic
        infix fun String.rightJoin(field1: String): Join {
            return RightJoin(this, field1)
        }

        @JvmStatic
        infix fun Join.on(field2: String): Join {
            return when(this) {
                is InnerJoin -> InnerJoin(table, field1, field2)
                is LeftJoin -> LeftJoin(table, field1, field2)
                is RightJoin -> RightJoin(table, field1, field2)
            }
        }
    }
}

class InnerJoin(table: String, field1: String, field2: String? = null): Join("INNER JOIN", table, field1, field2)
class LeftJoin(table: String, field1: String, field2: String? = null): Join("LEFT JOIN", table, field1, field2)
class RightJoin(table: String, field1: String, field2: String? = null): Join("RIGHT JOIN", table, field1, field2)

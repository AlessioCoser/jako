package dbhelper.query.join

open class Join(
    val type: String,
    val table: String,
    val field1: String,
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
        infix fun Join.on(field2: String): Join {
            return Join(type, table, field1, field2)
        }
    }
}

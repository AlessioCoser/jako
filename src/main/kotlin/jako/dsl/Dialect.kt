package jako.dsl

data class Dialect(val columnSeparator: String) {

    companion object All {
        @JvmStatic
        val MYSQL = Dialect("`")
        @JvmStatic
        val PSQL = Dialect("\"")
    }
}
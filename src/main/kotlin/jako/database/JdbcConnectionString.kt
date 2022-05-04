package jako.database

object JdbcConnectionString {
    fun postgresql(host: String, username: String, password: String): String {
        return "jdbc:postgresql://$host?user=$username&password=$password"
    }
}
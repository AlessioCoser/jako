package jako.database

class JdbcConnection(val connection: String) {
    companion object {
        fun postgresql(host: String, username: String, password: String) =
            JdbcConnection("jdbc:postgresql://$host?user=$username&password=$password")
    }
}
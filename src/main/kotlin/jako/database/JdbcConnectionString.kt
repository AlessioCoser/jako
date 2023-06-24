package jako.database

object JdbcConnectionString {
    fun postgresql(host: String, username: String, password: String): String {
        return "jdbc:postgresql://$host?user=$username&password=$password"
    }

    fun mysql(host: String, username: String, password: String): String {
        return "jdbc:mysql://$host?user=$username&password=$password&allowPublicKeyRetrieval=true&useSSL=false"
    }
}
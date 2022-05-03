package jako.database

class JdbcPostgresConnection private constructor(val connection: String) {
    constructor(
        host: String,
        username: String,
        password: String
    ) : this("jdbc:postgresql://$host?user=$username&password=$password")
}

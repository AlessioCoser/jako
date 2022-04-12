package dbhelper.dsl.update

import dbhelper.dsl.Statement
import dbhelper.dsl.where.Where

class Update(override val statement: String, override val params: List<Any?>): Statement {
    constructor(
        table: String,
        fields: SetFields,
        where: Where
    ) : this("UPDATE $table$fields$where", fields.params().plus(where.params()))
}
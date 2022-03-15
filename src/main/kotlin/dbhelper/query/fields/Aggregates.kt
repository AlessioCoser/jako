package dbhelper.query.fields

import dbhelper.query.fields.Fields.wrap

object Aggregates {
    fun COUNT(fieldName: String) = "count(${fieldName.wrap()})"
    fun AVG(fieldName: String) = "avg(${fieldName.wrap()})"
    fun EVERY(fieldName: String) = "every(${fieldName.wrap()})"
    fun MAX(fieldName: String) = "max(${fieldName.wrap()})"
    fun MIN(fieldName: String) = "min(${fieldName.wrap()})"
    fun SUM(fieldName: String) = "sum(${fieldName.wrap()})"
}
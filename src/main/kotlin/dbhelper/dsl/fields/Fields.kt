package dbhelper.dsl.fields

import dbhelper.dsl.fields.Column.Companion.wrap
import dbhelper.dsl.fields.Field.Companion.ALL

interface Field {
    override fun toString(): String

    operator fun plus(i: Int): Field {
        return Constant("${toString()} + $i")
    }

    operator fun minus(i: Int): Field {
        return Constant("${toString()} - $i")
    }

    companion object {
        val ALL: Field = Constant("*")
    }
}

class Constant(val value: Any): Field {
    override fun toString() = value.toString()
}

class Column(val value: String): Field {
    override fun toString() = value.wrap()

    companion object {
        @JvmStatic
        fun C(value: String): Field {
            return Column(value)
        }

        fun String.wrap(): String {
            if(contains("(*)") || this == "*" || contains("AS")) {
                return this
            }
            if(contains("(\"") || (startsWith("\"") && endsWith("\""))) {
                return this
            }
            if(contains("(")) {
                return this
                    .replace(".", "\".\"")
                    .replace("(", "(\"")
                    .replace(")", "\")")
            }
            return """"${this.replace(".", "\".\"")}""""
        }
    }
}


class As(val column: Field, val name: Field): Field {
    override fun toString() = "$column AS $name"

    companion object {
        @JvmStatic
        infix fun String.AS(name: String): Field {
            return As(Column(this), Column(name))
        }

        @JvmStatic
        infix fun Field.AS(name: String): Field {
            return As(this, Column(name))
        }
    }
}

class CountField(val value: Field): Field {
    override fun toString() = "COUNT($value)"

    companion object {
        @JvmStatic
        fun COUNT(fieldName: String) = CountField(Column(fieldName))

        @JvmStatic
        fun COUNT(field: Field) = CountField(field)
    }
}

class AvgField(val value: Field): Field {
    override fun toString() = "AVG($value)"

    companion object {
        @JvmStatic
        fun AVG(fieldName: String) = AvgField(Column(fieldName))

        @JvmStatic
        fun AVG(field: Field) = AvgField(field)
    }
}

class MaxField(val value: Field): Field {
    override fun toString() = "MAX($value)"

    companion object {
        @JvmStatic
        fun MAX(fieldName: String) = MaxField(Column(fieldName))

        @JvmStatic
        fun MAX(field: Field) = MaxField(field)
    }
}

class CoalesceField(val value: Field, val default: Field): Field {
    override fun toString() = "COALESCE($value, $default)"

    companion object {
        @JvmStatic
        fun COALESCE(fieldName: String, default: Any) = CoalesceField(Column(fieldName), Constant(default))

        @JvmStatic
        fun COALESCE(fieldName: Field, default: Any) = CoalesceField(fieldName, Constant(default))
    }
}



class Fields(private val fields: List<Field>) {

    constructor(vararg fields: String): this(fields.map { Column(it) })

    override fun toString() = fields.joinToString(separator = ", ") { it.toString() }

    companion object {
        fun EVERY(fieldName: String) = "every(${fieldName.wrap()})"
        fun MIN(fieldName: String) = "min(${fieldName.wrap()})"
        fun SUM(fieldName: String) = "sum(${fieldName.wrap()})"


        fun all(): Fields {
            return Fields(listOf(ALL))
        }
    }
}

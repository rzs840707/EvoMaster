package org.evomaster.core.database.schema

/**
 *
 * Should be immutable
 */
data class Column(

        val name: String,

        val type: ColumnDataType,

        val size: Int = 0,

        val primaryKey: Boolean = false,

        val nullable: Boolean = true,

        val unique: Boolean = false,

        val autoIncrement: Boolean = false,

        var foreignKeyToAutoIncrement: Boolean = false,

        var lowerBound: Int? = null,

        var upperBound: Int? = null

        // public boolean identity;

        //TODO something for other constraints
)
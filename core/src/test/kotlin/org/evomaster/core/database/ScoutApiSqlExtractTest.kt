package org.evomaster.core.database

import org.evomaster.clientJava.controller.db.SqlScriptRunner
import org.evomaster.clientJava.controller.internal.db.SchemaExtractor
import org.evomaster.clientJava.controllerApi.dto.database.schema.DatabaseType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.sql.Connection
import java.sql.DriverManager

class ScoutApiSqlExtractTest {


    companion object {

        private lateinit var connection: Connection

        @BeforeAll
        @JvmStatic
        fun initClass() {
            connection = DriverManager.getConnection("jdbc:h2:mem:db_test", "sa", "")
        }
    }

    @BeforeEach
    fun initTest() {

        //custom H2 command
        SqlScriptRunner.execCommand(connection, "DROP ALL OBJECTS;")
    }


    @Test
    fun testCreateAndExtract() {

        val sqlCommand = this::class.java.getResource("/sql_schema/scout-api.sql").readText()

        SqlScriptRunner.execCommand(connection, sqlCommand)

        val schema = SchemaExtractor.extract(connection)

        assertNotNull(schema)

        assertAll(Executable { assertEquals("public", schema.name.toLowerCase()) },
                Executable { assertEquals(DatabaseType.H2, schema.databaseType) },
                Executable { assertEquals(14, schema.tables.size) },
                Executable { assertTrue(schema.tables.any { it.name == "ACTIVITY" }, "missing table ACTIVITY") },
                Executable { assertTrue(schema.tables.any { it.name == "ACTIVITY_DERIVED" }, "missing table ACTIVITY_DERIVED") },
                Executable { assertTrue(schema.tables.any { it.name == "ACTIVITY_PROPERTIES" }, "missing table ACTIVITY_PROPERTIES") },
                Executable { assertTrue(schema.tables.any { it.name == "ACTIVITY_PROPERTIES_MEDIA_FILE" }, "missing table ACTIVITY_PROPERTIES_MEDIA_FILE") },
                Executable { assertTrue(schema.tables.any { it.name == "ACTIVITY_PROPERTIES_TAG" }, "missing table ACTIVITY_PROPERTIES_TAG") },
                Executable { assertTrue(schema.tables.any { it.name == "ACTIVITY_RATING" }, "missing table ACTIVITY_RATING") },
                Executable { assertTrue(schema.tables.any { it.name == "ACTIVITY_RELATION" }, "missing table ACTIVITY_RELATION") },
                Executable { assertTrue(schema.tables.any { it.name == "MEDIA_FILE" }, "missing table MEDIA_FILE") },
                Executable { assertTrue(schema.tables.any { it.name == "MEDIA_FILE_KEYWORDS" }, "missing table MEDIA_FILE_KEYWORDS") },
                Executable { assertTrue(schema.tables.any { it.name == "SYSTEM_MESSAGE" }, "missing table SYSTEM_MESSAGE") },
                Executable { assertTrue(schema.tables.any { it.name == "TAG" }, "missing table TAG") },
                Executable { assertTrue(schema.tables.any { it.name == "TAG_DERIVED" }, "missing table TAG_DERIVED") },
                Executable { assertTrue(schema.tables.any { it.name == "USER_IDENTITY" }, "missing table USER_IDENTITY") },
                Executable { assertTrue(schema.tables.any { it.name == "USERS" }, "missing table USERS") }
        )


        assertTrue(schema.tables.filter { it.name == "ACTIVITY" }.first().columns.filter { it.name == "ID" }.first().autoIncrement)
        assertTrue(schema.tables.filter { it.name == "ACTIVITY_PROPERTIES" }.first().columns.filter { it.name == "ID" }.first().autoIncrement)
        assertTrue(schema.tables.filter { it.name == "USERS" }.first().columns.filter { it.name == "ID" }.first().autoIncrement)
        assertTrue(schema.tables.filter { it.name == "SYSTEM_MESSAGE" }.first().columns.filter { it.name == "ID" }.first().autoIncrement)
        assertTrue(schema.tables.filter { it.name == "TAG" }.first().columns.filter { it.name == "ID" }.first().autoIncrement)
        assertTrue(schema.tables.filter { it.name == "USER_IDENTITY" }.first().columns.filter { it.name == "ID" }.first().autoIncrement)
        assertTrue(schema.tables.filter { it.name == "MEDIA_FILE" }.first().columns.filter { it.name == "ID" }.first().autoIncrement)


        assertEquals(5, schema.tables.filter { it.name == "USERS" }.first().columns.size)

        assertEquals(true, schema.tables.filter { it.name == "ACTIVITY_PROPERTIES" }.first().columns.filter { it.name == "publishing_activity_id".toUpperCase() }.first().unique)
        assertEquals(true, schema.tables.filter { it.name == "MEDIA_FILE" }.first().columns.filter { it.name == "uri".toUpperCase() }.first().unique)
        assertEquals(true, schema.tables.filter { it.name == "SYSTEM_MESSAGE" }.first().columns.filter { it.name == "key".toUpperCase() }.first().unique)

        assertEquals(100, schema.tables.filter { it.name == "ACTIVITY_PROPERTIES" }.first().columns.filter { it.name == "age_max".toUpperCase() }.first().upperBound)
        assertEquals(100, schema.tables.filter { it.name == "ACTIVITY_PROPERTIES" }.first().columns.filter { it.name == "age_min".toUpperCase() }.first().upperBound)


    }


}
import java.io.File

fun readInputFile(name: String) = File("src/main/resources/$name").readText()

fun <T> assertEquals(actual: T, expected: T) = require(actual == expected) { "$actual != $expected" }

fun String.splitByEmptyLines(): List<String> = split("\n\n")

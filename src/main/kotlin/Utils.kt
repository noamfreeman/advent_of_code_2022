import java.io.File

fun readInputFile(name: String) = File("src/main/resources/$name").readText()

fun <T> assertEquals(actual: T, expected: T) = require(actual == expected) { "$actual != $expected" }
fun assertEqualString(actual: String, expected: String) {
    val alines = actual.lines()
    val eLines = expected.lines()
    require(alines.size == eLines.size) { "number of lines ${alines.size} != ${eLines.size}"}
    alines.zip(eLines).forEachIndexed { i, (actual, expected) ->
        require(actual == expected) { "at line $i:$actual!=$expected" }
    }
}


fun String.splitByEmptyLines(): List<String> = split("\n\n")

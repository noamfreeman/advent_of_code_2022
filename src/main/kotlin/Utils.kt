import java.io.File

fun readInputFile(name: String) = File("src/main/resources/$name").readText()

fun <T> assertEquals(actual: T, expected: T) = require(actual == expected) { "$actual != $expected" }
fun assertEqualString(actual: String, expected: String) {
    val alines = actual.lines()
    val eLines = expected.lines()
    require(alines.size == eLines.size) { "number of lines ${alines.size} != ${eLines.size}" }
    alines.zip(eLines).forEachIndexed { i, (actual, expected) ->
        require(actual == expected) { "at line $i:$actual!=$expected" }
    }
}


fun String.splitByEmptyLines(): List<String> = split("\n\n")

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    fun opposite() = when (this) {
        UP -> DOWN
        DOWN -> UP
        LEFT -> RIGHT
        RIGHT -> LEFT
    }

    companion object
}

data class Coord2(val x: Int, val y: Int) {
    companion object {
        val ZERO = Coord2(0, 0)
    }
}
typealias MatrixCoord = Coord2

operator fun MatrixCoord.plus(other: MatrixCoord) = copy(x = x + other.x, y = y + other.y)
operator fun MatrixCoord.plus(dir: Direction) = when (dir) {
    Direction.UP -> copy(y = y - 1)
    Direction.DOWN -> copy(y = y + 1)
    Direction.LEFT -> copy(x = x - 1)
    Direction.RIGHT -> copy(x = x + 1)
}

fun sign(int: Int): Int {
    if (int == 0) return 0
    if (int > 0) return 1
    return -1
}
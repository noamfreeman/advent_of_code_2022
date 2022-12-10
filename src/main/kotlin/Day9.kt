import kotlin.math.abs

private val exampleInput = """
    R 4
    U 4
    L 3
    D 1
    R 4
    D 1
    L 5
    R 2
""".trimIndent()

private val largeExample = """
    R 5
    U 8
    L 8
    D 3
    R 17
    D 10
    L 25
    U 20
""".trimIndent()


fun main() {
    println("day8")
    println()

    println("part1")

    assertEquals(part1(exampleInput), 13)
    println(part1(readInputFile("day9_input.txt")))

    println()

    println("part2")
    assertEquals(part2(exampleInput), 1)
    assertEquals(part2(largeExample), 36)
    println(part2(readInputFile("day9_input.txt")))
}

private fun part1(input: String): Int {
    val moves = parseInput(input)
    return countPositionsOfTail(2, moves)
}

private fun part2(input: String): Int {
    val moves = parseInput(input)
    return countPositionsOfTail(10, moves)
}

private fun countPositionsOfTail(ropeLength: Int, moves: List<Direction>): Int {
    val startPosition = Rope(List(ropeLength) { Coord2.ZERO })
    val positions = moves.fold(listOf(startPosition)) { positions, move ->
        positions + positions.last().move(move)
    }
    return positions.map {
        it.nodes.last()
    }.toSet().size
}

private fun parseInput(input: String): List<Direction> {
    return input.lines().flatMap { line ->
        val (dir, count) = line.split(" ")
        val direction = Direction.fromString(dir)
        val amount = count.toInt()
        List(amount) { direction }
    }
}

data class Rope(val nodes: List<Coord2>) {
    fun move(direction: Direction): Rope {
        val newNodes =nodes.fold(listOf<Coord2>()) { rope, node ->
            if (rope.isEmpty()) rope + (node + direction)
            else rope + moveTailToHead(node, rope.last())
        }
        return Rope(newNodes)
    }
}

private fun moveTailToHead(tail: Coord2, head: Coord2): Coord2 {
    val xDiff = head.x - tail.x
    val yDiff = head.y - tail.y
    val isDiagonal = abs(xDiff) > 0 && abs(yDiff) > 1 || abs(yDiff) > 0 && abs(xDiff) > 1
    if (isDiagonal) {
        return tail + Coord2(sign(xDiff), sign(yDiff))
    }
    return tail + Coord2(singleDimensionPull(xDiff), singleDimensionPull(yDiff))
}

private fun singleDimensionPull(int: Int): Int {
    if (int == 1) return 0
    if (int == -1) return 0
    return sign(int)
}

private fun Direction.Companion.fromString(dir: String) = when (dir) {
    "R" -> Direction.RIGHT
    "L" -> Direction.LEFT
    "U" -> Direction.UP
    "D" -> Direction.DOWN
    else -> error("unknown direction $dir")
}
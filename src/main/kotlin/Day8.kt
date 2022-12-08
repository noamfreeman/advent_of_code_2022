import kotlin.math.max

private val exampleInput = """
    30373
    25512
    65332
    33549
    35390
""".trimIndent()

fun main() {
    println("day8")
    println()

    println("part1")

    assertEquals(part1(exampleInput), 21)
    println(part1(readInputFile("day8_input.txt")))

    println()

    println("part2")
    assertEquals(part2(exampleInput), 8)
    println(part2(readInputFile("day8_input.txt")))
}

private fun part1(input: String): Int {
    val trees = parseInput(input)
    val cache = mutableMapOf<Triple<Int, Int, Direction>, Int>()
    return Direction.values()
        .flatMap { dir ->
            // all visible trees from a direction
            trees.allCoords().filter { coord ->
                trees.isTreeVisibleFromDirection(coord, dir, memoizationCache = cache)
            }
        }
        .toSet() // some trees are visible from more than one direction
        .count()
}

private fun part2(input: String): Int {
    val trees = parseInput(input)
    val cache = mutableMapOf<LookPosition, Int>()
    return trees.allCoords().map { coord ->
        val score = Direction.values().fold(1) { acc, dir ->
            acc * trees.treesSeenFrom(LookPosition(xy = coord, z = trees[coord], dir = dir), memoizationCache = cache)
        }
        score
    }.max()
}

private fun parseInput(input: String): Matrix<Int> {
    return input
        .lines()
        .map { line -> line.map { it.digitToInt() } }
        .let { Matrix(it) }
}

private fun Matrix<Int>.isTreeVisibleFromDirection(
    coord: MatrixCoord, dir: Direction,
    memoizationCache: MutableMap<Triple<Int, Int, Direction>, Int>
): Boolean =
    // if there is a tree higher than this tree, it doesn't see the edge.
    maxValueInDirection(coord, dir.opposite(), memoizationCache) < this[coord]


private fun Matrix<Int>.maxValueInDirection(
    coord: MatrixCoord, dir: Direction,
    memoizationCache: MutableMap<Triple<Int, Int, Direction>, Int>
): Int = memoizationCache.getOrPut(Triple(coord.y, coord.x, dir)) {
    val neighborCoord = coord + dir
    if (isOnEdge(coord, dir)) -1// no values in this direction
    else max(
        this[neighborCoord],
        maxValueInDirection(neighborCoord, dir, memoizationCache)
    )
}

private data class LookPosition(val xy: MatrixCoord, val z: Int, val dir: Direction)

private fun Matrix<Int>.treesSeenFrom(
    lookPosition: LookPosition,
    memoizationCache: MutableMap<LookPosition, Int>
): Int {
    val coord = lookPosition.xy
    val dir = lookPosition.dir
    val neighborCoord = coord + dir
    val sightFromNeighbor = lookPosition.copy(xy = neighborCoord)

    return memoizationCache.getOrPut(lookPosition) {
        if (isOnEdge(coord, dir)) 0
        else if (this[neighborCoord] >= lookPosition.z) return 1 // we see only the neighbor
        else treesSeenFrom(
            sightFromNeighbor, memoizationCache
        ) + 1// we see one more than the neighbor would have seen if he had been high as we
    }
}

private class Matrix<T>(private val data: List<List<T>>) {
    val rows = data.size
    val columns = data[0].size

    init {
        data.forEachIndexed { y, row ->
            require(row.size == columns) { "row $y size: ${row.size} != $columns" }
        }
    }

    operator fun get(coord: MatrixCoord) = data[coord.y][coord.x]

    fun isOnEdge(coord: MatrixCoord, edge: Direction) = when (edge) {
        Direction.UP -> coord.y == 0
        Direction.DOWN -> coord.y == rows - 1
        Direction.LEFT -> coord.x == 0
        Direction.RIGHT -> coord.x == columns - 1
    }

    fun allCoords() = sequence {
        (0 until rows).forEach { y ->
            (0 until columns).forEach { x ->
                yield(MatrixCoord(x, y))
            }
        }
    }
}

private data class MatrixCoord(val x: Int, val y: Int)

private operator fun MatrixCoord.plus(dir: Direction) = when (dir) {
    Direction.UP -> copy(y = y - 1)
    Direction.DOWN -> copy(y = y + 1)
    Direction.LEFT -> copy(x = x - 1)
    Direction.RIGHT -> copy(x = x + 1)
}

private enum class Direction {
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
}

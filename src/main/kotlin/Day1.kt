private val part1ExampleInput = """
    1000
    2000
    3000

    4000

    5000
    6000

    7000
    8000
    9000

    10000
""".trimIndent()

private fun main() {
    println("day1")
    println()

    println("part1")
    assertEquals(part1(part1ExampleInput), 24_000)
    println(part1(readInputFile("day1_input.txt")))
    println()

    println("part2")
    assertEquals(part2(part1ExampleInput), 45_000)
    println(part2(readInputFile("day1_input.txt")))
}

fun part1(input: String): Int {
    val calories: List<List<Int>> = parseInput(input)
    return calories.maxOf { it.sum() }
}

fun part2(input: String): Int {
    val calories: List<List<Int>> = parseInput(input)
    return calories
        .map { it.sum() }
        .sorted()
        .takeLast(3)
        .sum()
}

private fun parseInput(input: String): List<List<Int>> {
    return input
        .splitByEmptyLines()
        .map { elf ->
            elf.lines().map { it.toInt() }
        }
}


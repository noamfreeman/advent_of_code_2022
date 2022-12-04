private val part1ExampleInput = """
    2-4,6-8
    2-3,4-5
    5-7,7-9
    2-8,3-7
    6-6,4-6
    2-6,4-8
""".trimIndent()

fun main() {
    println("day4")
    println()

    println("part1")

    assertEquals(part1(part1ExampleInput), 2)
    assertEquals(part1(readInputFile("day4_input.txt")), 485) // regression for equal ranges.
    println(part1(readInputFile("day4_input.txt")))

    println()

    println("part2")
    assertEquals(part2(part1ExampleInput), 4)
    println(part2(readInputFile("day4_input.txt")))
}

private fun parseInput(input: String): List<Pair<IntRange, IntRange>> = input
    .lines().map { line ->
        val (elf1, elf2) = line.split(",")
        val range1 = elf1.parseRange()
        val range2 = elf2.parseRange()
        range1 to range2
    }

private fun part1(input: String): Int = parseInput(input)
    .count { (range1, range2) ->
        range1.contains(range2) || range2.contains(range1)
    }

private fun part2(input: String): Int = parseInput(input)
    .count { (range1, range2) ->
        range1 intersects range2
    }

private fun String.parseRange(): IntRange = this
    .split("-")
    .let {
        it[0].toInt()..it[1].toInt()
    }

private fun IntRange.contains(other: IntRange): Boolean {
    return this.first <= other.first && this.last >= other.last
}

private infix fun IntRange.intersects(other: IntRange): Boolean {
    return this.contains(other.first) || this.contains(other.last)
            || other.contains(this.first) || other.contains(this.last)
}

private val part1ExampleInput = """
    A Y
    B X
    C Z
""".trimIndent()

private fun main() {
    println("day1")
    println()

    println("part1")
    assertEquals(part1(part1ExampleInput), 15)
    println(part1(readInputFile("day2_input.txt"))) // 17_189
    println()

    println("part2")
    assertEquals(part2(part1ExampleInput), 12)
    assertEquals(part2(readInputFile("day2_input.txt")), 13_490)
}

private fun part2(input: String): Int {
    return input.lines().sumOf {line ->
        val other = line[0]
        val me = line[2]
        part2Score(me, other)
    }
}

private fun part1(input: String): Int {
    val mapping = mapOf(
        'X' to 'A',
        'Y' to 'B',
        'Z' to 'C',
    )
    return input.lines().sumOf { line ->
        val other = line[0]
        val me = line[2].interpretWith(mapping)
        scorePerRound(me, other)
    }
}

private fun Char.interpretWith(mapping: Map<Char, Char>): Char {
    return mapping.getValue(this)
}

private fun scorePerRound(me: Char, other: Char) = scorePerItem(me) + scorePerPair(me, other)

private fun scorePerPair(me: Char, other: Char) =
    when {
        me == 'A' && other == 'B' ||
                me == 'B' && other == 'C' ||
                me == 'C' && other == 'A' ->
            //lose
            0

        me == other -> 3
        else -> 6
    }

private fun scorePerItem(item: Char) = when (item) {
    'A' -> 1
    'B' -> 2
    'C' -> 3
    else -> error("unknown $item")
}

private fun part2Score(me: Char, other: Char) =
    when {
        me == 'Y' && other == 'A' -> 4
        me == 'Y' && other == 'B' -> 5
        me == 'Y' && other == 'C' -> 6
        me == 'X' && other == 'A' -> 3
        me == 'X' && other == 'B' -> 1
        me == 'X' && other == 'C' -> 2
        me == 'Z' && other == 'A' -> 8
        me == 'Z' && other == 'B' -> 9
        me == 'Z' && other == 'C' -> 7
        else -> error("")
    }
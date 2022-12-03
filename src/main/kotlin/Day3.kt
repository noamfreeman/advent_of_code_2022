private val part1ExampleInput = """
    vJrwpWtwJgWrhcsFMMfFFhFp
    jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
    PmmdzqPrVvPwwTWBwg
    wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
    ttgJtRGJQctTZtZT
    CrZsJsPPZsGzwwsLwLmpwMDw
""".trimIndent()

fun main() {
    println("day3")
    println()

    println("part1")

    assertEquals(part1(part1ExampleInput), 157)
    println(part1(readInputFile("day3_input.txt")))

    println()

    println("part2")
    assertEquals(part2(part1ExampleInput), 70)
    println(part2(readInputFile("day3_input.txt")))
}

private fun part1(string: String): Int = string
    .lines()
    .map { sack ->
        val (a, b) = sack.splitAtMiddle()
        findCommon(a, b)
    }.sumOf { char ->
        char.toValue()
    }

private fun part2(string: String): Int = string
    .lines()
    .chunked(3)
    .map {
        findCommon(it)
    }.sumOf {
        it.toValue()
    }

private fun String.splitAtMiddle(): Pair<String, String> {
    require(length % 2 == 0)
    return take(length / 2) to takeLast(length / 2)
}

private fun findCommon(a: String, b: String) = findCommon(listOf(a, b))

private fun findCommon(strings: List<String>): Char {
    val occurrences = mutableMapOf<Char, Int>()
    strings.forEach { string ->
        string.toSet().forEach { char ->
            occurrences.incrementOrZero(char)
        }
    }
    return occurrences.toList().firstOrNull { it.second == strings.size }?.first
        ?: error("no common in $strings")
}

private fun MutableMap<Char, Int>.incrementOrZero(char: Char) {
    this[char] = this.getOrDefault(char, 0) + 1
}

private fun Char.toValue(): Int {
    val lower = this - 'a'
    if (lower > 0) return lower + 1
    return this - 'A' + 27
}
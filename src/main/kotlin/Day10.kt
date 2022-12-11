import kotlin.math.abs

private val exampleInput = """
    addx 15
    addx -11
    addx 6
    addx -3
    addx 5
    addx -1
    addx -8
    addx 13
    addx 4
    noop
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx -35
    addx 1
    addx 24
    addx -19
    addx 1
    addx 16
    addx -11
    noop
    noop
    addx 21
    addx -15
    noop
    noop
    addx -3
    addx 9
    addx 1
    addx -3
    addx 8
    addx 1
    addx 5
    noop
    noop
    noop
    noop
    noop
    addx -36
    noop
    addx 1
    addx 7
    noop
    noop
    noop
    addx 2
    addx 6
    noop
    noop
    noop
    noop
    noop
    addx 1
    noop
    noop
    addx 7
    addx 1
    noop
    addx -13
    addx 13
    addx 7
    noop
    addx 1
    addx -33
    noop
    noop
    noop
    addx 2
    noop
    noop
    noop
    addx 8
    noop
    addx -1
    addx 2
    addx 1
    noop
    addx 17
    addx -9
    addx 1
    addx 1
    addx -3
    addx 11
    noop
    noop
    addx 1
    noop
    addx 1
    noop
    noop
    addx -13
    addx -19
    addx 1
    addx 3
    addx 26
    addx -30
    addx 12
    addx -1
    addx 3
    addx 1
    noop
    noop
    noop
    addx -9
    addx 18
    addx 1
    addx 2
    noop
    noop
    addx 9
    noop
    noop
    noop
    addx -1
    addx 2
    addx -37
    addx 1
    addx 3
    noop
    addx 15
    addx -21
    addx 22
    addx -6
    addx 1
    noop
    addx 2
    addx 1
    noop
    addx -10
    noop
    noop
    addx 20
    addx 1
    addx 2
    addx 2
    addx -6
    addx -11
    noop
    noop
    noop
""".trimIndent()

private val part2Output = """
    ##..##..##..##..##..##..##..##..##..##..
    ###...###...###...###...###...###...###.
    ####....####....####....####....####....
    #####.....#####.....#####.....#####.....
    ######......######......######......####
    #######.......#######.......#######.....
""".trimIndent()


fun main() {
    println("day10")
    println()

    println("part1")

    assertEquals(part1(exampleInput), 13_140)
    println(part1(readInputFile("day10_input.txt")))

    println()

    println("part2")
    assertEqualString(part2(exampleInput), part2Output)
    println(part2(readInputFile("day10_input.txt")))
}

private fun part1(input: String): Int {
    val registerValues = calculateRegisterValues(input)
    val places = listOf(
        20, 60, 100, 140, 180, 220
    )
    return places.sumOf { registerValues[it - 1] * it }
}

private fun part2(input: String): String {
    val registerValues = calculateRegisterValues(input)
    val screen = registerValues
        .mapIndexed { index, i ->
            if (abs(index % 40 - i) <= 1) "#" else "."
        }
        .chunked(40)
        .dropLast(1)

    return screen.joinToString("\n") { it.joinToString("") }
}

private fun calculateRegisterValues(input: String): List<Int> {
    val register = mutableListOf(1)
    input.lines().forEach {
        if (it == "noop") {
            register.add(register.last())
        } else {
            val amount = it.split(" ")[1].toInt()
            register.add(register.last())
            register.add(register.last() + amount)
        }
    }
    return register
}

private val part1ExampleInput = """
    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
""".trimIndent()

fun main() {
    println("day5")
    println()

    println("part1")

    assertEquals(part1(part1ExampleInput), "CMZ")
    println(part1(readInputFile("day5_input.txt")))

    println()

    println("part2")
    assertEquals(part2(part1ExampleInput), "MCD")
    println(part2(readInputFile("day5_input.txt")))
}

private fun part1(input: String): String {
    val (columns, instructions) = parseInput(input)
    val res = instructions.fold(columns) { cols, instruction ->
        cols.executeInstruction(instruction, reversed = true)
    }
    return res.map { it.last() }.joinToString("")
}

private fun part2(input: String): String {
    val (columns, instructions) = parseInput(input)
    val res = instructions.fold(columns) { cols, instruction ->
        cols.executeInstruction(instruction, reversed = false)
    }
    return res.map { it.last() }.joinToString("")
}


private fun <T> List<List<T>>.executeInstruction(instruction: Instruction, reversed: Boolean): MutableList<List<T>> {
    val src = this[instruction.source]
    val dest = this[instruction.dest]
    return this.toMutableList().apply {
        this[instruction.dest] = dest + src.takeLast(instruction.amount).run { if (reversed) reversed() else this }
        this[instruction.source] = src.dropLast(instruction.amount)
    }
}

private data class Instruction(val source: Int, val dest: Int, val amount: Int)

private fun parseInput(input: String): Pair<List<List<Char>>, List<Instruction>> {
    val (crates, rawInstructions) = input.splitByEmptyLines()
    val rows = crates.lines().dropLast(1).reversed().map { line ->
        line.drop(1).chunked(4).map {
            it[0].takeIf { it != ' ' }
        }
    }
    val columns = rows.transpose().map {
        it.removeTrailingNulls()
    }
    val regex = Regex("move (\\d+) from (\\d+) to (\\d+)")
    val instructions = rawInstructions.lines().map { rawInstruction ->
        val match = regex.find(rawInstruction)!!.groupValues
        Instruction(source = match[2].toInt() - 1, dest = match[3].toInt() - 1, amount = match[1].toInt())
    }
    return columns to instructions
}

private fun <T> List<List<T>>.transpose(): List<List<T>> {
    val length = size
    // assuming all inner list in the same size
    val width = this[1].size
    return List(width) { i -> List(length) { j -> this[j][i] } }
}

@Suppress("UNCHECKED_CAST")
private fun <T> List<T?>.removeTrailingNulls(): List<T> {
    // assuming non-null until some tail.
    val firstNull = indexOfFirst { it == null }
    if (firstNull == -1) return this as List<T>
    return subList(0, firstNull) as List<T>
}
import java.lang.Integer.max

private val exampleInput = """
    ${'$'} cd /
    ${'$'} ls
    dir a
    14848514 b.txt
    8504156 c.dat
    dir d
    ${'$'} cd a
    ${'$'} ls
    dir e
    29116 f
    2557 g
    62596 h.lst
    ${'$'} cd e
    ${'$'} ls
    584 i
    ${'$'} cd ..
    ${'$'} cd ..
    ${'$'} cd d
    ${'$'} ls
    4060174 j
    8033020 d.log
    5626152 d.ext
    7214296 k
""".trimIndent()

private val exampleFileSystem = """
- / (dir)
  - a (dir)
    - e (dir)
      - i (file, size=584)
    - f (file, size=29116)
    - g (file, size=2557)
    - h.lst (file, size=62596)
  - b.txt (file, size=14848514)
  - c.dat (file, size=8504156)
  - d (dir)
    - j (file, size=4060174)
    - d.log (file, size=8033020)
    - d.ext (file, size=5626152)
    - k (file, size=7214296)

""".trimIndent()

fun main() {
    println("day7")
    println()

    println("part1")

    assertEqualString(parseFileSystem(exampleInput).repr(), exampleFileSystem)
    assertEquals(part1(exampleInput), 95437)
    println(part1(readInputFile("day7_input.txt")))

    println()

    println("part2")
    assertEquals(part2(exampleInput), 24_933_642)
    println(part2(readInputFile("day7_input.txt")))
}

private fun part1(input: String): Int {
    val fileSystem = parseFileSystem(input)
    return fileSystem.getAllDirs().filter {
        it.totalSize() < 100_000
    }.sumOf { it.totalSize() }
}

private fun part2(input: String): Int {
    val fileSystem = parseFileSystem(input)
    val totalSize = fileSystem.totalSize()
    val availableSpace = (70_000_000 - totalSize)
    val moreSpaceNeeded = max(30_000_000 - availableSpace, 0)
    return fileSystem.getAllDirs()
        .map {it.totalSize() }
        .filter {
            it > moreSpaceNeeded
        }.minOf { it }

}

private fun parseFileSystem(input: String): FileSystem {
    val fileSystem = FileSystem()
    input.lines().forEach { line ->
        if (line.startsWith("$")) {
            if (line == "% ls") {
                // do nothing
            } else if (line.startsWith("$ cd ")) {
                val dirname = line.drop("$ cd ".length)
                fileSystem.moveTo(dirname)
            }
        } else {
            if (line.startsWith("dir ")) {
                val dirname = line.drop("dir ".length)
                fileSystem.addDir(dirname)
            } else {
                val (number, name) = line.split(" ")
                fileSystem.addFile(name, number.toInt())
            }
        }
    }
    return fileSystem
}


private class FileSystem {
    private var root = Dir("/", mutableListOf())
    private var currentPath: List<Dir> = listOf(root)
    private fun currentDir() = currentPath.last()

    fun addDir(name: String) {
        currentDir().addChild(name)
    }

    fun totalSize() = root.totalSize()

    fun addFile(name: String, size: Int) {
        currentDir().children.add(File(name, size))
    }

    fun moveTo(path: String) {
        when (path) {
            "/" -> currentPath = listOf(root)
            ".." -> currentPath = currentPath.dropLast(1)
            else -> {
                val dir = currentDir().addChild(path)
                currentPath = currentPath + dir
            }
        }
    }

    fun repr(): String {
        return root.repr()
    }

    fun getAllDirs(): Sequence<Dir> {
        return sequence {
            val current = mutableListOf(root)

            suspend fun SequenceScope<Dir>.yieldNext() {
                val dir = current.removeAt(0)
                yield(dir)
                current.addAll(dir.children.filterIsInstance<Dir>())
            }

            while (current.isNotEmpty()) {
                yieldNext()
            }
        }
    }
}

private sealed interface Entry {
    fun repr(): String
}

private class File(val name: String, val size: Int) : Entry {
    override fun repr(): String = toString() + "\n"
    override fun toString(): String = "- $name (file, size=$size)"
}

private class Dir(val name: String, val children: MutableList<Entry>) : Entry {
    private fun findDir(path: String): Dir? {
        return children.filterIsInstance<Dir>().find {
            it.name == path
        }
    }

    fun addChild(path: String): Dir {
        val existingDir = findDir(path)
        if (existingDir != null) return existingDir

        val newDir = Dir(path, mutableListOf())
        children.add(newDir)
        return newDir
    }

    override fun toString(): String = "- $name (dir)"

    override fun repr(): String {
        val children = children.joinToString("") {
            it.repr()
        }.indent()
        return "$this\n$children"
    }
}

private fun Entry.totalSize(): Int = when(this) {
    is File -> this.size
    is Dir -> this.children.sumOf { it.totalSize() }
}

private fun String.indent(): String {
    return lines().filter { it.isNotBlank() }.joinToString("") { "  $it\n" }
}
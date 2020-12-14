package day14

// https://adventofcode.com/2020/day/14

import getFileFromArgs

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)

    val system = System()
    file.readLines().forEach { system.processLine(it) }

    println(system.sumMemoryNumbers())
}

class System() {
    private val memory = mutableMapOf<Long, Long>()
    private var mask: Mask? = null

    fun processLine(line: String) {
        val splited = line.split(" = ")
        if (splited[0] == "mask") mask = Mask(splited[1])
        else {
            //saveValue(splited[0], splited[1])
            saveValue2(splited[0], splited[1])
        }
    }

    private fun saveValue(posString: String, value: String) {
        val pos = extractPos(posString)
        memory[pos.toLong()] = mask?.applyTo(value) ?: throw Exception("Mask not initialized")
    }

    private fun saveValue2(posString: String, value: String) {
        val pos = extractPos(posString)
        mask?.applyToMultiple(pos)
                ?.forEach {
                    memory[it] = value.toLong()
                } ?: throw Exception("Mask not initialized")
    }

    private fun extractPos(posString: String): String {
        val match = posRegex.find(posString)!!
        val (pos) = match.destructured
        return pos
    }

    fun sumMemoryNumbers(): Long = memory.values.sum()

    companion object {
        val posRegex: Regex = Regex("mem\\[(\\d+)\\]")
    }
}

class Mask(val mask: String) {
    fun applyTo(value: String) =
            value.as36bitString().mapIndexed { index, ch ->
                when (mask[index]) {
                    'X' -> ch
                    '1' -> '1'
                    '0' -> '0'
                    else -> ch
                }
            }.joinToString("").toLong(2)

    fun applyToMultiple(value: String): Set<Long> {
        val bitVariations = value.as36bitString().mapIndexed { index, ch ->
            when (mask[index]) {
                '0' -> setOf(ch)
                '1' -> setOf('1')
                'X' -> setOf('0', '1')
                else -> setOf(ch)
            }
        }

        return bitVariations.fold(mutableSetOf("")) { acc, chs ->
            chs.flatMap { ch -> acc.map { str -> str + ch } }.toMutableSet()
        }.map { it.toLong(2) }.toSet()
    }

    private fun String.as36bitString() = this.toLong().toString(2).padStart(36, '0')

}
package day08

// https://adventofcode.com/2020/day/8

import getFileFromArgs

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)

    val instructions = file.readLines().map { instructionFrom(it) }
    val result = Executor(instructions).execute()
    println(result.second)

    val mutator = Mutator(instructions)
    do {
        val result2 = Executor(mutator.nextMutation()).execute()
        if (result2.first) {
            println(result2.second)
        }
    } while (!result2.first)
}

class Mutator(private val instr: List<Instruction>) {
    var lastMutated = -1

    fun nextMutation(): List<Instruction> {
        while (lastMutated <= instr.size) {
            lastMutated += 1
            if (instr[lastMutated].operation in mutableOperations) {
                return mutateOperation(lastMutated)
            }
        }
        throw Exception("No more mutations available")
    }

    private fun mutateOperation(indexToMutate: Int): List<Instruction> {
        return instr.mapIndexed { index, instruction ->
            when (index) {
                indexToMutate -> Instruction(if (instruction.operation == "nop") "jmp" else "nop", instruction.argument)
                else -> instruction
            }
        }
    }

    companion object {
        private val mutableOperations = setOf("nop", "jmp")
    }
}

class Executor(private val instrs: List<Instruction>) {
    private val listLength = instrs.size
    private val executed = mutableSetOf<Int>()
    private var nextInstr = 0

    fun execute(): Pair<Boolean, Int> {
        var accumulator = 0

        while (!finished()) {
            executed.add(nextInstr)
            val instr = instrs[nextInstr]
            when (instr.operation) {
                "acc" -> {
                    accumulator += instr.argument; nextInstr += 1
                }
                "nop" -> {
                    nextInstr += 1
                }
                "jmp" -> {
                    nextInstr += instr.argument
                }
            }
        }

        return if (finishedOk()) Pair(true, accumulator)
        else Pair(false, accumulator)
    }

    private fun finished(): Boolean = executed.contains(nextInstr) || nextInstr >= listLength
    private fun finishedOk(): Boolean = !executed.contains(nextInstr) || nextInstr >= listLength
}

fun instructionFrom(line: String): Instruction {
    val splitted = line.split(" ")
    return Instruction(splitted[0], splitted[1].toInt())
}

data class Instruction(
        val operation: String,
        val argument: Int
)

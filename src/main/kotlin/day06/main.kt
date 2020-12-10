package day06

// https://adventofcode.com/2020/day/6

import getFileFromArgs
import java.io.File

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)

    val groupAnswers = parseGroupAnswers(file)

    val sumDifferent = groupAnswers.map { it.differentAnswers }.sum()
    val sumSame = groupAnswers.map { it.sameAnswers }.sum()

    println("$sumDifferent")
    println("$sumSame")
}

private fun parseGroupAnswers(file: File): MutableList<GroupAnswers> {
    val answers = mutableListOf<String>()
    val groupAnswers = mutableListOf<GroupAnswers>()
    file.readLines().forEach {
        if (it.isEmpty()) {
            groupAnswers.add(GroupAnswers(answers))
            answers.clear()
        } else {
            answers.add(it)
        }
    }.also {
        if (answers.isNotEmpty()) {
            groupAnswers.add(GroupAnswers(answers))
        }
    }
    return groupAnswers
}

class GroupAnswers(answers: List<String>) {
    val differentAnswers: Int
    val sameAnswers: Int

    init {
        val answersSets = answers.map { it.toSet() }
        differentAnswers = answersSets.reduce { s1, s2 -> s1.union(s2) }.size
        sameAnswers = answersSets.reduce { s1, s2 -> s1.intersect(s2) }.size
    }
}

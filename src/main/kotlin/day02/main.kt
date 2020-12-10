package day02

// https://adventofcode.com/2020/day/2

import getFileFromArgs

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)
    val validPasswords = file.readLines().filter { isValidPassword(it) }.count()

    println("$validPasswords passwords are valid")
}

fun isValidPassword(line: String): Boolean {
    val splitedLine = line.split(":")
    val password = splitedLine[1].trim()
    val conditions = splitedLine[0]
    return Condition(conditions).checkCondition(password)
}

class Condition(conditionExpr: String) {
    private val letter: Char
    private val minReps: Int
    private val maxReps: Int

    init {
        val conditionExprSplited = conditionExpr.split(" ")
        letter = conditionExprSplited[1].first()
        val repetitionsSplitted = conditionExprSplited[0].split("-")
        minReps = repetitionsSplitted[0].toInt()
        maxReps = repetitionsSplitted[1].toInt()
    }

    fun checkCondition(password: String): Boolean {
        //val reps = password.count{ it == letter }
        //return reps in minReps..maxReps;
        val in1 = password[minReps - 1] == letter
        val in2 = password[maxReps - 1] == letter
        return in1 xor in2
    }
}
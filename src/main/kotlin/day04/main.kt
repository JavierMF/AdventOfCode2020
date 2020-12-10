package day04

// https://adventofcode.com/2020/day/4

import getFileFromArgs
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)
    val passports = readPassportsFromFile(file)

    passports.forEach { println(it) }

    val validPassports = passports.filter { it.isValid() }.count()
    val totalPassports = passports.size

    println("We found $validPassports valid passports of $totalPassports")
}

data class Passport(val lines: String) {
    fun isValid(): Boolean {
        val fieldsMap = lines.trim().split(" ")
                .map { val pair = it.split(":"); pair[0] to pair[1] }.toMap()
        return fieldsMap.keys.containsAll(requiredFields) && allFieldsValid(fieldsMap)
    }

    private fun allFieldsValid(fieldsMap: Map<String, String>) =
            fieldsMap
                    .filter { it.key in requiredFields }
                    .all { fieldIsValid(it.key, it.value) }

    private fun fieldIsValid(key: String, value: String) =
            when (key) {
                "byr" -> value.fourDigitsInRange(1920..2002)
                "iyr" -> value.fourDigitsInRange(2010..2020)
                "eyr" -> value.fourDigitsInRange(2020..2030)
                "ecl" -> value in validEyeColors
                "pid" -> value.length == 9 && value.toLongOrNull() != null
                "hcl" -> value.matches("#[0-9a-fA-F]{6}".toRegex())
                "hgt" -> value.isValidHeight()
                else -> false
            }

    companion object {
        val requiredFields = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
        val validEyeColors = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
    }
}

private fun String.fourDigitsInRange(intRange: IntRange) = this.length == 4 && this.toInt() in intRange

private fun String.isValidHeight(): Boolean = when {
    this.endsWith("cm") -> {
        val height = this.removeSuffix("cm").toIntOrNull()
        height in 150..193
    }
    this.endsWith("in") -> {
        val height = this.removeSuffix("in").toIntOrNull()
        height in 59..76
    }
    else -> false
}

private fun readPassportsFromFile(file: File): List<Passport> {
    val passports = mutableListOf<Passport>()
    var currentPassport = ""
    file.readLines().forEach {
        if (it.isEmpty()) {
            passports.add(Passport(currentPassport))
            currentPassport = ""
        } else
            currentPassport += " $it"
    }
    if (currentPassport.isNotEmpty()) passports.add(Passport(currentPassport))
    return passports.toList()
}

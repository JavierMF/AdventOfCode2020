package day16

// https://adventofcode.com/2020/day/16

import getFileFromArgs

fun main(args: Array<String>) {
    val lines = getFileFromArgs(args).readLines()
    val yourTicketIndex = lines.indexOf("your ticket:")

    val yourTicket = lines[yourTicketIndex + 1].split(",").map { it.toInt() }
    val nearbyTickets = NearbyTickets(lines.subList(yourTicketIndex + 4, lines.size))
    val rules = Rules(lines.subList(0, yourTicketIndex - 1))

    val errorRate = nearbyTickets.allNumbers().filter { !rules.isValidForAny(it) }.sum()
    println(errorRate)


    val searcher = PositionSearcher(rules.rules)
    val validTickets = nearbyTickets.tickets.filter { it.numbers.all { n -> rules.isValidForAny(n) } }
    validTickets.forEach { searcher.checkTicket(it) }

    val result = searcher.departureIndexes().map { yourTicket[it].toLong() }.reduce { a, b -> a * b }
    println(result)
}

class PositionSearcher(rules: List<Rule>) {
    var positions: List<MutableList<Rule>> = rules.map { rules.toMutableList() }.toList()

    fun checkTicket(ticket: Ticket) {
        for (index in positions.indices) {
            positions[index].removeAll { !it.isValid(ticket.numbers[index]) }
        }
    }

    fun departureIndexes(): List<Int> {
        findSingleRules()
        return positions.mapIndexedNotNull { index, rules ->
            if (rules.first().isDeparture()) index else null
        }
    }

    private fun findSingleRules() {
        do {
            var anyChange = false
            val singlePositions = positions.filter { it.size == 1 }.map { it.first() }
            for (candidatesRules in positions) {
                if (candidatesRules.size > 1) candidatesRules.removeAll(singlePositions).also { anyChange = true }
            }
        } while (anyChange)
    }
}

class Rules(rulesStrings: List<String>) {
    val rules = rulesStrings.map { Rule(it) }

    fun isValidForAny(value: Int) = rules.any { it.isValid(value) }
}

data class Rule(val rule: String) {

    private var highRange: IntRange
    private var lowRange: IntRange
    private var name: String

    init {
        val split = rule.split(": ")
        name = split[0]
        val ranges = split[1].split(" or ")
        val lowRangeSplit = ranges[0].split("-")
        lowRange = lowRangeSplit[0].toInt()..lowRangeSplit[1].toInt()
        val highRangeSplit = ranges[1].split("-")
        highRange = highRangeSplit[0].toInt()..highRangeSplit[1].toInt()
    }

    fun isValid(value: Int) = value in lowRange || value in highRange
    fun isDeparture(): Boolean = name.startsWith("departure")
}

class NearbyTickets(tickets: List<String>) {
    val tickets = tickets.map { Ticket(it) }
    fun allNumbers() = tickets.flatMap { it.numbers }
}

class Ticket(ticket: String) {
    val numbers = ticket.split(",").map { it.toInt() }
}
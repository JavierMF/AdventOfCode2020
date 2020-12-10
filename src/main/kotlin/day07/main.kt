package day07

// https://adventofcode.com/2020/day/7

import getFileFromArgs

fun main(args: Array<String>) {
    val file = getFileFromArgs(args)

    val bags = file.readLines()
            .map { Bag(it) }
            .toSet()

    val bagSet = BagSet(bags)
    val canContain = bagSet.canContain("shiny gold").count()
    println("$canContain")

    val containedIn = bagSet.containedIn("shiny gold") - 1
    println("$containedIn")
}

class BagSet(private val bags: Set<Bag>) {
    private val bagsByColor = bags.map { it.color to it }.toMap()

    fun canContain(color: String): Set<Bag> {
        val colorsThatCanContain = bags.filter { it.canContain(color) }.toSet()
        val colorsThatCanContainParents = colorsThatCanContain
                .map { canContain(it.color) }
                .fold(setOf<Bag>()) { a, b -> a + b }

        return colorsThatCanContain + colorsThatCanContainParents
    }

    fun containedIn(color: String): Int {
        val bag = bagsByColor[color]

        return 1 + bag!!.canContain
                .map { property -> property.value * containedIn(property.key) }
                .sum()
    }
}

class Bag(rule: String) {
    val color: String
    val canContain: Map<String, Int>

    init {
        val splitted = rule.split(" contain ")
        color = splitted.first().removeSuffix(" bags")
        canContain = if (splitted.last() == "no other bags.") {
            emptyMap()
        } else {
            splitted.last()
                    .removeSuffix(".")
                    .split(",")
                    .map {
                        val bagsSplitted = it.trim().removeSuffix(" bags").split(" ")
                        "${bagsSplitted[1]} ${bagsSplitted[2]}" to bagsSplitted[0].toInt()
                    }.toMap()
        }
    }

    fun canContain(bagColor: String) = canContain.containsKey(bagColor)
}

package day18

// https://adventofcode.com/2020/day/18

import getFileFromArgs

fun main(args: Array<String>) {
    val lines = getFileFromArgs(args).readLines()

    val result1 = lines.map { Parser(it).parse().evaluate1() }.sum()
    println(result1)

    val result2 = lines.map { Parser(it).parse().evaluate2() }.sum()
    println(result2)
}

class Parser(val exprString: String) {
    fun parse(): Expression {
        val exprStack = mutableListOf(Expression())
        exprString.forEach {
            when (it) {
                in '0'..'9' -> exprStack.last().addValue(it)
                '(' -> exprStack.add(exprStack.last().newExpression())
                ')' -> exprStack.removeLast()
                '+' -> exprStack.last().addOperator(it)
                '*' -> exprStack.last().addOperator(it)
            }
        }
        return exprStack.first()
    }
}

open class Token

class Value(val value: Long) : Token()

class Operator(private val operator: Char) : Token() {

    fun applyOperation(a: Long, b: Long) = when (operator) {
        '+' -> a + b
        '*' -> a * b
        else -> throw IllegalArgumentException("Wrong operator")
    }

    fun isAddition() = '+' == operator
}

class Expression : Token() {
    private val tokens = mutableListOf<Token>()

    fun evaluate1(): Long = evaluateTokens1(tokens)

    fun evaluateTokens1(tokens: List<Token>): Long {
        val lastValue = when (val last = tokens.last()) {
            is Value -> last.value
            is Expression -> last.evaluate1()
            else -> throw IllegalArgumentException("Wrong token")
        }
        if (tokens.size == 1) return lastValue

        val operator = tokens[tokens.size - 2] as Operator
        val subValue = evaluateTokens1(tokens.take(tokens.size - 2))

        return operator.applyOperation(lastValue, subValue)
    }

    fun evaluate2(): Long = evaluateTokens2(tokens)

    // I'm not proud of this, but it works
    fun evaluateTokens2(tokens: List<Token>): Long {
        val tokensWithoutExpressions = tokens.map { if (it is Expression) Value(it.evaluate2()) else it }
        val onlyMultiOperations = applySumOperations(tokensWithoutExpressions)
        return evaluateTokens1(onlyMultiOperations)
    }

    private fun applySumOperations(valueTokens: List<Token>): MutableList<Token> {
        val onlyMultiOperations = mutableListOf<Token>()

        var i = 0
        while (i < valueTokens.size) {
            when (val token = valueTokens[i]) {
                is Value -> onlyMultiOperations.add(token)
                is Operator -> {
                    if (token.isAddition()) {
                        // We apply the sum with the last value we added
                        val prev = (onlyMultiOperations.last() as Value).value
                        val next = (valueTokens[i + 1] as Value).value

                        val result = token.applyOperation(prev, next)

                        // Last value has already been used in the sum
                        onlyMultiOperations[onlyMultiOperations.size - 1] = Value(result)

                        // Next value has already been used in the sum
                        i += 1
                    } else {
                        onlyMultiOperations.add(token)
                    }
                }
            }
            i += 1
        }

        return onlyMultiOperations
    }

    fun addValue(value: Char) = tokens.add(Value(value.toString().toLong()))
    fun addOperator(operator: Char) = tokens.add(Operator(operator))
    fun newExpression() = Expression().also { tokens.add(it) }
}

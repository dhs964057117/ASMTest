package com.haosen.asmtest.test

// 定义操作符
enum class Operation(val operator: String) {
    ADD("+"),
    SUBTRACT("-"),
    MULTI("*"),
    DIVIDE("/");
}

/**
 * 定义计算公式实体类
 */
data class Formula(
    private val left: Int,
    private val operator: Operation,
    private val right: Int
) {

    companion object {
        private val MATCH_REGEX = """(-?\d+)\s*([+\-*/])(-?\d+)""".toRegex()
        fun getMatchRegex() = MATCH_REGEX
    }

    fun calculate(): Int? {
        return when (operator) {
            Operation.ADD -> add()
            Operation.SUBTRACT -> subtract()
            Operation.MULTI -> multi()
            Operation.DIVIDE -> if (right != 0) divide() else null // 防止除以0
            else -> null // 不支持的运算符
        }
    }

    private fun add(): Int {
        return left + right
    }

    private fun subtract(): Int {
        return left - right
    }

    private fun multi(): Int {
        return left * right
    }

    private fun divide(): Int {
        return left / right
    }
}
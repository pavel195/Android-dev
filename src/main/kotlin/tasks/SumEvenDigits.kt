package tasks

import kotlin.system.exitProcess

/**
 * Сумма чётных цифр целого числа в пределах Int.
 * Валидация входа: допускается ведущий знак, остальное — только цифры; число должно быть в диапазоне Int.
 */
fun sumEvenDigits(input: String): Int {
    val s = input.trim()
    require(s.isNotEmpty()) { "Пустая строка недопустима" }

    val signAllowed = if (s.first() == '+' || s.first() == '-') 1 else 0
    require(signAllowed < s.length) { "Отсутствуют цифры после знака" }
    require((signAllowed until s.length).all { s[it].isDigit() }) { "Строка должна содержать только цифры после необязательного знака" }

    // Проверяем диапазон Int для корректности ввода
    try {
        s.toInt()
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException("Число вне диапазона Int")
    }

    // Складываем чётные цифры, игнорируя знак
    return (signAllowed until s.length)
        .asSequence()
        .map { s[it] - '0' }
        .filter { it % 2 == 0 }
        .sum()
}

fun main() {
    // Примеры:
    // Ввод:  -12034
    // Вывод: 6
    // Ввод:  908
    // Вывод: 8
    try {
        val line = readLine() ?: throw IllegalArgumentException("Ожидалась строка с числом")
        println(sumEvenDigits(line))
    } catch (e: IllegalArgumentException) {
        System.err.println(e.message ?: "Некорректный ввод")
        exitProcess(1)
    }
}



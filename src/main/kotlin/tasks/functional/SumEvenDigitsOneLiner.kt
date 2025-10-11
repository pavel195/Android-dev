package tasks.functional

import kotlin.system.exitProcess

/**
 * Однострочное решение: сумма чётных цифр входного целого (в пределах Int).
 * Условия: без var/mutable/рекурсии; одна цепочка точечных вызовов, включая ввод/вывод.
 */
fun main() = try {
    // Пример: ввод "-12034" → вывод "6"
    println(
        (readLine() ?: error("Ожидалась строка"))
            .trim()
            .also { require(it.isNotEmpty()) { "Пустая строка недопустима" } }
            .also { s -> s.toIntOrNull() ?: throw IllegalArgumentException("Должно быть целое число в пределах Int") }
            .filter { it.isDigit() }
            .map { it - '0' }
            .filter { it % 2 == 0 }
            .sum()
    )
} catch (e: IllegalArgumentException) {
    System.err.println(e.message ?: "Некорректный ввод")
    exitProcess(1)
}



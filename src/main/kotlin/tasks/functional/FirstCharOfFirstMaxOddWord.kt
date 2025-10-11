package tasks.functional

import kotlin.system.exitProcess

/**
 * Однострочное решение: первый символ первого максимально длинного слова нечётной длины.
 * Вход: строка слов, разделённых одним или несколькими пробелами. Выход: символ или пустая строка, если не найдено.
 */
fun main() = try {
    // Пример: "aa bbb ccc d" → "b"
    println(
        (readLine() ?: error("Ожидалась строка"))
            .trim()
            .split(" ")
            .filter { it.isNotEmpty() }
            .let { words ->
                val maxOdd = words.filter { it.length % 2 == 1 }.maxByOrNull { it.length }
                maxOdd?.firstOrNull()?.toString() ?: ""
            }
    )
} catch (e: IllegalArgumentException) {
    System.err.println(e.message ?: "Некорректный ввод")
    exitProcess(1)
}



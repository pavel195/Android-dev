package tasks.functional

import kotlin.system.exitProcess

/**
 * Однострочное решение: числа через пробел; вычислить побитовое И предпоследних цифр всех чисел, у которых она есть.
 * Если подходящих нет — печатать 0.
 */
fun main() = try {
    // Пример: "12 305 -7 80" → предпоследние: 1, 0, (нет), 8 → 1 & 0 & 8 = 0
    println(
        (readLine() ?: error("Ожидалась строка"))
            .trim()
            .split(" ")
            .filter { it.isNotEmpty() }
            .mapNotNull { tok -> tok.toLongOrNull() }
            .mapNotNull { v ->
                val abs = kotlin.math.abs(v)
                if (abs >= 10) ((abs / 10) % 10).toInt() else null
            }
            .fold<Int, Int?>(null) { acc, d -> if (acc == null) d else (acc and d) }
            ?: 0
    )
} catch (e: IllegalArgumentException) {
    System.err.println(e.message ?: "Некорректный ввод")
    exitProcess(1)
}



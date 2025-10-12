package tasks.functional

import java.math.BigInteger
import kotlin.system.exitProcess

/**
 * Однострочное решение: по номеру числа Фибоначчи (F0=0, F1=1) найти F(n).
 * Реализация через fold без var/рекурсии; результат BigInteger (на случай больших n).
 */
fun main() = try {
    // Примеры: n=0 → 0; n=1 → 1; n=10 → 55
    println(
        (println("Введите n (неотрицательное целое):").let { readLine() ?: error("Ожидалось n") })
            .trim()
            .toIntOrNull()
            ?.also { require(it >= 0) { "n должно быть неотрицательным" } }
            ?.let { n -> (0 until n).fold(0.toBigInteger() to 1.toBigInteger()) { (a, b), _ -> b to (a + b) }.first }
            ?: throw IllegalArgumentException("n должно быть целым")
    )
} catch (e: IllegalArgumentException) {
    System.err.println(e.message ?: "Некорректный ввод")
    exitProcess(1)
}



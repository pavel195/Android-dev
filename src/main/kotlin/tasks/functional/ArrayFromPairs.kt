package tasks.functional

import kotlin.system.exitProcess

/**
 * Однострочное решение: ввод 10 строк формата `номер:значение` (номер 0..9 в любом порядке),
 * вывод значений по возрастанию номера, по одному в строке.
 */
fun main() = try {
    // Пример ввода (10 строк):
    // 3:three
    // 0:zero
    // ...
    // Вывод: значения по индексам 0..9
    println("Введите 10 строк формата индекс:значение (индексы 0..9):")
    generateSequence { readLine() }
        .take(10)
        .map { line ->
            val parts = line.split(":")
            require(parts.size == 2) { "Ожидался формат номер:значение" }
            val idx = parts[0].trim().toIntOrNull() ?: throw IllegalArgumentException("Номер должен быть целым")
            require(idx in 0..9) { "Номер должен быть 0..9" }
            idx to parts[1]
        }
        .toList()
        .also { list -> require(list.map { it.first }.toSet().size == 10) { "Должны быть все индексы 0..9 без повторов" } }
        .sortedBy { it.first }
        .forEach { println(it.second) }
} catch (e: IllegalArgumentException) {
    System.err.println(e.message ?: "Некорректный ввод")
    exitProcess(1)
}



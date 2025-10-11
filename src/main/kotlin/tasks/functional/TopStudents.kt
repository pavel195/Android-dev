package tasks.functional

import kotlin.system.exitProcess

/**
 * Однострочное решение: строки до EOF вида "Фамилия Имя Оценка1 Оценка2 ..." (целые),
 * вывести студентов, попавших в диапазон трёх лучших средних баллов (включая тай-брейки),
 * сортировка: по среднему ↓, затем Фамилия ↑, Имя ↑. Формат вывода: "Фамилия Имя средний".
 */
fun main() = try {
    // Пример:
    // Иванов Петр 5 4 5
    // Смирнов Алексей 4 4 5
    // Петров Иван 5 5 5
    // → выведет всех с топ-3 средними, по убыванию среднего, затем по фамилии/имени
    generateSequence { readLine() }
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .map { line ->
            val parts = line.split(" ").filter { it.isNotEmpty() }
            require(parts.size >= 3) { "Ожидались: Фамилия Имя Оценки..." }
            val surname = parts[0]
            val name = parts[1]
            val grades = parts.drop(2).map { it.toIntOrNull() ?: throw IllegalArgumentException("Оценки должны быть целыми") }
            require(grades.isNotEmpty()) { "Нужна хотя бы одна оценка" }
            val avg = grades.average()
            Triple(surname, name, avg)
        }
        .toList()
        .let { students ->
            val topAverages = students
                .map { it.third }
                .distinct()
                .sortedDescending()
                .take(3)
                .toSet()
            students
                .filter { it.third in topAverages }
                .sortedWith(compareByDescending<Triple<String, String, Double>> { it.third }
                    .thenBy { it.first }
                    .thenBy { it.second })
        }
        .forEach { println("${it.first} ${it.second} ${"%.2f".format(it.third)}") }
} catch (e: IllegalArgumentException) {
    System.err.println(e.message ?: "Некорректный ввод")
    exitProcess(1)
}



package tasks.whenops

import kotlin.system.exitProcess

/** Проверка високосного года по григорианским правилам. */
fun isLeapYear(year: Int): Boolean = when {
    year % 400 == 0 -> true
    year % 100 == 0 -> false
    year % 4 == 0 -> true
    else -> false
}

/** Количество дней в месяце (1..12) для указанного года. */
fun daysInMonth(month: Int, year: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (isLeapYear(year)) 29 else 28
    else -> throw IllegalArgumentException("Месяц должен быть в диапазоне 1..12") //Для некорректного месяца выбрасывается исключение.
}

/** Возвращает дату следующего дня без использования библиотек дат. */
fun nextDay(day: Int, month: Int, year: Int): Triple<Int, Int, Int> {
    require(month in 1..12) { "Месяц должен быть в диапазоне 1..12" }
    val dim = daysInMonth(month, year)
    require(day in 1..dim) { "День должен быть в диапазоне 1..$dim для $month.$year" }

    return if (day < dim) Triple(day + 1, month, year)
    else if (month < 12) Triple(1, month + 1, year)
    else Triple(1, 1, year + 1)
}

fun main() {
    // Примеры:
    // Ввод: 28 2 2024 → Вывод: 29 2 2024 | sameDays=true
    // Ввод: 31 12 2023 → Вывод: 1 1 2024 | sameDays=false
    try {
        println("Введите дату в формате: dd mm yyyy:")
        val parts = (readLine() ?: error("Ожидалось: dd mm yyyy")).trim().split(" ")
            .filter { it.isNotEmpty() }
        require(parts.size == 3) { "Ожидалось три числа: dd mm yyyy" }
        val (d, m, y) = parts.map { it.toIntOrNull() ?: throw IllegalArgumentException("Все поля должны быть целыми числами") }

        val beforeDim = daysInMonth(m, y)
        val (nd, nm, ny) = nextDay(d, m, y)
        val afterDim = daysInMonth(nm, ny)
        val same = beforeDim == afterDim

        println("$nd $nm $ny | sameDays=$same")
    } catch (e: IllegalArgumentException) {
        System.err.println(e.message ?: "Некорректный ввод")
        exitProcess(1)
    }
}



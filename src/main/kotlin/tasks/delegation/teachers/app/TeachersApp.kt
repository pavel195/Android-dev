package tasks.delegation.teachers.app

import tasks.delegation.teachers.domain.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess

private val DATE_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yyyy")

fun main() {
    try {
        println("Ввод преподавателей. Пустая строка в поле ФИО — завершение.")
        val teachers = mutableListOf<Teacher>()
        while (true) {
            print("ФИО: ")
            val fio = readLine()?.trim().orEmpty()
            if (fio.isEmpty()) break

            print("Пол (M/F/other): ")
            val gender = when (readLine()?.trim()?.lowercase()) {
                "m" -> Gender.MALE
                "f" -> Gender.FEMALE
                else -> Gender.OTHER
            }

            print("Средний балл учащихся (0..5): ")
            val avg = readLine()?.trim()?.toDoubleOrNull() ?: throw IllegalArgumentException("Нужно число")
            val base: Teacher = BaseTeacher(fio, gender, avg)

            var decorated: Teacher = base

            print("Есть категория? (y/n): ")
            if (readLine()?.trim()?.lowercase() == "y") {
                print("Дата последнего подтверждения (д.М.гггг): ")
                val date = LocalDate.parse(readLine()?.trim().orEmpty(), DATE_FMT)
                print("Номер приказа: ")
                val orderNo = readLine()?.trim().orEmpty()
                decorated = WithCategory(decorated, date, orderNo)
            }

            print("Есть кандидатская степень? (y/n): ")
            if (readLine()?.trim()?.lowercase() == "y") {
                print("Тема диссертации: ")
                val topic = readLine()?.trim().orEmpty()
                print("Научное направление: ")
                val field = readLine()?.trim().orEmpty()
                print("Дата защиты (д.М.гггг): ")
                val defense = LocalDate.parse(readLine()?.trim().orEmpty(), DATE_FMT)
                decorated = WithPhD(decorated, topic, field, defense)
            }

            teachers += decorated
        }

        println("\nПреподаватели со средним баллом > 4:")
        teachers.filter { it.avgScore > 4.0 }
            .sortedBy { it.fullName }
            .forEach { println(it.describe()) }
    } catch (e: Exception) {
        System.err.println(e.message ?: "Некорректный ввод")
        exitProcess(1)
    }
}



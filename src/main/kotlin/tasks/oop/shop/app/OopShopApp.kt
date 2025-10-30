package tasks.oop.shop.app

import tasks.oop.shop.domain.Book
import tasks.oop.shop.domain.Disc
import tasks.oop.shop.domain.Product
import kotlin.system.exitProcess

/**
 * Консольное приложение: ввод книг и дисков с подсказками; вывод товаров с ценой < 100.
 * Принципы SOLID: доменные классы отделены от ввода/вывода.
 */
fun main() {
    try {
        println("Добавление товаров. Допустимые типы: book, disc. Пустая строка — завершение.")
        val products = mutableListOf<Product>()
        while (true) {
            print("Тип товара (book|disc, пусто — конец): ")
            val type = readLine()?.trim().orEmpty()
            if (type.isEmpty()) break
            when (type.lowercase()) {
                "book" -> products += readBook()
                "disc" -> products += readDisc()
                else -> throw IllegalArgumentException("Неизвестный тип товара: $type")
            }
        }

        println("\nТовары с ценой < 100:")
        products.filter { it.price < 100 }
            .forEach { p ->
                when (p) {
                    is Book -> println("Книга: '${p.name}', автор: ${p.author}, стр.: ${p.pages}, цена: ${p.price}")
                    is Disc -> println("Диск: '${p.name}', треков: ${p.tracks}, цена: ${p.price}")
                    else -> println("Товар: ${p.name}, цена: ${p.price}")
                }
            }
    } catch (e: IllegalArgumentException) {
        System.err.println(e.message ?: "Некорректный ввод")
        exitProcess(1)
    }
}

private fun readBook(): Book {
    print("Название книги: ")
    val name = readLine()?.trim().orEmpty().also { require(it.isNotEmpty()) { "Название обязательно" } }
    print("Автор: ")
    val author = readLine()?.trim().orEmpty().also { require(it.isNotEmpty()) { "Автор обязателен" } }
    print("Страниц (целое > 0): ")
    val pages = readLine()?.trim()?.toIntOrNull() ?: throw IllegalArgumentException("Страницы: нужно целое число")
    print("Цена (неотрицательное целое): ")
    val price = readLine()?.trim()?.toIntOrNull() ?: throw IllegalArgumentException("Цена: нужно целое число")
    return Book(name, author, pages, price)
}

private fun readDisc(): Disc {
    print("Название диска: ")
    val name = readLine()?.trim().orEmpty().also { require(it.isNotEmpty()) { "Название обязательно" } }
    print("Треков (целое > 0): ")
    val tracks = readLine()?.trim()?.toIntOrNull() ?: throw IllegalArgumentException("Треков: нужно целое число")
    print("Цена (неотрицательное целое): ")
    val price = readLine()?.trim()?.toIntOrNull() ?: throw IllegalArgumentException("Цена: нужно целое число")
    return Disc(name, tracks, price)
}



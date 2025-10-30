package tasks.oop.shop.domain

/**
 * Книга: название, автор, страницы, цена. Неизменяемый, валидируемый тип.
 */
data class Book(
    override val name: String,
    val author: String,
    val pages: Int,
    override val price: Int
) : Product {
    init {
        require(name.isNotBlank()) { "Название книги не может быть пустым" }
        require(author.isNotBlank()) { "Автор не может быть пустым" }
        require(pages > 0) { "Количество страниц должно быть > 0" }
        require(price >= 0) { "Цена не может быть отрицательной" }
    }
}



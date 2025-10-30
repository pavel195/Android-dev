package tasks.oop.shop.domain

/**
 * Диск: название, количество треков, цена. Неизменяемый, валидируемый тип.
 */
data class Disc(
    override val name: String,
    val tracks: Int,
    override val price: Int
) : Product {
    init {
        require(name.isNotBlank()) { "Название диска не может быть пустым" }
        require(tracks > 0) { "Количество треков должно быть > 0" }
        require(price >= 0) { "Цена не может быть отрицательной" }
    }
}



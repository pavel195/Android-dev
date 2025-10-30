package tasks.delegation.teachers.domain

import java.time.LocalDate

enum class Gender { MALE, FEMALE, OTHER }

interface Teacher {
    val fullName: String
    val gender: Gender
    val avgScore: Double
    fun describe(): String
}

data class BaseTeacher(
    override val fullName: String,
    override val gender: Gender,
    override val avgScore: Double
) : Teacher {
    init {
        require(fullName.isNotBlank()) { "ФИО обязательно" }
        require(avgScore in 0.0..5.0) { "Средний балл должен быть в диапазоне 0..5" }
    }
    override fun describe(): String = "$fullName | пол: $gender | ср.балл: ${"%.2f".format(avgScore)}"
}

class WithCategory(
    private val base: Teacher,
    val lastConfirmed: LocalDate,
    val orderNo: String
) : Teacher by base {
    init { require(orderNo.isNotBlank()) { "Номер приказа обязателен" } }
    override fun describe(): String = base.describe() + " | категория: подтверждена $lastConfirmed, приказ №$orderNo"
}

class WithPhD(
    private val base: Teacher,
    val topic: String,
    val field: String,
    val defenseDate: LocalDate
) : Teacher by base {
    init {
        require(topic.isNotBlank()) { "Тема диссертации обязательна" }
        require(field.isNotBlank()) { "Научное направление обязательно" }
    }
    override fun describe(): String = base.describe() + " | канд. наук: тема '$topic', напр. '$field', защита $defenseDate"
}



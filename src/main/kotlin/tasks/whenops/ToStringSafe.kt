package tasks.whenops

/**
 * toStringSafe: безопасное строковое представление значения по правилам задачи.
 * - Int, Double, Boolean — стандартный toString()
 * - String — сама строка
 * - List<Any?> — "[элемент1, элемент2, ...]", элементы — через toStringSafe
 * - null — "null"
 * - остальные типы — пустая строка
 */
fun toStringSafe(value: Any?): String = when (value) {
    null -> "null"
    is Int, is Double, is Boolean -> value.toString()
    is String -> value
    is List<*> -> value.joinToString(prefix = "[", postfix = "]") { toStringSafe(it) }
    else -> ""
}

fun main() {
    println(toStringSafe(42))            // 42
    println(toStringSafe(3.14))          // 3.14
    println(toStringSafe(true))          // true
    println(toStringSafe("hello"))      // hello
    println(toStringSafe(listOf(1, null, listOf(false, 2)))) // [1, null, [false, 2]]
    println(toStringSafe(null))          // null
    println(toStringSafe(mapOf(1 to 2))) // ""
}



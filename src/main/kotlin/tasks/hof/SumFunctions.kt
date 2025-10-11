package tasks.hof

/**
 * Возвращает функцию — сумму переданных функций Int->Int.
 * Для x возвращает f1(x) + f2(x) + ... + fn(x).
 */
fun sumOfFunctions(vararg functions: (Int) -> Int): (Int) -> Int = { x ->
    functions.sumOf { it(x) }
}

fun main() {
    // Демонстрация
    // Пример: f(x)=x, g(x)=2x+1, h(x)=x*x; x=3 → 19
    val f: (Int) -> Int = { it }
    val g: (Int) -> Int = { 2 * it + 1 }
    val h: (Int) -> Int = { it * it }

    val s = sumOfFunctions(f, g, h)
    val x = 3
    // Ожидается: 3 + 7 + 9 = 19
    println(s(x))
}



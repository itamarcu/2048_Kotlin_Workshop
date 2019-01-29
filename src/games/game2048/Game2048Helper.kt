package games.game2048

import board.GameBoard

/**
 * This function moves all the non-null elements to the beginning of the list (by removing nulls) and merges equal elements.
 * The parameter 'double' specifies the way how to merge equal elements:
 * it returns a new element that should be present in the result list instead of two merged elements.
 *
 * If the function double("a") returns "aa",
 * then the function moveAndMergeEqual transforms the input in the following way:
 *   a, a, b -> aa, b
 *   a, null -> a
 *   b, null, a, a -> b, aa
 *   a, a, null, a -> aa, a
 *   a, null, a, a -> aa, a
 */
fun <T : Any> List<T?>.moveAndMergeEqual(double: (T) -> T): List<T> {
    val outcome = mutableListOf<T>()
    var lastWasMerged = false
    forEach { nextValue ->
        when {
            nextValue == null -> Unit
            !lastWasMerged && nextValue == outcome.lastOrNull() -> {outcome[outcome.size - 1] = double(nextValue); lastWasMerged = true}
            else -> {outcome.add(nextValue); lastWasMerged = false}
        }
    }
    return outcome
}

fun <T> GameBoard<T>.printAllBoard() {
    (1..width).forEach { i ->
        println(getRow(i, 1..width).joinToString(", ") { "${get(it)}" })
    }
    println()
}
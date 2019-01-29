package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game
import board.Direction.*

/**
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game)
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game executing 'PlayGame2048'
 * (or choosing the corresponding run configuration).
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
        Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)
    private val history = mutableListOf<GameBoard<Int?>>()

    override fun initialize() {
        history.clear()
        board.clear()
        repeat(2) {
            board.addNewValue(initializer)
        }
        history.add(board.copy())
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        history.add(board.copy())
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        } else {
            history.removeAt(history.size - 1)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }

    override fun undo() {
        if (history.isEmpty())
            return
        board.setEntireBoardFrom(history.last())
        history.removeAt(history.size - 1)
    }
}

/**
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    val (cell, value) = initializer.nextValue(this) ?: return // board is full; do nothing
    set(cell, value)
}

/**
 * Move values in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column), in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    val before = rowOrColumn.map { get(it) }
    val afterWithoutNulls = before.moveAndMergeEqual { it * 2 }
    val nullTail = List((width - afterWithoutNulls.size), { null })
    val after = afterWithoutNulls + nullTail
    if (before == after)
        return false
    rowOrColumn.forEachIndexed { i, cell -> set(cell, after[i]) }
    return true
}

/**
 * Move values by the rules of the 2048 game to the specified direction.
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    var somethingMoved = false
    (1..width).forEach { index ->
        val rowOrColumn = when (direction) {
            UP -> getColumn(1..width, index)
            DOWN -> getColumn(1..width, index).reversed()
            RIGHT -> getRow(index, 1..width).reversed()
            LEFT -> getRow(index, 1..width)
        }
        // boolean logic - no short-circuit on purpose
        somethingMoved = somethingMoved or moveValuesInRowOrColumn(rowOrColumn)
    }
    return somethingMoved
}
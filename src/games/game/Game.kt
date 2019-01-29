package games.game

import board.Direction

interface Game {
    fun initialize()
    fun canMove(): Boolean
    fun hasWon(): Boolean
    fun processMove(direction: Direction)
    fun undo() = Unit
    operator fun get(i: Int, j: Int): Int?
}

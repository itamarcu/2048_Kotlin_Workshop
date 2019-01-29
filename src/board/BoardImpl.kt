package board

import board.Direction.*

class CellImpl<T>(override val i: Int, override val j: Int, val value: T?) : Cell

open class SquareBoardImpl<T>(final override val width: Int) : SquareBoard {
    val cells: MutableMap<Pair<Int, Int>, CellImpl<T>> = mutableMapOf()

    init {
        initializeEmpty()
    }

    fun initializeEmpty() {
        cells.clear()
        (1..width).forEach { i ->
            (1..width).forEach { j ->
                cells[i to j] = CellImpl<T>(i, j, null)
            }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): CellImpl<T>? {
        return cells[i to j]
    }

    override fun getCell(i: Int, j: Int): CellImpl<T> {
        return getCellOrNull(i, j)!!
    }

    override fun getAllCells(): Collection<CellImpl<T>> {
        return cells.values
    }

    override fun getRow(i: Int, jRange: IntProgression): List<CellImpl<T>> {
        return jRange.mapNotNull { j -> cells[i to j] }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<CellImpl<T>> {
        return iRange.mapNotNull { i -> cells[i to j] }
    }

    override fun Cell.getNeighbour(direction: Direction): CellImpl<T>? {
        return when (direction) {
            UP -> cells[i to j - 1]
            DOWN -> cells[i to j + 1]
            RIGHT -> cells[i + 1 to j]
            LEFT -> cells[i - 1 to j]
        }
    }
}

class GameBoardImpl<T>(width: Int) : GameBoard<T>, SquareBoardImpl<T>(width) {
    override fun get(cell: Cell): T? {
        return getCellOrNull(cell.i, cell.j)?.value
    }

    override fun set(cell: Cell, value: T?) {
        cells[cell.i to cell.j] = CellImpl(cell.i, cell.j, value)
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cells.values.filter { predicate(it.value) }
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return cells.values.find { predicate(it.value)}
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return cells.values.any { predicate(it.value)}
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return cells.values.all { predicate(it.value)}
    }

    override fun clear() {
        initializeEmpty()
    }

    override fun copy(): GameBoard<T> {
        val newBoard = GameBoardImpl<T>(width)
        newBoard.setEntireBoardFrom(this)
        return newBoard
    }

    override fun setEntireBoardFrom(other: GameBoard<T>) {
        (1..width).forEach { i ->
            (1..width).forEach { j ->
//                print("mine: ${cells[i to j]?.value}")
//                println("    other: ${other[cells[i to j]!!]}")
                cells[i to j] = CellImpl(i, j, other[cells[i to j]!!])
            }
        }
    }
}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl<Int>(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)


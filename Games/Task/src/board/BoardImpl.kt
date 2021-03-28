package board

import board.Direction.*
import java.lang.IllegalArgumentException
import kotlin.math.min

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl<T>(width)

data class SquareBoardImpl(override val width: Int) : SquareBoard {
    private val board: List<List<Cell>> = (1..width).toList()
        .map{i -> (1..width).toList().map { j -> Cell(i, j)  }}

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (!isCoordinateValid(i, j)) {
            return null
        }
        return this.board[i-1][j-1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        if (!isCoordinateValid(i, j)) {
            throw IllegalArgumentException("incorrect value: $i $j")
        }
        return this.board[i-1][j-1]
    }

    override fun getAllCells(): Collection<Cell> {
        return board.flatten()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        if (i !in 1..this.width) {
            throw IllegalArgumentException("incorrect value: $i $jRange")
        }

        val jHi = min(this.width, jRange.last)
        val jLo = min(this.width, jRange.first)
        val jRangeCapped = if (jHi > jLo) (jLo..jHi) else (jLo downTo jHi)
        return jRangeCapped.map { j ->
            this.board[i-1][j-1]
        }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        if (j !in 1..this.width) {
            throw IllegalArgumentException("incorrect value: $j $iRange")
        }
        val iHi = min(this.width, iRange.last)
        val iLo = min(this.width, iRange.first)
        val iRangeCapped = if (iHi > iLo) (iLo..iHi) else (iLo downTo iHi)
        return iRangeCapped.map { i -> this.board[i-1][j-1] }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when(direction) {
            DOWN -> getCellOrNull(this.i+1, this.j)
            UP -> getCellOrNull(this.i-1, this.j)
            LEFT -> getCellOrNull(this.i, this.j-1)
            RIGHT -> getCellOrNull(this.i, this.j+1)
        }
    }

    private fun isCoordinateValid(i: Int, j: Int): Boolean {
        return i in 1..this.width && j in 1..this.width
    }
}

data class GameBoardImpl<T>(override val width: Int) : GameBoard<T> {
    private val board = createSquareBoard(width)
    private val map: MutableMap<Cell, T?> = mutableMapOf()

    init {
        for (cell in board.getAllCells()) {
            map[cell] = null
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return board.getCellOrNull(i, j)
    }

    override fun getCell(i: Int, j: Int): Cell {
        return board.getCell(i, j)
    }

    override fun getAllCells(): Collection<Cell> {
        return board.getAllCells()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return board.getRow(i, jRange)
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return board.getColumn(iRange, j)
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when(direction) {
            DOWN -> getCellOrNull(this.i+1, this.j)
            UP -> getCellOrNull(this.i-1, this.j)
            LEFT -> getCellOrNull(this.i, this.j-1)
            RIGHT -> getCellOrNull(this.i, this.j+1)
        }
    }

    override fun get(cell: Cell): T? {
        return map[cell]
    }

    override fun set(cell: Cell, value: T?) {
        map[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return map.filter { (_, value) -> predicate(value) }.keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return map.filterValues{predicate(it)}.keys.first()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return  map.values.any(predicate)
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return  map.values.all(predicate)
    }
}
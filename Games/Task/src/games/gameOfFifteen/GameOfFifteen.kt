package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        val xs = initializer.initialPermutation
        var i = 0
        for (j in 1..4) {
            for (k in 1..4) {
                if (i == 15) {
                    break
                }
                board[Cell(j, k)] = xs[i]
                i++
            }
        }
    }

    override fun canMove() = true

    override fun hasWon(): Boolean {
        var i = 1
        for (j in 1..4) {
            for (k in 1..4) {
                if (i > 15) {
                    break
                }
                if (board[Cell(j, k)] != i) {
                    return false
                }
                i++
            }
        }
        return true
    }

    override fun processMove(direction: Direction) {
        val emptyCell = findEmptyCell()
        when (direction) {
            Direction.LEFT -> {
                if(emptyCell.j == 4) {
                    return
                }
                val cellToMove = Cell(emptyCell.i, emptyCell.j + 1)
                board[emptyCell] = board[cellToMove]
                board[cellToMove] = null
            }
            Direction.RIGHT -> {
                if(emptyCell.j == 0) {
                    return
                }
                val cellToMove = Cell(emptyCell.i, emptyCell.j - 1)
                board[emptyCell] = board[cellToMove]
                board[cellToMove] = null
            }
            Direction.UP -> {
                if(emptyCell.i == 4) {
                    return
                }
                val cellToMove = Cell(emptyCell.i + 1, emptyCell.j)
                board[emptyCell] = board[cellToMove]
                board[cellToMove] = null
            }
            Direction.DOWN -> {
                if(emptyCell.i == 0) {
                    return
                }
                val cellToMove = Cell(emptyCell.i - 1, emptyCell.j)
                board[emptyCell] = board[cellToMove]
                board[cellToMove] = null
            }
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }

    private fun findEmptyCell(): Cell {
        for (cell in board.getAllCells()) {
            if (board[cell] == null) {
                return cell
            }
        }
        throw IllegalStateException("cannot find empty cell")
    }
}

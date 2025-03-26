package edu.kpi.tetris.controller

import edu.kpi.tetris.logic.board.Board
import edu.kpi.tetris.logic.board.BoardManager
import edu.kpi.tetris.logic.board.BoardParser
import edu.kpi.tetris.view.InputManager
import edu.kpi.tetris.view.OutputManager

class GameController(
    private val inputManager: InputManager,
    private val outputManager: OutputManager
) {
    private var board: Board? = null
    private var finished = false

    private fun move() {
        val moved = BoardManager.moveFigure(this.board!!)

        if (moved == null) {
            this.finished = true
        } else {
            this.board = moved
        }
    }

    fun execute(source: String?, target: String?, outputSteps: Boolean = false) {
        this.finished = false

        val dataFromSource: List<String> = this.inputManager.read(source)

        this.board = BoardParser.parseList(dataFromSource)
        BoardManager.validateFigure(this.board!!)

        while (!this.finished) {
            if (outputSteps) this.outputManager.append(target, this.board!!)

            this.move()
        }

        if (this.board == null) {
            throw IllegalStateException("Board can't be null")
        }

        if (!outputSteps) this.outputManager.write(target, this.board!!)
    }

    fun isFinished(): Boolean {
        return this.finished
    }
}
package edu.kpi.tetris.view

import edu.kpi.tetris.logic.board.Board

interface OutputManager {
    fun write(target: String?, content: Board)
    fun append(target: String?, content: Board)
}
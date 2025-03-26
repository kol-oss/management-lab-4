package edu.kpi.tetris.random.utils

import edu.kpi.tetris.logic.board.Board
import edu.kpi.tetris.logic.pixel.PixelTypeEnum
import kotlin.test.assertEquals

class RandomVerifier {
    companion object {
        fun verifyBoards(input: Board, actual: Board) {
            val pixels = input.getPixels()
            val figurePixels = pixels.filter { it.getType() == PixelTypeEnum.PIECE }

            val figureBorderPieces = figurePixels.filter {
                input.getBelowNeighbour(it.x, it.y) == null ||
                input.getBelowNeighbour(it.x, it.y)!!.getType() != PixelTypeEnum.PIECE
            }

            val minChange = figureBorderPieces.minOfOrNull { piece ->
                var change = 0
                var current = piece
                var below = input.getBelowNeighbour(current.x, current.y)

                while (below != null && below.getType() != PixelTypeEnum.LAND) {
                    change++
                    current = below
                    below = input.getBelowNeighbour(current.x, current.y)
                }
                change
            } ?: 0

            val expected = Board(input.width, input.height)


            val landPixels = pixels.filter { it.getType() == PixelTypeEnum.LAND }
            for (piece in landPixels) {
                expected.set(piece.x, piece.y, piece.getType())
            }

            for (piece in figurePixels) {
                expected.set(piece.x, piece.y + minChange, piece.getType())
            }

            assertEquals(expected, actual)
        }
    }
}
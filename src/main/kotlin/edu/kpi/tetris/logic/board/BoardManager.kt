package edu.kpi.tetris.logic.board

import edu.kpi.tetris.logic.pixel.Pixel
import edu.kpi.tetris.logic.pixel.PixelTypeEnum
import java.util.*
import java.util.stream.Collectors

class BoardManager {
    companion object {
        fun moveFigure(board: Board): Board? {
            val result = Board(board.width, board.height)
            val pixels = board.getPixels()

            for (pixel in pixels) {
                when (pixel.getType()) {
                    PixelTypeEnum.LAND -> result.set(pixel.x, pixel.y, pixel.getType())
                    PixelTypeEnum.PIECE -> {
                        val belowPixel = board.getBelowNeighbour(pixel.x, pixel.y) ?: return null

                        if (belowPixel.getType() == PixelTypeEnum.EMPTY || belowPixel.getType() == PixelTypeEnum.PIECE) {
                            result.set(belowPixel.x, belowPixel.y, PixelTypeEnum.PIECE)
                        } else {
                            return null
                        }
                    }

                    PixelTypeEnum.EMPTY -> {}
                }
            }

            if (result == board) return null
            return result
        }

        fun validateFigure(board: Board) {
            val pieces = board.getPixels().stream()
                .filter { it.getType() == PixelTypeEnum.PIECE }
                .collect(Collectors.toList())

            if (pieces.size == 0) return

            val queue: Queue<Pixel> = LinkedList()
            val found: MutableSet<Pixel> = mutableSetOf()

            queue.add(pieces[0])

            while (queue.size > 0) {
                val current = queue.poll()
                found.add(current)

                for (neighbour in board.getNeighbours(current.x, current.y)) {
                    if (neighbour.getType() != PixelTypeEnum.PIECE) continue

                    if (!found.contains(neighbour)) {
                        queue.add(neighbour)
                    }
                }
            }

            if (pieces.size != found.size) {
                throw IllegalStateException("Pieces must create only one figure on the board")
            }
        }
    }
}
package edu.kpi.tetris.random.utils

import edu.kpi.tetris.logic.board.Board
import edu.kpi.tetris.logic.pixel.Pixel
import edu.kpi.tetris.logic.pixel.PixelTypeEnum
import java.util.*

class RandomGenerator {
    companion object {
        private val random = Random()

        private fun generateFigure(board: Board, pixelCount: Int) {
            val pixels: List<Pixel> = board.getPixels()
            if (pixels.isEmpty()) return

            var start: Pixel = pixels.random()
            while (start.getType() != PixelTypeEnum.EMPTY) {
                start = pixels.random()
            }

            val queue: Deque<Pixel> = LinkedList()
            queue.add(start)

            val foundPixels = mutableSetOf<Pixel>()

            while (foundPixels.size < pixelCount && queue.isNotEmpty()) {
                val pixel: Pixel = queue.poll()
                foundPixels.add(pixel)
                board.set(pixel.x, pixel.y, PixelTypeEnum.PIECE)

                for (neighbour in board.getNeighbours(pixel.x, pixel.y).shuffled()) {
                    queue.addFirst(neighbour)

                    if (foundPixels.contains(neighbour)) continue
                }
            }

            if (foundPixels.size < pixelCount) {
                throw IllegalStateException("Can not generate figure with $pixelCount pixels")
            }
        }

        private fun generateLand(board: Board, pixelCount: Int) {
            val pixels: List<Pixel> = board.getPixels().filter { it.getType() != PixelTypeEnum.PIECE }
            if (pixels.isEmpty()) return

            if (pixels.size < pixelCount) {
                throw IllegalStateException("Can not generate land with $pixelCount pixels")
            }

            var added: Int = 0
            while (added < pixelCount) {
                val pixel: Pixel = pixels.random()
                if (pixel.getType() == PixelTypeEnum.LAND) continue

                board.set(pixel.x, pixel.y, PixelTypeEnum.LAND)
                added++
            }
        }

        fun generate(width: Int, height: Int): Board {
            val pixelsNumber = width * height
            val board = Board(width, height)

            val figurePixels = this.random.nextInt(pixelsNumber).coerceAtLeast(1)
            this.generateFigure(board, figurePixels)
            if (figurePixels == pixelsNumber) return board

            val landPixels = this.random.nextInt(pixelsNumber - figurePixels).coerceAtLeast(1)
            this.generateLand(board, landPixels)

            return board
        }
    }
}

package edu.kpi.tetris.logic.board

import edu.kpi.tetris.logic.pixel.PixelTypeEnum

class BoardParser {
    companion object {
        fun parseList(lines: List<String>): Board {
            val width: Int = if (lines.isNotEmpty()) lines[0].length else 0
            val height: Int = lines.size

            val result = Board(width, height)

            for (y in 0 until height) {
                val line: String = lines[y].trim()
                if (line.length != width) {
                    throw IllegalArgumentException("Line $line does not match expected width $width")
                }

                for (x in 0 until width) {
                    val symbol: String = lines[y][x].toString()
                    val type: PixelTypeEnum = PixelTypeEnum.fromString(symbol)
                        ?: throw IllegalArgumentException("Unknown symbol: $symbol")

                    result.set(x, y, type)
                }
            }

            return result
        }
    }
}
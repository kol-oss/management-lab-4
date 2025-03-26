package edu.kpi.tetris.logic.board

import edu.kpi.tetris.logic.pixel.Pixel
import edu.kpi.tetris.logic.pixel.PixelTypeEnum

class Board(
    val width: Int,
    val height: Int
) {
    private val pixels: Array<Array<Pixel>> = Array(this.height) { row ->
        Array(this.width) { col ->
            Pixel(col, row, PixelTypeEnum.EMPTY)
        }
    }

    private fun validate(x: Int, y: Int) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
            throw IndexOutOfBoundsException("Invalid index, expected from (0,0) to (${width-1},${height-1}), got (${x},${y})")
        }
    }

    fun get(x: Int, y: Int): PixelTypeEnum {
        this.validate(x, y)

        return this.pixels[y][x].getType()
    }

    fun set(x: Int, y: Int, value: PixelTypeEnum) {
        this.validate(x, y)
        val current: Pixel = this.pixels[y][x]

        current.setType(value)
    }

    fun getPixels(): List<Pixel> {
        val result: ArrayList<Pixel> = ArrayList()

        for (y in 0 until this.height) {
            for (x in 0 until this.width) {
                result.add(this.pixels[y][x])
            }
        }

        return result
    }

    fun getNeighbours(x: Int, y: Int): ArrayList<Pixel> {
        val result: ArrayList<Pixel> = ArrayList()
        val neighboursCoords = listOf(
            intArrayOf(0, -1),
            intArrayOf(-1, 0),
            intArrayOf(1, 0),
            intArrayOf(0, 1),
        )

        for (neighbourCord in neighboursCoords) {
            try {
                val neighX = x + neighbourCord[0]
                val neighY = y + neighbourCord[1]
                this.validate(neighX, neighY)

                result.add(this.pixels[neighY][neighX])
            } catch (exception: IndexOutOfBoundsException) {
                continue
            }
        }

        return result
    }

    fun getBelowNeighbour(x: Int, y: Int): Pixel? {
        this.validate(x, y)
        if (y + 1 >= this.height) {
            return null
        }

        return this.pixels[y+1][x]
    }

    fun clone(): Board {
        val cloned = Board(width, height)

        for (y in 0 until this.height) {
            for (x in 0 until this.width) {
                cloned.set(x, y, this.get(x, y))
            }
        }

        return cloned
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Board) {
            return false
        }

        if (this.width != other.width || this.height != other.height) {
            return false
        }

        val curPixels = this.getPixels()
        val otherPixels = other.getPixels()

        for (i in curPixels.indices) {
            if (curPixels[i].getType() != otherPixels[i].getType()) {
                return false
            }
        }

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + pixels.contentDeepHashCode()
        return result
    }

    override fun toString(): String {
        return pixels.joinToString("\n") { row ->
            row.joinToString("") { pixel ->
                pixel.getType().toString()
            }
        }
    }
}
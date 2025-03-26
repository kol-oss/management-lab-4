package edu.kpi.tetris.logic

import edu.kpi.tetris.logic.board.Board
import edu.kpi.tetris.logic.pixel.Pixel
import edu.kpi.tetris.logic.pixel.PixelTypeEnum
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BoardTest {
    @Test
    fun testPixelSetAndGetOperations() {
        // Preconditions: create 3x3 board
        val board = Board(3, 3)

        // Test Case 1: testing get operation
        assertEquals(PixelTypeEnum.EMPTY, board.get(0, 0))
        assertEquals(PixelTypeEnum.EMPTY, board.get(1, 1))
        assertEquals(PixelTypeEnum.EMPTY, board.get(2, 0))

        // Test Case 2: testing set operation
        // .#.
        // #..
        // ...

        board.set(0, 1, PixelTypeEnum.LAND)
        board.set(1, 0, PixelTypeEnum.LAND)

        assertEquals(PixelTypeEnum.LAND, board.get(0, 1))
        assertEquals(PixelTypeEnum.LAND, board.get(1, 0))

        // Test Case 3: testing index-out-of-bound get
        assertThrows<IndexOutOfBoundsException> {
            board.get(0, -1)
        }

        assertThrows<IndexOutOfBoundsException> {
            board.get(3, 3)
        }

        // Test Case 4: testing get all pixels
        val pixels = board.getPixels()

        assertEquals(9, pixels.size)
        assertEquals(PixelTypeEnum.EMPTY, pixels[0].getType())
        assertEquals(PixelTypeEnum.LAND, pixels[1].getType())
        assertEquals(PixelTypeEnum.LAND, pixels[3].getType())
    }

    @Test
    fun testGetBelowNeighbour() {
        // Preconditions: create 3x3 board
        // .p.
        // p..
        // ...

        val board = Board(3, 3)
        board.set(0, 1, PixelTypeEnum.PIECE)
        board.set(1, 0, PixelTypeEnum.PIECE)

        // Test Case 1: testing get valid below neighbour
        val firstBelowPixel: Pixel? = board.getBelowNeighbour(0, 0)
        assertNotNull(firstBelowPixel)
        assertEquals(PixelTypeEnum.PIECE, firstBelowPixel.getType())

        val thirdBelowPixel = board.getBelowNeighbour(2, 0)
        assertNotNull(thirdBelowPixel)
        assertEquals(PixelTypeEnum.EMPTY, thirdBelowPixel.getType())

        // Test Case 2: testing get invalid below neighbour
        val invalidPixel = board.getBelowNeighbour(2, 2)
        assertNull(invalidPixel)
    }

    @Test
    fun testBoardToString() {
        // Test Case 1: testing empty board string
        val emptyBoard = Board(2, 2)
        assertEquals("..\n..", emptyBoard.toString())

        // Test Case 2: testing filled board string
        // .p.
        // .p.
        // #..

        val filledBoard = Board(3, 3)
        filledBoard.set(1, 0, PixelTypeEnum.PIECE)
        filledBoard.set(1, 1, PixelTypeEnum.PIECE)
        filledBoard.set(0, 2, PixelTypeEnum.LAND)

        assertEquals(".p.\n.p.\n#..", filledBoard.toString())
    }
}
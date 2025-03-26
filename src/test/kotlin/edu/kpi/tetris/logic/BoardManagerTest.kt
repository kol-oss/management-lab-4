package edu.kpi.tetris.logic

import edu.kpi.tetris.logic.board.Board
import edu.kpi.tetris.logic.board.BoardManager
import edu.kpi.tetris.logic.board.BoardParser
import edu.kpi.tetris.logic.pixel.PixelTypeEnum
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class BoardManagerTest {
    @Test
    fun testMoveSimpleFigure() {
        // Preconditions: creating board
        // .p.
        // ...
        // ###

        val board = Board(3, 3)

        board.set(1, 0, PixelTypeEnum.PIECE)
        board.set(0, 2, PixelTypeEnum.LAND)
        board.set(1, 2, PixelTypeEnum.LAND)
        board.set(2, 2, PixelTypeEnum.LAND)

        // Test Case 1: testing moving piece to empty block
        val movedToEmpty = BoardManager.moveFigure(board)

        assertNotNull(movedToEmpty)

        assertEquals(PixelTypeEnum.EMPTY, movedToEmpty!!.get(1, 0))
        assertEquals(PixelTypeEnum.PIECE, movedToEmpty.get(1, 1))
        assertEquals(PixelTypeEnum.LAND, movedToEmpty.get(0, 2))
        assertEquals(PixelTypeEnum.LAND, movedToEmpty.get(1, 2))
        assertEquals(PixelTypeEnum.LAND, movedToEmpty.get(2, 2))

        // Test Case 2: testing original board not changing
        assertEquals(PixelTypeEnum.PIECE, board.get(1, 0))
        assertEquals(PixelTypeEnum.EMPTY, board.get(1, 1))
        assertEquals(PixelTypeEnum.LAND, board.get(0, 2))
        assertEquals(PixelTypeEnum.LAND, board.get(1, 2))
        assertEquals(PixelTypeEnum.LAND, board.get(2, 2))

        // Test Case 3: testing moving piece to land block
        val movedToLand = BoardManager.moveFigure(movedToEmpty)

        assertNull(movedToLand)
    }

    @Test
    fun testMoveComplexFigure() {
        // Preconditions: creating complex board
        // .ppp.
        // ..p..
        // #...#
        // ##.##

        val board = Board(5, 4)

        board.set(1, 0, PixelTypeEnum.PIECE)
        board.set(2, 0, PixelTypeEnum.PIECE)
        board.set(3, 0, PixelTypeEnum.PIECE)
        board.set(2, 1, PixelTypeEnum.PIECE)

        board.set(0, 2, PixelTypeEnum.LAND)
        board.set(0, 3, PixelTypeEnum.LAND)
        board.set(1, 3, PixelTypeEnum.LAND)
        board.set(3, 3, PixelTypeEnum.LAND)
        board.set(4, 2, PixelTypeEnum.LAND)
        board.set(4, 3, PixelTypeEnum.LAND)

        // Test Case 1: testing moving piece to one step
        val movedToEmpty = BoardManager.moveFigure(board)

        assertNotNull(movedToEmpty)

        assertEquals(PixelTypeEnum.EMPTY, movedToEmpty!!.get(1, 0))
        assertEquals(PixelTypeEnum.EMPTY, movedToEmpty.get(2, 0))
        assertEquals(PixelTypeEnum.EMPTY, movedToEmpty.get(3, 0))

        assertEquals(PixelTypeEnum.PIECE, movedToEmpty.get(1, 1))
        assertEquals(PixelTypeEnum.PIECE, movedToEmpty.get(2, 1))
        assertEquals(PixelTypeEnum.PIECE, movedToEmpty.get(3, 1))
        assertEquals(PixelTypeEnum.PIECE, movedToEmpty.get(2, 2))

        // Test Case 2: testing moving piece to final step
        val movedToFinal = BoardManager.moveFigure(movedToEmpty)

        assertNotNull(movedToFinal)

        assertEquals(PixelTypeEnum.EMPTY, movedToFinal!!.get(1, 1))
        assertEquals(PixelTypeEnum.EMPTY, movedToFinal.get(2, 1))
        assertEquals(PixelTypeEnum.EMPTY, movedToFinal.get(3, 1))

        assertEquals(PixelTypeEnum.PIECE, movedToFinal.get(1, 2))
        assertEquals(PixelTypeEnum.PIECE, movedToFinal.get(2, 2))
        assertEquals(PixelTypeEnum.PIECE, movedToFinal.get(3, 2))
        assertEquals(PixelTypeEnum.PIECE, movedToFinal.get(2, 3))

        // Test Case 3: testing moving piece to land
        val movedToLand = BoardManager.moveFigure(movedToFinal)

        assertNull(movedToLand)
    }

    @Test
    fun testMoveWithoutLand() {
        // Preconditions: creating board
        // ...
        // .p.
        // ...

        val board = Board(3, 3)

        board.set(1, 1, PixelTypeEnum.PIECE)

        // Test Case 1: testing moving to the end of the board
        val movedToTheEnd = BoardManager.moveFigure(board)

        assertEquals(PixelTypeEnum.PIECE, movedToTheEnd!!.get(1, 2))
        assertEquals(PixelTypeEnum.EMPTY, movedToTheEnd.get(1, 1))

        // Test Case 2: testing moving piece out of bounds
        val movedOutOfBounds = BoardManager.moveFigure(movedToTheEnd)

        assertNull(movedOutOfBounds)
    }

    @Test
    fun testMoveInvalidFigure() {
        // Preconditions: creating invalid board
        // .....
        // .....
        // .....
        // .....

        val board = Board(5, 4)

        // Test Case 1: testing no pieces board
        val movedNoPieces = BoardManager.moveFigure(board)

        assertNull(movedNoPieces)
    }

    @Test
    fun testValidFigureValidation() {
        // Test Case 1: testing validation of empty board
        BoardManager.validateFigure(BoardParser.parseList(listOf(
            ".....",
            ".....",
            ".....",
            "#####"
        )))

        // Test Case 2: testing valid board validation
        BoardManager.validateFigure(BoardParser.parseList(listOf(
            "ppp..",
            "#p#..",
            "#.#..",
            "###.."
        )))

        BoardManager.validateFigure(BoardParser.parseList(listOf(
            ".pp..",
            ".pp..",
            "..#..",
            ".###."
        )))
    }

    @Test
    fun testInvalidFigureValidation() {
        // Test Case 1: testing invalid figure validation
        assertThrows<IllegalStateException> {
            BoardManager.validateFigure(BoardParser.parseList(listOf(
                "pp..p",
                "....p",
                ".....",
                "#####"
            )))
        }

        // Test Case 2: testing multiple invalid figures validation
        assertThrows<IllegalStateException> {
            BoardManager.validateFigure(BoardParser.parseList(listOf(
                "p...p",
                "...p.",
                ".p...",
                "#####"
            )))
        }

        // Test Case 3: testing wrong figure format validation
        assertThrows<IllegalStateException> {
            BoardManager.validateFigure(BoardParser.parseList(listOf(
                ".p.p.",
                "..p..",
                ".p.p.",
                "#####"
            )))
        }
    }
}
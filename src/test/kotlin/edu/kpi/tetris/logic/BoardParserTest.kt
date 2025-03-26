package edu.kpi.tetris.logic

import edu.kpi.tetris.logic.board.Board
import edu.kpi.tetris.logic.board.BoardParser
import edu.kpi.tetris.logic.pixel.PixelTypeEnum
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BoardParserTest {
    @Test
    fun testParseValidList() {
        // Test Case 1: testing parsing empty list
        val emptyBoard: Board = BoardParser.parseList(listOf())
        assertEquals(Board(0, 0), emptyBoard)

        // Test Case 2: testing parsing simple content

        // .p
        // #p

        val simpleBoard = Board(2, 2)
        simpleBoard.set(0, 1, PixelTypeEnum.LAND)
        simpleBoard.set(1, 0, PixelTypeEnum.PIECE)
        simpleBoard.set(1, 1, PixelTypeEnum.PIECE)

        val simpleBoardString = simpleBoard.toString().split("\n")
        assertEquals(simpleBoard, BoardParser.parseList(simpleBoardString))

        // Test Case 3: testing parsing complex content

        // ..pp.
        // ...pp
        // ##...
        // #...#

        val complexBoard = Board(5, 4)
        complexBoard.set(0, 2, PixelTypeEnum.LAND)
        complexBoard.set(0, 3, PixelTypeEnum.LAND)
        complexBoard.set(1, 2, PixelTypeEnum.LAND)
        complexBoard.set(3, 3, PixelTypeEnum.LAND)

        complexBoard.set(2, 0, PixelTypeEnum.PIECE)
        complexBoard.set(3, 0, PixelTypeEnum.PIECE)
        complexBoard.set(3, 1, PixelTypeEnum.PIECE)
        complexBoard.set(4, 1, PixelTypeEnum.PIECE)

        val complexBoardString = complexBoard.toString().split("\n")
        assertEquals(complexBoard, BoardParser.parseList(complexBoardString))
    }

    @Test
    fun testParseInvalidLength() {
        // Test Case 1: testing invalid row size
        assertThrows<IllegalArgumentException> {
            BoardParser.parseList(listOf(".p..", "...", "...."))
        }

        assertThrows<IllegalArgumentException> {
            BoardParser.parseList(listOf(".", "..", "..."))
        }

        assertThrows<IllegalArgumentException> {
            BoardParser.parseList(listOf("...##.", "", "."))
        }
    }

    @Test
    fun testParseInvalidSymbols() {
        // Test Case 1: testing invalid symbols
        assertThrows<IllegalArgumentException> {
            BoardParser.parseList(listOf("...", ".x.", "..."))
        }

        assertThrows<IllegalArgumentException> {
            BoardParser.parseList(listOf(".p.", ".x.", "..."))
        }

        assertThrows<IllegalArgumentException> {
            BoardParser.parseList(listOf("_p_", ".x.", "###"))
        }
    }

    @Test
    fun testPixelTypeParsing() {
        // Test Case 1: testing allowed symbols parsing
        assertEquals(PixelTypeEnum.EMPTY, PixelTypeEnum.fromString("."))
        assertEquals(PixelTypeEnum.LAND, PixelTypeEnum.fromString("#"))
        assertEquals(PixelTypeEnum.PIECE, PixelTypeEnum.fromString("p"))

        // Test Case 2: testing not allowed symbols parsing
        assertNull(PixelTypeEnum.fromString(""))
        assertNull(PixelTypeEnum.fromString(","))

        assertNull(PixelTypeEnum.fromString("/"))
        assertNull(PixelTypeEnum.fromString("?"))

        assertNull(PixelTypeEnum.fromString("ppp"))
        assertNull(PixelTypeEnum.fromString("P"))

        // Test Case 3: testing pixel to string
        assertEquals(".", PixelTypeEnum.fromString(".").toString())
        assertEquals("#", PixelTypeEnum.fromString("#").toString())
        assertEquals("p", PixelTypeEnum.fromString("p").toString())

    }
}
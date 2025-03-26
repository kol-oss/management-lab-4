package edu.kpi.tetris.controller

import edu.kpi.tetris.logic.board.Board
import edu.kpi.tetris.logic.board.BoardParser
import edu.kpi.tetris.view.FileManager
import edu.kpi.tetris.view.InputManager
import edu.kpi.tetris.view.OutputManager
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.test.assertEquals

class GameControllerTest {
    @Test
    fun testSimpleStubExecution() {
        // Preconditions: creating simple boards and stubs managers
        val input = listOf(
            ".ppp.",
            "..p..",
            ".....",
            ".....",
            "#####"
        )

        val expected = listOf(
            ".....",
            ".....",
            ".ppp.",
            "..p..",
            "#####"
        )

        var result: Board? = null

        val stubInputManager: InputManager = mockk()
        every { stubInputManager.read(any()) } returns input

        val stubOutputManager: OutputManager = mockk()
        every { stubOutputManager.write(any(), any()) } answers {
            result = secondArg()
        }

        val controller = GameController(stubInputManager, stubOutputManager)
        controller.execute(null, null)

        // Test Case 1: testing reaching final stage
        assertEquals(BoardParser.parseList(expected), result)
        assertTrue(controller.isFinished())

        verify(exactly = 1) { stubInputManager.read(any()) }
        verify(exactly = 1) { stubOutputManager.write(any(), any()) }

        // Test Case 2: testing reusing controller
        controller.execute(null, null)

        assertEquals(BoardParser.parseList(expected), result)
        assertTrue(controller.isFinished())

        verify(exactly = 2) { stubInputManager.read(any()) }
        verify(exactly = 2) { stubOutputManager.write(any(), any()) }
    }

    @Test
    fun testComplexStubExecution() {
        // Preconditions: creating complex boards and stubs managers
        val input = listOf(
            "..p.....",
            ".ppp....",
            "..p.....",
            "........",
            "...#....",
            "...#...#",
            "#..#####"
        )

        val expected = listOf(
            "........",
            "........",
            "..p.....",
            ".ppp....",
            "..p#....",
            "...#...#",
            "#..#####"
        )

        var result: Board? = null

        val stubInputManager: InputManager = mockk()
        every { stubInputManager.read(any()) } returns input

        val stubOutputManager: OutputManager = mockk()
        every { stubOutputManager.write(any(), any()) } answers {
            result = secondArg()
        }

        val controller = GameController(stubInputManager, stubOutputManager)
        controller.execute(null, null)

        // Test Case 1: testing valid complex stub execution
        assertEquals(BoardParser.parseList(expected), result)
        assertTrue(controller.isFinished())

        verify(exactly = 1) { stubInputManager.read(any()) }
        verify(exactly = 1) { stubOutputManager.write(any(), any()) }
    }

    @Test
    fun testStepsStubExecution() {
        // Preconditions: creating complex boards and stubs managers
        val input = listOf(
            "..p.....",
            ".ppp....",
            "..p.....",
            "........",
            "...#....",
            "...#...#",
            "#..#####"
        )

        val firstStepExpected = listOf(
            "........",
            "..p.....",
            ".ppp....",
            "..p.....",
            "...#....",
            "...#...#",
            "#..#####"
        )

        val secondStepExpected = listOf(
            "........",
            "........",
            "..p.....",
            ".ppp....",
            "..p#....",
            "...#...#",
            "#..#####"
        )

        val steps: ArrayList<Board?> = ArrayList()

        val stubInputManager: InputManager = mockk()
        every { stubInputManager.read(any()) } returns input

        val stubOutputManager: OutputManager = mockk()
        every { stubOutputManager.append(any(), any()) } answers {
            steps.add(secondArg())
        }

        val controller = GameController(stubInputManager, stubOutputManager)
        controller.execute(null, null, true)

        // Test Case 1: testing steps of the board
        verify(exactly = 1) { stubInputManager.read(any()) }
        verify(exactly = 3) { stubOutputManager.append(any(), any()) }

        // first step must be the copy of the input
        assertEquals(BoardParser.parseList(input), steps[0])
        assertEquals(BoardParser.parseList(firstStepExpected), steps[1])
        assertEquals(BoardParser.parseList(secondStepExpected), steps[2])

        assertTrue(controller.isFinished())
    }

    @Test
    fun testInvalidStubExecution() {
        // Preconditions: creating invalid board (multiple figures) and stubs managers
        val input = listOf(
            "..p...pp",
            ".ppp...p",
            "..p....p",
            "........",
            "...#....",
            "...#...#",
            "#..#####"
        )

        val stubInputManager: InputManager = mockk()
        every { stubInputManager.read(any()) } returns input

        // call of any function will cause error
        val stubOutputManager: OutputManager = mockk()

        // Test Case 1: testing execution with invalid figure
        val controller = GameController(stubInputManager, stubOutputManager)
        assertThrows<IllegalStateException> {
            controller.execute(null, null)
        }

        verify(exactly = 1) { stubInputManager.read(any()) }
    }

    @Test
    fun testFileExecution() {
        // Preconditions: creating controller with file I/O manager
        val manager = FileManager() // serves both for input and output
        val controller = GameController(manager, manager)

        // Test Case 1: testing valid execution
        val inputFile = File.createTempFile("test-input", ".txt")
        inputFile.deleteOnExit()

        inputFile.writeText("3 3\n.p.\n...\n###")

        val outputFile = File.createTempFile("test-output", ".txt")
        outputFile.deleteOnExit()

        controller.execute(inputFile.absolutePath, outputFile.absolutePath)

        assertEquals("...\n.p.\n###", outputFile.readText())

        // Test Case 2: testing invalid executions (source or target files are not defined)
        assertThrows<IllegalArgumentException> {
            controller.execute(inputFile.absolutePath, null)
        }

        assertThrows<IllegalArgumentException> {
            controller.execute(null, outputFile.absolutePath)
        }
    }

    @Test
    fun testManagersCalling() {
        // Preconditions: creating stubs for I/O operations
        var result: Board? = null

        val stubInputManager: InputManager = mockk()
        every { stubInputManager.read(any()) } returns listOf(".p.", "...", "###")

        val stubOutputManager: OutputManager = mockk()
        every { stubOutputManager.write(any(), any()) } answers {
            result = secondArg()
        }

        val controller = GameController(stubInputManager, stubOutputManager)

        // Test Case 1: testing game controller's managers
        controller.execute(null, null) // names are not required in stubs

        verify(exactly = 1) { stubInputManager.read(any()) }
        verify(exactly = 1) { stubOutputManager.write(any(), any()) }

        // Test Case 2: testing passing result to the write operation
        val expected = BoardParser.parseList(listOf("...", ".p.", "###"))

        assertEquals(expected, result)
    }

    @Test
    fun testManagersInvalidCalling() {
        // Preconditions: creating stubs for I/O operations
        val stubInputManager: InputManager = mockk()
        // invalid board size
        every { stubInputManager.read(any()) } returns listOf(".p.", "...", "##")

        // output methods should not be called
        val stubOutputManager: OutputManager = mockk()

        val controller = GameController(stubInputManager, stubOutputManager)

        // Test Case 1: testing managers after exception is thrown
        assertThrows<IllegalArgumentException> {
            controller.execute(null, null) // names are not required in stubs
        }

        verify(exactly = 1) { stubInputManager.read(any()) }
    }

    @AfterEach
    fun removeMocks() {
        clearAllMocks()
    }
}
package edu.kpi.tetris.random

import edu.kpi.tetris.controller.GameController
import edu.kpi.tetris.logic.board.Board
import edu.kpi.tetris.random.utils.RandomGenerator
import edu.kpi.tetris.random.utils.RandomVerifier
import edu.kpi.tetris.view.InputManager
import edu.kpi.tetris.view.OutputManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.*

class RandomGameControllerTest {
    private val random = Random()

    @Test
    fun testRandomGameController() {
        // Preconditions: configuring test params
        val testsNumber: Int = 100
        val maxWidth: Int = 16
        val maxHeight: Int = 16
        val logging: Boolean = true

        var controller: GameController

        var input: Board;
        var result: Board?

        // Preconditions: creating mocks
        val stubInputManager: InputManager = mockk()

        val stubOutputManager: OutputManager = mockk()
        every { stubOutputManager.write(any(), any()) } answers {
            result = secondArg()
        }

        // Test Case 1: running randomized tests
        for (i in 0 until testsNumber) {
            result = null

            val width = this.random.nextInt(maxWidth).coerceAtLeast(1)
            val height = this.random.nextInt(maxHeight).coerceAtLeast(1)

            input = RandomGenerator.generate(width, height)
            every { stubInputManager.read(any()) } returns input.toString().split("\n")

            controller = GameController(stubInputManager, stubOutputManager)
            controller.execute(null, null)

            if (logging) {
                println("Case #$i (width: $width, height: $height):")
                println("> Input:\n$input")
                println("> Result:\n$result")
            }

            // Assert 1: verifying current random board result
            RandomVerifier.verifyBoards(input, result!!)
        }

        // Assert 2: verifying total mocks callings
        verify(exactly = testsNumber) { stubInputManager.read(any()) }
        verify(exactly = testsNumber) { stubOutputManager.write(any(), any()) }
    }
}

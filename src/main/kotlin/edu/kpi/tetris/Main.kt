package edu.kpi.tetris

import edu.kpi.tetris.controller.GameController
import edu.kpi.tetris.view.ConsoleManager
import edu.kpi.tetris.view.FileManager
import edu.kpi.tetris.view.InputManager
import edu.kpi.tetris.view.OutputManager

fun main(args: Array<String>) {
    val mode = args.getOrNull(0) ?: "CONSOLE"

    var inputManager: InputManager = ConsoleManager()
    var outputManager: OutputManager = ConsoleManager()

    var input: String? = null
    var output: String? = null

    var steps = false

    if (mode == "FILE") {
        inputManager = FileManager()
        outputManager = FileManager()

        input = args.getOrNull(1) ?: "src/main/resources/input.txt"
        output = args.getOrNull(2) ?: "result.txt"
        steps = (args.getOrNull(3) ?: "false").lowercase() == "true"
    } else if (mode == "CONSOLE") {
        inputManager = ConsoleManager()
        outputManager = ConsoleManager()

        steps = (args.getOrNull(1) ?: "false").lowercase() == "true"
    }

    val controller = GameController(inputManager, outputManager)

    controller.execute(input, output, steps)
}
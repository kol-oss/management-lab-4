package edu.kpi.tetris.view

import edu.kpi.tetris.logic.board.Board

// made only for example, no tests defined
class ConsoleManager : InputManager, OutputManager {
    override fun read(source: String?): List<String> {
        print("Enter height and width: ")

        val (height, width) = readln()
            .split(InputManager.PARAMS_DELIMITER_REGEX)
            .takeIf { it.size == 2 }
            ?.mapNotNull(String::toIntOrNull)
            ?: throw IllegalArgumentException("There must be exactly 2 integer parameters")

        val content: ArrayList<String> = ArrayList()

        println("Enter board content ($height lines, $width symbols in line):")
        for (i in 0..<height) {
            val line: String = readln().trim()
            if (line.length != width) {
                throw IllegalArgumentException("Row size must be $width symbols")
            }

            content.add(line)
        }

        return content
    }

    override fun write(target: String?, content: Board) {
        println(content)
    }

    override fun append(target: String?, content: Board) {
        println(content)
    }
}
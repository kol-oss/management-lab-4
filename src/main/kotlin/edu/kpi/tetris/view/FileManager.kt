package edu.kpi.tetris.view

import edu.kpi.tetris.logic.board.Board
import java.io.File
import java.io.FileNotFoundException
import java.nio.charset.Charset

class FileManager : InputManager, OutputManager {
    companion object {
        val CHARSET: Charset = Charsets.UTF_8
    }

    private fun getFile(fileName: String): File {
        val file = File(fileName)

        if (!file.exists()) {
            throw FileNotFoundException("File $fileName does not exist")
        } else if (file.isDirectory) {
            throw FileNotFoundException("File $fileName is a directory")
        }

        return file
    }

    override fun read(source: String?): List<String> {
        if (source.isNullOrEmpty()) {
            throw IllegalArgumentException("Source file name $source is null or empty")
        }

        val lines: List<String> = this.getFile(source)
            .readLines(CHARSET)
            .ifEmpty { throw IllegalStateException("File $source is empty") }

        val (height, width) = lines.first().trim()
            .split(InputManager.PARAMS_DELIMITER_REGEX)
            .takeIf { it.size == 2 }
            ?.mapNotNull(String::toIntOrNull)
            ?: throw IllegalStateException("File $source: there must be exactly 2 integer parameters")

        if (width <= 0 || height <= 0) {
            throw IllegalStateException("File $source: params must be positive")
        }

        if (lines.size - 1 < height) {
            throw IllegalStateException("File $source: content must have height $height but was ${lines.size - 1}")
        }

        val content = lines.drop(1)
            .map { it.trim() }
            .onEach { row ->
                if (row.length != width) {
                    throw IllegalStateException("File $source: row must have width $width but was ${row.length}")
                }
            }
            .take(height)

        return content
    }

    override fun write(target: String?, content: Board) {
        if (target.isNullOrEmpty()) {
            throw IllegalArgumentException("Target file name $target is null or empty")
        }

        File(target).writeText(content.toString(), CHARSET)
    }

    override fun append(target: String?, content: Board) {
        if (target.isNullOrEmpty()) {
            throw IllegalArgumentException("Target file name $target is null or empty")
        }

        val file = File(target)

        if (!file.exists() || file.readLines(CHARSET).isEmpty()) {
            File(target).writeText(content.toString(), CHARSET)
        } else {
            File(target).appendText("\n" + content.toString(), CHARSET)
        }
    }
}
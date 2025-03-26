package edu.kpi.tetris.view

import edu.kpi.tetris.logic.board.Board
import edu.kpi.tetris.logic.pixel.PixelTypeEnum
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.FileNotFoundException
import kotlin.test.assertNotEquals

class FileManagerTest {
    private fun readFile(testFile: File): String {
        return testFile.readText()
    }

    private fun readFile(testFile: File, from: Int, to: Int): String {
        return testFile.readText()
            .split("\n")
            .subList(from, to)
            .joinToString("\n")
    }

    private fun writeFile(testFile: File, content: String) {
        testFile.writeText(content)
    }

    @Test
    fun testRead() {
        // Preconditions: creating empty file
        val file: File = File.createTempFile("test", ".temp")
        val path: String = file.absolutePath
        file.deleteOnExit()

        val manager = FileManager()

        // Test Case 1: testing reading one-line content
        this.writeFile(file, "1 1\n.")

        assertEquals(listOf("."), manager.read(path))

        val multiContent = listOf(".p.", ".p.", "pp.")

        // Test Case 2: testing reading multi-line board with Unix delimiter
        this.writeFile(file, "3 3\n" + multiContent.joinToString("\n"))
        assertEquals(multiContent, manager.read(path))

        // Test Case 3: testing reading multi-line board with Windows delimiter
        this.writeFile(file,"3 3\r\n" + multiContent.joinToString("\r\n"))
        assertEquals(multiContent, manager.read(path))

        // Test Case 4: testing reading not-quadratic board
        val notQuadraticContent = listOf("..p.", "..p.", "..p.", "..p.", "..p.", "..p.")

        this.writeFile(file, "6 4\n" + notQuadraticContent.joinToString("\n"))
        assertEquals(notQuadraticContent, manager.read(path))
    }

    @Test
    fun testWrite() {
        // Preconditions: creating empty file
        val file: File = File.createTempFile("test", ".temp")
        val path: String = file.absolutePath
        file.deleteOnExit()

        val manager = FileManager()
        val content = Board(3, 3)

        // Test Case 1: testing writing with invalid name
        assertThrows<IllegalArgumentException> {
            manager.write(null, content)
        }

        assertThrows<IllegalArgumentException> {
            manager.write("", content)
        }

        // Test Case 2: testing simple writing
        manager.write(path, content)
        assertEquals(content.toString(), this.readFile(file))

        // Test Case 3: testing overwriting content
        content.set(1, 1, PixelTypeEnum.LAND)
        assertNotEquals(content.toString(), this.readFile(file))

        manager.write(path, content)
        assertEquals(content.toString(), this.readFile(file))
    }

    @Test
    fun testAppend() {
        // Preconditions: creating empty file
        val file: File = File.createTempFile("test", ".temp")
        val path: String = file.absolutePath
        file.deleteOnExit()

        val manager = FileManager()
        val content = Board(3, 3)

        // Test Case 1: testing appending with invalid name
        assertThrows<IllegalArgumentException> {
            manager.append(null, content)
        }

        assertThrows<IllegalArgumentException> {
            manager.append("", content)
        }

        // Test Case 2: testing appending to the empty file
        manager.append(path, content)
        assertEquals(content.toString(), this.readFile(file))

        // Test Case 3: testing appending to file with content
        content.set(1, 1, PixelTypeEnum.LAND)
        manager.append(path, content)

        // reading file from fourth row as board height is 3
        assertEquals(content.toString(), this.readFile(file, 3, 6))

        // Test Case 4: testing writing and appending to file
        content.set(1, 2, PixelTypeEnum.LAND)
        val contentBeforeAppend = content.clone()

        manager.write(path, content)

        content.set(2, 2, PixelTypeEnum.LAND)
        manager.append(path, content)

        assertEquals(contentBeforeAppend.toString(), this.readFile(file, 0, 3))
        assertEquals(content.toString(), this.readFile(file, 3, 6))
    }

    @Test
    fun testInvalidNameRead() {
        // Preconditions: creating empty file
        val file: File = File.createTempFile("test", ".temp")
        file.deleteOnExit()

        val manager = FileManager()

        // Test Case 1: testing reading null or empty file name
        assertThrows<IllegalArgumentException> {
            manager.read(null)
        }

        assertThrows<IllegalArgumentException> {
            manager.read("")
        }

        // Test Case 2: testing reading empty file
        assertThrows<IllegalStateException> {
            manager.read(file.absolutePath)
        }
    }

    @Test
    fun testFileNotFoundRead() {
        val manager = FileManager()

        val testFile = File("test")
        testFile.deleteOnExit()

        val testDir = File("test-dir")
        testDir.deleteOnExit()

        // Test Case 1: testing reading non-existing file
        assertThrows<FileNotFoundException> {
            manager.read(testFile.absolutePath)
        }

        // Test Case 1: testing reading directory
        testDir.mkdir()

        assertThrows<FileNotFoundException> {
            manager.read(testDir.absolutePath)
        }
    }

    @Test
    fun testInvalidContentRead() {
        // Preconditions: creating test file
        val file: File = File.createTempFile("test", ".temp")
        file.deleteOnExit()

        val manager = FileManager()

        // Test Case 1: testing wrong params (expected: 2 int params as string)
        this.writeFile(file, "1")
        assertThrows<IllegalStateException> {
            manager.read(file.absolutePath)
        }

        this.writeFile(file, "1 2 3")
        assertThrows<IllegalStateException> {
            manager.read(file.absolutePath)
        }

        // Test Case 2: testing invalid board height
        this.writeFile(file, "-1 -1")
        assertThrows<IllegalStateException> {
            manager.read(file.absolutePath)
        }

        this.writeFile(file, "-1 1")
        assertThrows<IllegalStateException> {
            manager.read(file.absolutePath)
        }

        this.writeFile(file, "3 3\n...\n...")
        assertThrows<IllegalStateException> {
            manager.read(file.absolutePath)
        }

        // Test Case 3: testing invalid row length
        this.writeFile(file, "3 3\n...\n..\n...")
        assertThrows<IllegalStateException> {
            manager.read(file.absolutePath)
        }
    }
}
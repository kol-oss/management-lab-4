package edu.kpi.tetris.view

interface InputManager {
    companion object {
        val PARAMS_DELIMITER_REGEX: Regex = "[\\s,:-]+".toRegex()
    }

    fun read(source: String?): List<String>
}
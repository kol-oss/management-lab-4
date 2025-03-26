package edu.kpi.tetris.logic.pixel

enum class PixelTypeEnum(private val value: String) {
    EMPTY("."),
    PIECE("p"),
    LAND("#");

    override fun toString(): String {
        return this.value
    }

    companion object {
        fun fromString(symbol: String): PixelTypeEnum? {
            return entries.find { it.value == symbol }
        }
    }
}
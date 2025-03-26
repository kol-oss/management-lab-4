package edu.kpi.tetris.logic.pixel

data class Pixel(
    val x: Int,
    val y: Int,
    private var type: PixelTypeEnum
) {
    fun getType(): PixelTypeEnum {
        return type
    }

    fun setType(type: PixelTypeEnum) {
        this.type = type
    }
}
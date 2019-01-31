package games.ui

import games.game2048.newGame2048
import java.awt.Color

object Game2048Settings : GameSettings("2048++", Color(0xbbada0)) {
    private val emptyColor = Color(0xcdc1b4)
    private var colors: Map<Int, Color> = run {
        val colors = listOf(
                Color(0xeee4da),
                Color(0xede0c8),
                Color(0xf2b179),
                Color(0xf59563),
                Color(0xf67c5f),
                Color(0xf65e3b),
                Color(0xedcf72),
                Color(0xedcc61),
                Color(0xedc850),
                Color(0xedc53f),
                Color(0xedc22e))

        val values: List<Int> = (1..11).map { Math.pow(2.0, it.toDouble()).toInt() }
        values.zip(colors).toMap()
    }

    override fun getBackgroundColor(value: Int) = colors[value] ?: emptyColor
    override fun getForegroundColor(value: Int) = if (value < 16) Color(0x776e65) else Color(0xf9f6f2)
}


fun main(args: Array<String>) {
    playGame(newGame2048(), Game2048Settings)
}
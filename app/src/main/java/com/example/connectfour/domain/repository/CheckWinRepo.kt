package com.example.connectfour.domain.repository

import androidx.compose.ui.graphics.Color
import com.example.connectfour.data.models.Player

interface CheckWinRepo {

    fun checkRows(
        rows: Int,
        winList: String,
        list: MutableMap<Pair<Int, Int>, Color>
    ): Boolean

    fun checkColumns(
        columns: Int,
        winList: String,
        list: MutableMap<Pair<Int, Int>, Color>
    ): Boolean

    fun checkDiagonalLeftToRight(
        rows: Int,
        columns: Int,
        player: Player,
        list: MutableMap<Pair<Int, Int>, Color>
    ): Boolean

    fun checkDiagonalRightToLeft(
        rows: Int,
        columns: Int,
        player: Player,
        list: MutableMap<Pair<Int, Int>, Color>
    ): Boolean
}
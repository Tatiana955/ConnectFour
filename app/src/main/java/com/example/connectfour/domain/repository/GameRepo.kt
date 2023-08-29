package com.example.connectfour.domain.repository

import androidx.compose.ui.graphics.Color
import com.example.connectfour.data.models.Player

interface GameRepo {

    fun checkWin(
        rows: Int,
        columns: Int,
        list: MutableMap<Pair<Int, Int>, Color>,
        player: Player
    ): Boolean
}
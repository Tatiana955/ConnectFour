package com.example.connectfour.usecases

import androidx.compose.ui.graphics.Color
import com.example.connectfour.data.models.Player
import com.example.connectfour.domain.repository.GameRepo
import javax.inject.Inject

class CheckWinUseCase @Inject constructor(
    private val repo: GameRepo
) {
    operator fun invoke(
        rows: Int,
        columns: Int,
        list: MutableMap<Pair<Int, Int>, Color>,
        player: Player
    ): Boolean {
        return repo.checkWin(rows, columns, list, player)
    }
}
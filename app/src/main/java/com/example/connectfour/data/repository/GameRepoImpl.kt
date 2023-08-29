package com.example.connectfour.data.repository

import androidx.compose.ui.graphics.Color
import com.example.connectfour.data.models.Player
import com.example.connectfour.domain.repository.CheckWinRepo
import com.example.connectfour.domain.repository.GameRepo

class GameRepoImpl : GameRepo, CheckWinRepo {

    override fun checkWin(
        rows: Int,
        columns: Int,
        list: MutableMap<Pair<Int, Int>, Color>,
        player: Player
    ): Boolean {
        val winList: String = List(4) { player.color }.joinToString()
        if (checkRows(rows, winList, list)) return true
        if (checkColumns(columns, winList, list)) return true
        if (checkDiagonalLeftToRight(rows, columns, player, list)) return true
        if (checkDiagonalRightToLeft(rows, columns, player, list)) return true
        return false
    }

    override fun checkRows(
        rows: Int,
        winList: String,
        list: MutableMap<Pair<Int, Int>, Color>
    ): Boolean {
        for (row in rows downTo 1) {
            if (list.filter { it.key.first == row }.values.joinToString().contains(winList)) {
                return true
            }
        }
        return false
    }

    override fun checkColumns(
        columns: Int,
        winList: String,
        list: MutableMap<Pair<Int, Int>, Color>
    ): Boolean {
        for (col in 1..columns) {
            if (list.filter { it.key.second == col }.values.joinToString().contains(winList)) {
                return true
            }
        }
        return false
    }

    override fun checkDiagonalLeftToRight(
        rows: Int,
        columns: Int,
        player: Player,
        list: MutableMap<Pair<Int, Int>, Color>
    ): Boolean {
        for (row in 1..rows) {
            for (col in 1..columns - 3) {
                if (list[Pair(row, col)] == player.color) {
                    if (list[Pair(row - 1, col + 1)] == player.color &&
                        list[Pair(row - 2, col + 2)] == player.color &&
                        list[Pair(row - 3, col + 3)] == player.color
                    ) {
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun checkDiagonalRightToLeft(
        rows: Int,
        columns: Int,
        player: Player,
        list: MutableMap<Pair<Int, Int>, Color>
    ): Boolean {
        for (row in 1..rows) {
            for (col in 4..columns) {
                if (list[Pair(row, col)] == player.color) {
                    if (list[Pair(row - 1, col - 1)] == player.color &&
                        list[Pair(row - 2, col - 2)] == player.color &&
                        list[Pair(row - 3, col - 3)] == player.color
                    ) {
                        return true
                    }
                }
            }
        }
        return false
    }
}
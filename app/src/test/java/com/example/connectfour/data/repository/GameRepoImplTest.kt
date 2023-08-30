package com.example.connectfour.data.repository

import androidx.compose.ui.graphics.Color
import com.example.connectfour.data.models.Player
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GameRepoImplTest {

    private lateinit var repository: GameRepoImpl

    private val rows = 4
    private val columns = 5

    private val list: MutableMap<Pair<Int, Int>, Color> = mutableMapOf(
        Pair(1, 1) to Color.Gray,
        Pair(1, 2) to Color.Gray,
        Pair(1, 3) to Color.Gray,
        Pair(1, 4) to Color.Gray,
        Pair(1, 5) to Color.Gray,
        Pair(2, 1) to Color.Gray,
        Pair(2, 2) to Color.Gray,
        Pair(2, 3) to Color.Gray,
        Pair(2, 4) to Color.Gray,
        Pair(2, 5) to Color.Gray,
        Pair(3, 1) to Color.Gray,
        Pair(3, 2) to Color.Gray,
        Pair(3, 3) to Color.Gray,
        Pair(3, 4) to Color.Gray,
        Pair(3, 5) to Color.Gray,
        Pair(4, 1) to Color.Gray,
        Pair(4, 2) to Color.Gray,
        Pair(4, 3) to Color.Gray,
        Pair(4, 4) to Color.Gray,
        Pair(4, 5) to Color.Gray
    )

    @Before
    fun createRepository() {
        repository = GameRepoImpl()
    }

    @Test
    fun `check rows returns true`() {
        val winList: String = List(4) { Color.Red }.joinToString()
        list[Pair(4, 1)] = Color.Red
        list[Pair(4, 2)] = Color.Red
        list[Pair(4, 3)] = Color.Red
        list[Pair(4, 4)] = Color.Red
        val result = repository.checkRows(rows, winList, list)
        Assert.assertEquals(result, true)
    }

    @Test
    fun `check rows returns false`() {
        val winList: String = List(4) { Color.Red }.joinToString()
        list[Pair(4, 1)] = Color.Red
        list[Pair(4, 2)] = Color.Blue
        list[Pair(4, 3)] = Color.Red
        list[Pair(4, 4)] = Color.Red
        val result = repository.checkRows(rows, winList, list)
        Assert.assertEquals(result, false)
    }

    @Test
    fun `check columns returns true`() {
        val winList: String = List(4) { Color.Red }.joinToString()
        list[Pair(1, 1)] = Color.Red
        list[Pair(2, 1)] = Color.Red
        list[Pair(3, 1)] = Color.Red
        list[Pair(4, 1)] = Color.Red
        val result = repository.checkColumns(columns, winList, list)
        Assert.assertEquals(result, true)
    }

    @Test
    fun `check columns returns false`() {
        val winList: String = List(4) { Color.Red }.joinToString()
        list[Pair(1, 1)] = Color.Red
        list[Pair(2, 1)] = Color.Blue
        list[Pair(3, 1)] = Color.Red
        list[Pair(4, 1)] = Color.Red
        val result = repository.checkColumns(columns, winList, list)
        Assert.assertEquals(result, false)
    }

    @Test
    fun `check diagonal left to right returns true`() {
        val player = Player(color = Color.Red)
        list[Pair(4, 1)] = Color.Red
        list[Pair(3, 2)] = Color.Red
        list[Pair(2, 3)] = Color.Red
        list[Pair(1, 4)] = Color.Red
        val result = repository.checkDiagonalLeftToRight(rows, columns, player, list)
        Assert.assertEquals(result, true)
    }

    @Test
    fun `check diagonal left to right returns false`() {
        val player = Player(color = Color.Red)
        list[Pair(4, 1)] = Color.Red
        list[Pair(3, 2)] = Color.Blue
        list[Pair(2, 3)] = Color.Red
        list[Pair(1, 4)] = Color.Red
        val result = repository.checkDiagonalLeftToRight(rows, columns, player, list)
        Assert.assertEquals(result, false)
    }

    @Test
    fun `check diagonal right to left returns true`() {
        val player = Player(color = Color.Red)
        list[Pair(4, 5)] = Color.Red
        list[Pair(3, 4)] = Color.Red
        list[Pair(2, 3)] = Color.Red
        list[Pair(1, 2)] = Color.Red
        val result = repository.checkDiagonalRightToLeft(rows, columns, player, list)
        Assert.assertEquals(result, true)
    }

    @Test
    fun `check diagonal right to left returns false`() {
        val player = Player(color = Color.Red)
        list[Pair(4, 5)] = Color.Red
        list[Pair(3, 4)] = Color.Blue
        list[Pair(2, 3)] = Color.Red
        list[Pair(1, 2)] = Color.Red
        val result = repository.checkDiagonalRightToLeft(rows, columns, player, list)
        Assert.assertEquals(result, false)
    }

    @Test
    fun `check win returns true when rows returns true`() {
        val player = Player(color = Color.Red)
        list[Pair(4, 1)] = Color.Red
        list[Pair(4, 2)] = Color.Red
        list[Pair(4, 3)] = Color.Red
        list[Pair(4, 4)] = Color.Red
        val result = repository.checkWin(rows, columns, list, player)
        Assert.assertEquals(result, true)
    }

    @Test
    fun `check win returns true when columns returns true`() {
        val player = Player(color = Color.Red)
        list[Pair(1, 1)] = Color.Red
        list[Pair(2, 1)] = Color.Red
        list[Pair(3, 1)] = Color.Red
        list[Pair(4, 1)] = Color.Red
        val result = repository.checkWin(rows, columns, list, player)
        Assert.assertEquals(result, true)
    }

    @Test
    fun `check win returns true when diagonal left to right returns true`() {
        val player = Player(color = Color.Red)
        list[Pair(4, 1)] = Color.Red
        list[Pair(3, 2)] = Color.Red
        list[Pair(2, 3)] = Color.Red
        list[Pair(1, 4)] = Color.Red
        val result = repository.checkWin(rows, columns, list, player)
        Assert.assertEquals(result, true)
    }

    @Test
    fun `check win returns true when diagonal right to left returns true`() {
        val player = Player(color = Color.Red)
        list[Pair(4, 5)] = Color.Red
        list[Pair(3, 4)] = Color.Red
        list[Pair(2, 3)] = Color.Red
        list[Pair(1, 2)] = Color.Red
        val result = repository.checkWin(rows, columns, list, player)
        Assert.assertEquals(result, true)
    }

    @Test
    fun `check win returns false`() {
        val player = Player(color = Color.Red)
        list[Pair(4, 1)] = Color.Red
        list[Pair(4, 2)] = Color.Blue
        list[Pair(4, 3)] = Color.Red
        list[Pair(3, 2)] = Color.Blue
        val result = repository.checkWin(rows, columns, list, player)
        Assert.assertEquals(result, false)
    }
}
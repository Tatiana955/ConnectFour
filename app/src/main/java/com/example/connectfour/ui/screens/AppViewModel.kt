package com.example.connectfour.ui.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.connectfour.data.models.Player
import com.example.connectfour.domain.util.*
import com.example.connectfour.domain.util.CounterStatus
import com.example.connectfour.domain.util.GameMode
import com.example.connectfour.domain.util.VisibilityStatus

class AppViewModel : ViewModel() {

    private val _firstPlayer = mutableStateOf(Player(color = Color.Red))
    val firstPlayer: MutableState<Player> = _firstPlayer

    private val _secondPlayer = mutableStateOf(Player(color = Color.Blue))
    val secondPlayer: MutableState<Player> = _secondPlayer

    private val _currentPlayer = mutableStateOf(Player())
    val currentPlayer: MutableState<Player> = _currentPlayer

    private val _mapOfBoardSizes = mutableStateMapOf(
        Constants.BoardSize.BOARD_SIZE_MIN to false,
        Constants.BoardSize.BOARD_SIZE_SMALL to false,
        Constants.BoardSize.BOARD_SIZE_NORMAL to false,
        Constants.BoardSize.BOARD_SIZE_MEDIUM to false,
        Constants.BoardSize.BOARD_SIZE_LARGE to false,
        Constants.BoardSize.BOARD_SIZE_EXTRA_LARGE to false,
        Constants.BoardSize.BOARD_SIZE_MAX to false
    )
    val mapOfBoardSizes: SnapshotStateMap<String, Boolean> = _mapOfBoardSizes

    @SuppressLint("MutableCollectionMutableState")
    private val listOfSelectedBoardSizes = mutableStateOf(mutableSetOf<String>())

    private val _boardSize = mutableStateOf(Constants.BoardSize.BOARD_SIZE_NORMAL)
    val boardSize: MutableState<String> = _boardSize

    private val _columnCount = mutableIntStateOf(_boardSize.value.first().digitToInt())
    val columnCount: MutableState<Int> = _columnCount

    private val _rowCount = mutableIntStateOf(_boardSize.value.last().digitToInt())
    val rowCount: MutableState<Int> = _rowCount

    private val _gameModes = mutableStateMapOf(
        GameMode.SINGLE to false,
        GameMode.MULTI to false
    )
    val gameModes: SnapshotStateMap<GameMode, Boolean> = _gameModes

    private val _selectedGameMode = mutableStateOf(GameMode.SINGLE)
    val selectedGameMode: MutableState<GameMode> = _selectedGameMode

    private val _numberOfRounds = mutableIntStateOf(0)
    val numberOfRounds: MutableState<Int> = _numberOfRounds

    private val _fabVisibility = mutableStateOf(false)
    val fabVisibility: MutableState<Boolean> = _fabVisibility

    @SuppressLint("MutableCollectionMutableState")
    private val _listOfCoord = mutableStateOf(mutableMapOf<Pair<Int, Int>, Color>())
    val listOfCoord: MutableState<MutableMap<Pair<Int, Int>, Color>> = _listOfCoord

    fun save1st(name: String) {
        _firstPlayer.value = Player(
            name = name.ifEmpty { Constants.Name.FIRST_PLAYER },
            color = Color.Red
        )
    }

    fun save2nd(name: String) {
        _secondPlayer.value = Player(
            name = name.ifEmpty { Constants.Name.SECOND_PLAYER },
            color = Color.Blue
        )
    }

    fun choiceBoardSize(key: String, value: Boolean) {
        _mapOfBoardSizes[key] = value
    }

    fun getBoardSize(color: Color) {
        fillListOfSelectedBoardSizes()
        if (listOfSelectedBoardSizes.value.isNotEmpty()) {
            if (listOfSelectedBoardSizes.value.size > 1) {
                _boardSize.value = listOfSelectedBoardSizes.value.random()
            } else {
                _boardSize.value = listOfSelectedBoardSizes.value.first()
            }
        } else {
            _boardSize.value = Constants.BoardSize.BOARD_SIZE_NORMAL
        }
        _currentPlayer.value = _firstPlayer.value
        getColumnCount()
        getRowCount()
        fillOutListOfCoord(color)
    }

    private fun fillListOfSelectedBoardSizes() {
        for ((key, value) in _mapOfBoardSizes) {
            if (value) listOfSelectedBoardSizes.value.add(key)
            if (!value) listOfSelectedBoardSizes.value.remove(key)
        }
    }

    private fun getColumnCount() {
        _columnCount.intValue = if (_boardSize.value.length > 5) {
            "${_boardSize.value[0]}${_boardSize.value[1]}".toInt()
        } else {
            _boardSize.value.first().digitToInt()
        }
    }

    private fun getRowCount() {
        _rowCount.intValue = _boardSize.value.last().digitToInt()
    }

    private fun fillOutListOfCoord(color: Color) {
        _listOfCoord.value.clear()
        for (row in 1.._rowCount.intValue) {
            for (col in 1.._columnCount.intValue) {
                _listOfCoord.value[Pair(row, col)] = color
            }
        }
    }

    fun changeGameMode(gameMode: GameMode, checked: Boolean) {
        for ((key, value) in _gameModes) {
            if (key != gameMode) {
                _gameModes[key] = false
            } else if (value == checked) {
                _gameModes[gameMode] = !checked
            } else {
                _gameModes[gameMode] = checked
            }
        }
        getGameMode(gameMode)
    }

    private fun getGameMode(gameMode: GameMode) {
        _selectedGameMode.value = gameMode
    }

    fun saveNumberOfRounds(status: CounterStatus) {
        when (status) {
            CounterStatus.Increase -> _numberOfRounds.intValue += 1
            CounterStatus.Decrease -> _numberOfRounds.intValue =
                maxOf(_numberOfRounds.intValue - 1, 0)

            CounterStatus.Clear -> _numberOfRounds.intValue = 0
        }
    }

    fun fabVisibility(status: VisibilityStatus) {
        when (status) {
            VisibilityStatus.Visible -> _fabVisibility.value = true
            VisibilityStatus.Invisible -> _fabVisibility.value = false
        }
    }
}
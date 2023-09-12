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
import com.example.connectfour.usecases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _firstPlayer = mutableStateOf(Player(color = Color.Red))
    val firstPlayer: MutableState<Player> = _firstPlayer

    private val _secondPlayer = mutableStateOf(Player(color = Color.Blue))
    val secondPlayer: MutableState<Player> = _secondPlayer

    private val _currentPlayer = mutableStateOf(Player())
    val currentPlayer: MutableState<Player> = _currentPlayer

    @SuppressLint("MutableCollectionMutableState")
    private val _mapOfBoardSizes = mutableStateOf(
        mutableMapOf(
            Constants.BoardSize.BOARD_SIZE_MIN to false,
            Constants.BoardSize.BOARD_SIZE_SMALL to false,
            Constants.BoardSize.BOARD_SIZE_NORMAL to false,
            Constants.BoardSize.BOARD_SIZE_MEDIUM to false,
            Constants.BoardSize.BOARD_SIZE_LARGE to false,
            Constants.BoardSize.BOARD_SIZE_EXTRA_LARGE to false,
            Constants.BoardSize.BOARD_SIZE_MAX to false
        )
    )
    val mapOfBoardSizes: MutableState<MutableMap<String, Boolean>> = _mapOfBoardSizes

    @SuppressLint("MutableCollectionMutableState")
    private val listOfSelectedBoardSizes = mutableStateOf(mutableSetOf<String>())

    private val boardSize = mutableStateOf(Constants.BoardSize.BOARD_SIZE_NORMAL)

    private val _columnCount = mutableIntStateOf(boardSize.value.first().digitToInt())
    val columnCount: MutableState<Int> = _columnCount

    private val rowCount = mutableIntStateOf(boardSize.value.last().digitToInt())

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
    private val listOfCoord = mutableStateOf(mutableMapOf<Pair<Int, Int>, Color>())
    val listOfColors: MutableCollection<Color> = listOfCoord.value.values

    private val _statistic = mutableStateMapOf<String, Int>()
    val statistic: SnapshotStateMap<String, Int> = _statistic

    private val _showSnackbar = mutableStateOf(false)
    val showSnackbar: MutableState<Boolean> = _showSnackbar

    private val _showSheet = mutableStateOf(false)
    val showSheet: MutableState<Boolean> = _showSheet

    private val _showDialog = mutableStateOf(false)
    val showDialog: MutableState<Boolean> = _showDialog

    private val _message = mutableStateOf("")
    val message: MutableState<String> = _message

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
        _mapOfBoardSizes.value[key] = value
    }

    fun getBoardSize(color: Color) {
        initialValues()
        fillListOfSelectedBoardSizes()
        boardSize.value = if (listOfSelectedBoardSizes.value.isNotEmpty()) {
            if (listOfSelectedBoardSizes.value.size > 1) {
                listOfSelectedBoardSizes.value.random()
            } else {
                listOfSelectedBoardSizes.value.first()
            }
        } else {
            Constants.BoardSize.BOARD_SIZE_NORMAL
        }
        getColumnCount()
        getRowCount()
        fillOutListOfCoord(color)
    }

    private fun initialValues() {
        if (_showSheet.value) hideSheet()
        if (_showSnackbar.value) _showSnackbar.value = false
        if (_firstPlayer.value.score > 0) _firstPlayer.value.score = 0
        if (_secondPlayer.value.score > 0) _secondPlayer.value.score = 0
        _currentPlayer.value = _firstPlayer.value
        if (_selectedGameMode.value == GameMode.MULTI && _numberOfRounds.intValue == 0) {
            _numberOfRounds.intValue = 3
        }
    }

    fun hideSheet() {
        _showSheet.value = false
    }

    private fun fillListOfSelectedBoardSizes() {
        for ((key, value) in _mapOfBoardSizes.value) {
            if (value) listOfSelectedBoardSizes.value.add(key)
            if (!value) listOfSelectedBoardSizes.value.remove(key)
        }
    }

    private fun getColumnCount() {
        _columnCount.intValue = if (boardSize.value.length > 5) {
            "${boardSize.value[0]}${boardSize.value[1]}".toInt()
        } else {
            boardSize.value.first().digitToInt()
        }
    }

    private fun getRowCount() {
        rowCount.intValue = boardSize.value.last().digitToInt()
    }

    private fun fillOutListOfCoord(color: Color) {
        listOfCoord.value.clear()
        for (row in 1..rowCount.intValue) {
            for (col in 1.._columnCount.intValue) {
                listOfCoord.value[Pair(row, col)] = color
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
        _numberOfRounds.intValue = when (status) {
            CounterStatus.Increase -> _numberOfRounds.intValue + 1
            CounterStatus.Decrease -> maxOf(_numberOfRounds.intValue - 1, 0)
            CounterStatus.Clear -> 0
        }
    }

    fun fabVisibility(status: VisibilityStatus) {
        _fabVisibility.value = when (status) {
            VisibilityStatus.Visible -> true
            VisibilityStatus.Invisible -> false
        }
    }

    fun showDialog(isShow: Boolean) {
        _showDialog.value = isShow
    }

    fun clearBoard(startColor: Color) {
        for (k in listOfCoord.value.filter { it.value != startColor }.keys) {
            listOfCoord.value.replace(k, startColor)
        }
    }

    fun turnOfPlayer(column: Int, startColor: Color) {
        if (_showSnackbar.value) _showSnackbar.value = false
        when (turn(column, startColor)) {
            PlayerTurnStatus.FullColumn -> {
                _showSnackbar.value = true
                _message.value = "${PlayerTurnStatus.FullColumn}"
            }

            PlayerTurnStatus.FullField -> score(PlayerTurnStatus.FullField)
            PlayerTurnStatus.Success -> changeCurrentPlayer()
            PlayerTurnStatus.WinningRound -> score(PlayerTurnStatus.WinningRound)
            PlayerTurnStatus.ItIsDraw -> score(PlayerTurnStatus.ItIsDraw)
            PlayerTurnStatus.WinningGame -> {
                score(PlayerTurnStatus.WinningGame)
                playerWins()
            }
        }
    }

    private fun turn(column: Int, startColor: Color): PlayerTurnStatus {
        val filtered = listOfCoord.value.filter {
            it.key.second == column && it.value == startColor
        }

        val row = getNumberOfRow(filtered)

        if (row == 0) {
            return PlayerTurnStatus.FullColumn
        }

        for (k in filtered.keys) {
            if (listOfCoord.value[k] == startColor) {
                changeColor(row, k.second, _currentPlayer.value.color)
                break
            }
        }

        if (!isWin()) {
            if (listOfColors.none { it == startColor }) {
                _numberOfRounds.intValue -= 1
                if (_numberOfRounds.intValue == 0 && isItDraw()) {
                    return PlayerTurnStatus.ItIsDraw
                }
                return PlayerTurnStatus.FullField
            }
            return PlayerTurnStatus.Success
        }

        return when (_selectedGameMode.value) {
            GameMode.SINGLE -> PlayerTurnStatus.WinningGame
            else -> {
                _numberOfRounds.intValue -= 1
                _currentPlayer.value.score += 1

                if (_numberOfRounds.intValue > 0) {
                    PlayerTurnStatus.WinningRound
                } else {
                    if (isItDraw()) {
                        PlayerTurnStatus.ItIsDraw
                    } else {
                        PlayerTurnStatus.WinningGame
                    }
                }
            }
        }
    }

    private fun getNumberOfRow(filtered: Map<Pair<Int, Int>, Color>): Int {
        return if (filtered.isEmpty()) 0 else filtered.maxOf { it.key.first }
    }

    private fun changeColor(row: Int, col: Int, newColor: Color) {
        listOfCoord.value[Pair(row, col)] = newColor
    }

    private fun isWin(): Boolean {
        return useCases.checkWinUseCase.invoke(
            rows = rowCount.intValue,
            columns = _columnCount.intValue,
            list = listOfCoord.value,
            player = _currentPlayer.value
        )
    }

    private fun isItDraw(): Boolean {
        return _firstPlayer.value.score == _secondPlayer.value.score
    }

    private fun score(status: PlayerTurnStatus) {
        _showSheet.value = true
        _statistic[_firstPlayer.value.name] = _firstPlayer.value.score
        _statistic[_secondPlayer.value.name] = _secondPlayer.value.score
        _message.value = status.getState(_currentPlayer.value.name)
        changeCurrentPlayer()
    }

    private fun changeCurrentPlayer() {
        _currentPlayer.value = if (_currentPlayer.value == _firstPlayer.value) {
            _secondPlayer.value
        } else {
            _firstPlayer.value
        }
    }

    private fun playerWins() {
        _currentPlayer.value = if (_firstPlayer.value.score > _secondPlayer.value.score) {
            _firstPlayer.value
        } else {
            _secondPlayer.value
        }
    }
}
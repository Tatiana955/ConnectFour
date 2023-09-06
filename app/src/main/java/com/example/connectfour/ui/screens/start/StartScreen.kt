package com.example.connectfour.ui.screens.start

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.connectfour.R
import com.example.connectfour.domain.util.CounterStatus
import com.example.connectfour.domain.util.GameMode
import com.example.connectfour.domain.util.VisibilityStatus
import com.example.connectfour.ui.screens.AppViewModel
import com.example.connectfour.ui.screens.components.CounterButton
import com.example.connectfour.ui.screens.components.Section

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: AppViewModel,
    startGame: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(paddingValues)
    ) {
        Content(
            modifier = modifier,
            viewModel = viewModel,
            startGame = startGame
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    viewModel: AppViewModel,
    startGame: () -> Unit
) {
    val fabVisibility = remember { viewModel.fabVisibility }

    val firstPlayer = remember { viewModel.firstPlayer }
    val secondPlayer = remember { viewModel.secondPlayer }

    val mapOfBoardSizes = remember { viewModel.mapOfBoardSizes }.value
    val boardSizesList = remember { mapOfBoardSizes.keys }.toList()

    Box(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .wrapContentHeight()
        ) {
            Section(
                modifier = modifier,
                text = stringResource(R.string.names_of_players)
            )

            TextFieldName(
                modifier = modifier,
                label = stringResource(R.string.name_of_first),
                name = firstPlayer.value.name,
                color = firstPlayer.value.color,
                saveName = { viewModel.save1st(it) }
            )

            TextFieldName(
                modifier = modifier,
                label = stringResource(R.string.name_of_second),
                name = secondPlayer.value.name,
                color = secondPlayer.value.color,
                saveName = { viewModel.save2nd(it) }
            )

            Spacer(modifier = modifier.height(10.dp))

            Section(
                modifier = modifier,
                text = stringResource(R.string.board_size)
            )

            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                BoxOfBoardSizes(
                    modifier = modifier,
                    mapOfBoardSizes = mapOfBoardSizes,
                    boardSizeList = boardSizesList.subList(0, 3),
                    choiceBoardSize = { key, value ->
                        viewModel.choiceBoardSize(key, value)
                    }
                )

                BoxOfBoardSizes(
                    modifier = modifier,
                    mapOfBoardSizes = mapOfBoardSizes,
                    boardSizeList = boardSizesList.subList(3, boardSizesList.size - 1),
                    choiceBoardSize = { key, value ->
                        viewModel.choiceBoardSize(key, value)
                    }
                )
            }

            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                BoardSizeItem(
                    modifier = modifier,
                    boardSize = boardSizesList.last(),
                    isSelected = mapOfBoardSizes[boardSizesList.last()]!!,
                    choiceBoardSize = { viewModel.choiceBoardSize(boardSizesList.last(), it) }
                )
            }

            Spacer(modifier = modifier.height(20.dp))

            Section(
                modifier = modifier,
                text = stringResource(R.string.game_mode)
            )

            GameMode(
                modifier = modifier,
                viewModel = viewModel
            )
        }

        StartButton(
            modifier = modifier
                .padding(all = 10.dp)
                .align(Alignment.BottomEnd),
            visibility = fabVisibility.value,
            getBoardSize = { viewModel.getBoardSize(it) },
            startGame = startGame
        )
    }
}

@Composable
private fun TextFieldName(
    modifier: Modifier,
    name: String,
    label: String,
    color: Color,
    saveName: (String) -> Unit
) {
    var value by remember { mutableStateOf(name) }

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = { newText ->
            value = newText
            saveName(value)
        },
        modifier = modifier
            .fillMaxWidth(),
        label = { Text(text = label) },
        placeholder = { Text(text = stringResource(R.string.enter_name)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = stringResource(R.string.icon_account_circle),
                tint = color
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    value = ""
                    saveName(value)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.icon_clear)
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Go
        ),
        keyboardActions = KeyboardActions(
            onGo = {
                saveName(value)
                focusManager.clearFocus()
            }
        )
    )

    Spacer(modifier = modifier.height(10.dp))
}

@Composable
private fun BoxOfBoardSizes(
    modifier: Modifier,
    mapOfBoardSizes: MutableMap<String, Boolean>,
    boardSizeList: List<String>,
    choiceBoardSize: (String, Boolean) -> Unit,
) {
    val fbs = boardSizeList[0]
    val sbs = boardSizeList[1]
    val tbs = boardSizeList[2]

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoardSizeItem(
            modifier = modifier,
            boardSize = fbs,
            isSelected = mapOfBoardSizes[fbs]!!,
            choiceBoardSize = { choiceBoardSize(fbs, it) }
        )

        BoardSizeItem(
            modifier = modifier,
            boardSize = sbs,
            isSelected = mapOfBoardSizes[sbs]!!,
            choiceBoardSize = { choiceBoardSize(sbs, it) }
        )

        BoardSizeItem(
            modifier = modifier,
            boardSize = tbs,
            isSelected = mapOfBoardSizes[tbs]!!,
            choiceBoardSize = { choiceBoardSize(tbs, it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoardSizeItem(
    modifier: Modifier,
    boardSize: String,
    isSelected: Boolean,
    choiceBoardSize: (Boolean) -> Unit
) {
    var selected by remember { mutableStateOf(isSelected) }

    ElevatedFilterChip(
        modifier = modifier
            .padding(6.dp),
        selected = selected,
        onClick = {
            selected = !selected
            choiceBoardSize(selected)
        },
        label = { Text(text = boardSize) },
        leadingIcon = {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(R.string.icon_done),
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
private fun GameMode(
    modifier: Modifier,
    viewModel: AppViewModel
) {
    val mode = remember { viewModel.gameModes }

    val single = remember { mutableStateOf(mode[GameMode.SINGLE]!!) }
    val multiple = remember { mutableStateOf(mode[GameMode.MULTI]!!) }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        LabelledCheckBox(
            modifier = modifier,
            checked = single.value,
            onCheckedChange = {
                single.value = it
                multiple.value = false
            },
            fabVisibility = { viewModel.fabVisibility(it) },
            changeGameMode = { viewModel.changeGameMode(GameMode.SINGLE, it) },
            gameMode = GameMode.SINGLE.mode
        )

        LabelledCheckBox(
            modifier = modifier,
            checked = multiple.value,
            onCheckedChange = {
                multiple.value = it
                single.value = false
            },
            fabVisibility = { viewModel.fabVisibility(it) },
            changeGameMode = { viewModel.changeGameMode(GameMode.MULTI, it) },
            gameMode = GameMode.MULTI.mode
        )
    }

    Spacer(modifier = modifier.height(10.dp))

    if (multiple.value) {
        NumberOfRounds(
            modifier = modifier,
            numberOfRounds = viewModel.numberOfRounds,
            saveNumberOfRounds = { viewModel.saveNumberOfRounds(it) }
        )
    }
}

@Composable
private fun LabelledCheckBox(
    modifier: Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    fabVisibility: (VisibilityStatus) -> Unit,
    changeGameMode: (Boolean) -> Unit,
    gameMode: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(
                indication = rememberRipple(
                    color = MaterialTheme.colorScheme.primary
                ),
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onCheckedChange(!checked)
                    when (checked) {
                        true -> fabVisibility(VisibilityStatus.Invisible)
                        false -> fabVisibility(VisibilityStatus.Visible)
                    }
                    changeGameMode(!checked)
                }
            )
            .requiredHeight(ButtonDefaults.MinHeight)
            .padding(4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null
        )

        Spacer(Modifier.size(6.dp))

        Text(text = gameMode)
    }
}

@Composable
private fun NumberOfRounds(
    modifier: Modifier,
    numberOfRounds: MutableState<Int>,
    saveNumberOfRounds: (CounterStatus) -> Unit
) {
    val valueCounter by remember { numberOfRounds }

    Column(
        modifier = Modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Section(
            modifier = modifier,
            text = stringResource(R.string.number_of_rounds)
        )

        CounterButton(
            modifier = modifier,
            value = valueCounter.toString(),
            onValueIncreaseClick = {
                saveNumberOfRounds(CounterStatus.Increase)
            },
            onValueDecreaseClick = {
                saveNumberOfRounds(CounterStatus.Decrease)
            },
            onValueClearClick = {
                saveNumberOfRounds(CounterStatus.Clear)
            }
        )
    }
}

@Composable
private fun StartButton(
    modifier: Modifier,
    visibility: Boolean,
    getBoardSize: (Color) -> Unit,
    startGame: () -> Unit
) {
    val startColor = MaterialTheme.colorScheme.outline

    AnimatedVisibility(
        visible = visibility,
        modifier = modifier
    ) {
        FloatingActionButton(
            onClick = {
                getBoardSize(startColor)
                startGame()
            },
            modifier = modifier,
            content = {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = stringResource(R.string.icon_play_arrow),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        )
    }
}
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
import com.example.connectfour.domain.util.GameMode
import com.example.connectfour.domain.util.VisibilityStatus
import com.example.connectfour.ui.screens.AppViewModel
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
    val mapOfBoardSizes = remember { viewModel.mapOfBoardSizes }
    val boardSizesList = remember { mapOfBoardSizes }.keys.toList()

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
                    viewModel = viewModel,
                    mapOfBoardSizes = mapOfBoardSizes,
                    boardSizeList = boardSizesList.subList(0, 3)
                )

                BoxOfBoardSizes(
                    modifier = modifier,
                    viewModel = viewModel,
                    mapOfBoardSizes = mapOfBoardSizes,
                    boardSizeList = boardSizesList.subList(3, boardSizesList.size - 1)
                )
            }

            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                BoardSizeItem(
                    modifier = modifier,
                    viewModel = viewModel,
                    boardSize = boardSizesList.last(),
                    isSelected = mapOfBoardSizes[boardSizesList.last()]!!
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
            viewModel = viewModel,
            visibility = fabVisibility.value,
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
    viewModel: AppViewModel,
    mapOfBoardSizes: MutableMap<String, Boolean>,
    boardSizeList: List<String>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoardSizeItem(
            modifier = modifier,
            viewModel = viewModel,
            boardSize = boardSizeList[0],
            isSelected = mapOfBoardSizes[boardSizeList[0]]!!
        )

        BoardSizeItem(
            modifier = modifier,
            viewModel = viewModel,
            boardSize = boardSizeList[1],
            isSelected = mapOfBoardSizes[boardSizeList[1]]!!
        )

        BoardSizeItem(
            modifier = modifier,
            viewModel = viewModel,
            boardSize = boardSizeList[2],
            isSelected = mapOfBoardSizes[boardSizeList[2]]!!
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoardSizeItem(
    modifier: Modifier,
    viewModel: AppViewModel,
    boardSize: String,
    isSelected: Boolean
) {
    var selected by remember { mutableStateOf(isSelected) }

    ElevatedFilterChip(
        modifier = modifier
            .padding(6.dp),
        selected = selected,
        onClick = {
            selected = !selected
            viewModel.choiceBoardSize(boardSize, selected)
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
            viewModel = viewModel,
            checked = single.value,
            onCheckedChange = {
                single.value = it
                multiple.value = false
            },
            gameMode = GameMode.SINGLE
        )

        LabelledCheckBox(
            modifier = modifier,
            viewModel = viewModel,
            checked = multiple.value,
            onCheckedChange = {
                multiple.value = it
                single.value = false
            },
            gameMode = GameMode.MULTI
        )
    }

    Spacer(modifier = modifier.height(10.dp))

    if (multiple.value) {
        NumberOfRounds(
            modifier = modifier,
            viewModel = viewModel
        )
    }
}

@Composable
private fun LabelledCheckBox(
    modifier: Modifier,
    viewModel: AppViewModel,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    gameMode: GameMode
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
                        true -> viewModel.fabVisibility(VisibilityStatus.Invisible)
                        false -> viewModel.fabVisibility(VisibilityStatus.Visible)
                    }
                    viewModel.changeGameMode(gameMode, !checked)
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

        Text(text = gameMode.mode)
    }
}

@Composable
private fun NumberOfRounds(
    modifier: Modifier,
    viewModel: AppViewModel
) {
    Column(
        modifier = Modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Section(
            modifier = modifier,
            text = stringResource(R.string.number_of_rounds)
        )

        // Counter Button
    }
}

@Composable
private fun StartButton(
    modifier: Modifier,
    viewModel: AppViewModel,
    visibility: Boolean,
    startGame: () -> Unit
) {
    val startColor = MaterialTheme.colorScheme.outline

    AnimatedVisibility(
        visible = visibility,
        modifier = modifier
    ) {
        FloatingActionButton(
            onClick = {
                viewModel.getBoardSize(startColor)
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
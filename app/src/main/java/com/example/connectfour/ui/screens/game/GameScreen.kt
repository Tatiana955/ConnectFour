package com.example.connectfour.ui.screens.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.connectfour.ui.screens.AppViewModel
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Snackbar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import com.example.connectfour.R
import com.example.connectfour.data.models.Player
import com.example.connectfour.domain.util.GameMode
import com.example.connectfour.ui.screens.components.Section

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: AppViewModel,
    returnToStartScreen: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(paddingValues)
    ) {
        Content(
            modifier = modifier,
            viewModel = viewModel,
            returnToStartScreen = returnToStartScreen
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    viewModel: AppViewModel,
    returnToStartScreen: () -> Unit,
) {
    val startColor = MaterialTheme.colorScheme.outline

    val currentPlayer = viewModel.currentPlayer

    val columnsCount = viewModel.columnCount.value
    val radius = if (columnsCount in 4..6) 40f else 30f
    val fontSize = if (radius == 40f) 26.sp else 20.sp

    val selectedGameMode = viewModel.selectedGameMode.value
    val numberOfRounds = viewModel.numberOfRounds.value

    Box(
        modifier = Modifier.padding(10.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Section(
                modifier = modifier,
                text = stringResource(R.string.selected_game_mode, "$selectedGameMode")
            )

            if (selectedGameMode == GameMode.MULTI) {
                Section(
                    modifier = modifier,
                    text = stringResource(
                        R.string.number_of_rounds_remaining,
                        "$numberOfRounds"
                    ),
                    background = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            Spacer(modifier = modifier.height(10.dp))

            Image(
                painter = painterResource(R.drawable.connect_four),
                contentDescription = stringResource(R.string.image_connect_four),
                modifier = modifier
                    .size(100.dp)
                    .clickable { viewModel.showDialog(true) }
            )

            if (viewModel.showDialog.value) {
                AlertDialogWithRules(
                    modifier = modifier,
                    onDismissRequest = { viewModel.showDialog(it) }
                )
            }

            Spacer(modifier = modifier.height(20.dp))

            Section(
                modifier = modifier,
                text = stringResource(R.string.turn_of_player, currentPlayer.value.name),
                background = MaterialTheme.colorScheme.secondaryContainer
            )

            Spacer(modifier = modifier.height(20.dp))

            TopCycles(
                modifier = modifier,
                columnsCount = columnsCount,
                currentPlayer = currentPlayer,
                radius = radius,
            )

            Spacer(modifier = modifier.height(40.dp))

            Field(
                modifier = modifier,
                columnsCount = columnsCount,
                listOfColors = viewModel.listOfColors,
                fontSize = fontSize,
                radius = radius
            )

            Spacer(modifier = modifier.height(30.dp))

            ControlButtons(
                modifier = modifier,
                columnsCount = columnsCount,
                fontSize = fontSize,
                turnOfPlayer = { viewModel.turnOfPlayer(it, startColor) }
            )
        }

        if (viewModel.showSnackbar.value) {
            ShowSnackbar(
                modifier = modifier.align(Alignment.BottomCenter),
                message = viewModel.message.value
            )
        }

        if (viewModel.showSheet.value) {
            ShowSheet(
                modifier = modifier,
                onDismiss = {
                    if (selectedGameMode == GameMode.SINGLE || numberOfRounds == 0) {
                        returnToStartScreen()
                    } else {
                        viewModel.clearBoard(startColor)
                        viewModel.hideSheet()
                    }
                },
                message = viewModel.message.value,
                selectedGameMode = selectedGameMode,
                statistic = viewModel.statistic
            )
        }
    }
}

@Composable
private fun AlertDialogWithRules(
    modifier: Modifier,
    onDismissRequest: (Boolean) -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismissRequest(false) },
        icon = {
            Image(
                painter = painterResource(R.drawable.connect_four),
                contentDescription = stringResource(R.string.image_connect_four),
                modifier = modifier.size(40.dp)
            )
        },
        title = {
            Text(text = stringResource(R.string.objective))
        },
        text = {
            Text(text = stringResource(R.string.rules))
        },
        confirmButton = {},
        dismissButton = {
            IconButton(
                onClick = { onDismissRequest(false) },
                modifier = Modifier.padding(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.icon_close)
                )
            }
        }
    )
}

@Composable
private fun TopCycles(
    modifier: Modifier,
    columnsCount: Int,
    currentPlayer: MutableState<Player>,
    radius: Float,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        repeat(columnsCount) {
            Canvas(
                modifier = Modifier.wrapContentSize()
            ) {
                drawCircle(
                    color = currentPlayer.value.color,
                    radius = radius
                )
            }
        }
    }
}

@Composable
private fun Field(
    modifier: Modifier,
    columnsCount: Int,
    listOfColors: MutableCollection<Color>,
    fontSize: TextUnit,
    radius: Float
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnsCount),
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center
    ) {
        this.items(items = listOfColors.toList(), itemContent = {
            FieldItem(
                modifier = modifier,
                item = it,
                fontSize = fontSize,
                radius = radius
            )
        })
    }
}

@Composable
private fun FieldItem(
    modifier: Modifier,
    item: Color,
    fontSize: TextUnit,
    radius: Float
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(2.dp)
            .clip(RoundedCornerShape(2.dp))
    ) {
        Text(text = "", fontSize = fontSize)

        Canvas(
            modifier = modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            drawCircle(
                color = item,
                radius = radius
            )
        }
    }
}

@Composable
private fun ControlButtons(
    modifier: Modifier,
    columnsCount: Int,
    fontSize: TextUnit,
    turnOfPlayer: (Int) -> Unit
) {
    val buttonSize = if (fontSize == 26.sp) 40.dp else 30.dp

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        repeat(columnsCount) { column ->
            Button(
                onClick = { turnOfPlayer(column + 1) },
                shape = RoundedCornerShape(10.dp),
                modifier = modifier
                    .size(buttonSize),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "${column + 1}",
                    fontSize = fontSize,
                )
            }
        }
    }
}

@Composable
private fun ShowSnackbar(
    modifier: Modifier,
    message: String,
) {
    Snackbar(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Text(text = message)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowSheet(
    modifier: Modifier,
    onDismiss: () -> Unit,
    message: String,
    selectedGameMode: GameMode,
    statistic: SnapshotStateMap<String, Int>,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        windowInsets = WindowInsets.displayCutout
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = bottomPadding, start = 10.dp, end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row { Text(text = message) }

            if (statistic.isNotEmpty() && selectedGameMode == GameMode.MULTI) {
                Row { Text(text = stringResource(R.string.score)) }

                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = statistic.keys.first())
                        Text(text = statistic.keys.last())
                    }
                    Column {
                        Text(text = "${statistic.values.first()}")
                        Text(text = "${statistic.values.last()}")
                    }
                }
            }
        }
    }
}
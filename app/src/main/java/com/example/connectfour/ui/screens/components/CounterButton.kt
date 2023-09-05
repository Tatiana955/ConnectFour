package com.example.connectfour.ui.screens.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.connectfour.R
import com.example.connectfour.domain.util.Constants.CounterButton.CONTAINER_OFFSET_FACTOR
import com.example.connectfour.domain.util.Constants.CounterButton.COUNTER_DELAY_FAST_MS
import com.example.connectfour.domain.util.Constants.CounterButton.COUNTER_DELAY_INITIAL_MS
import com.example.connectfour.domain.util.Constants.CounterButton.DRAG_CLEAR_ICON_REVEAL_DP
import com.example.connectfour.domain.util.Constants.CounterButton.DRAG_LIMIT_HORIZONTAL_DP
import com.example.connectfour.domain.util.Constants.CounterButton.DRAG_LIMIT_HORIZONTAL_THRESHOLD_FACTOR
import com.example.connectfour.domain.util.Constants.CounterButton.DRAG_LIMIT_VERTICAL_DP
import com.example.connectfour.domain.util.Constants.CounterButton.DRAG_LIMIT_VERTICAL_THRESHOLD_FACTOR
import com.example.connectfour.domain.util.Constants.CounterButton.START_DRAG_THRESHOLD_DP
import kotlinx.coroutines.*
import kotlin.math.absoluteValue
import kotlin.math.sign

@Composable
fun CounterButton(
    modifier: Modifier,
    value: String,
    onValueDecreaseClick: () -> Unit,
    onValueIncreaseClick: () -> Unit,
    onValueClearClick: () -> Unit
) {
    val thumbOffsetX = remember { Animatable(0f) }
    val thumbOffsetY = remember { Animatable(0f) }

    val verticalDragButtonRevealPx = DRAG_CLEAR_ICON_REVEAL_DP.dp.dpToPx()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(200.dp)
            .height(80.dp)
    ) {
        ButtonContainer(
            modifier = modifier,
            thumbOffsetX = thumbOffsetX.value,
            thumbOffsetY = thumbOffsetY.value,
            onValueDecreaseClick = onValueDecreaseClick,
            onValueIncreaseClick = onValueIncreaseClick,
            onValueClearClick = onValueClearClick,
            clearButtonVisible = thumbOffsetY.value >= verticalDragButtonRevealPx,
        )

        DraggableThumbButton(
            modifier = modifier
                .align(Alignment.Center),
            value = value,
            thumbOffsetX = thumbOffsetX,
            thumbOffsetY = thumbOffsetY,
            onClick = onValueIncreaseClick,
            onValueDecreaseClick = onValueDecreaseClick,
            onValueIncreaseClick = onValueIncreaseClick,
            onValueReset = onValueClearClick
        )
    }
}

@Composable
private fun ButtonContainer(
    modifier: Modifier,
    thumbOffsetX: Float,
    thumbOffsetY: Float,
    onValueDecreaseClick: () -> Unit,
    onValueIncreaseClick: () -> Unit,
    onValueClearClick: () -> Unit,
    clearButtonVisible: Boolean = false,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .offset {
                IntOffset(
                    (thumbOffsetX * CONTAINER_OFFSET_FACTOR).toInt(),
                    (thumbOffsetY * CONTAINER_OFFSET_FACTOR).toInt(),
                )
            }
            .fillMaxSize()
            .clip(RoundedCornerShape(64.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 8.dp)
    ) {
        IconControlButton(
            modifier = modifier,
            icon = Icons.Outlined.Remove,
            contentDescription = stringResource(R.string.decrease_count),
            onClick = onValueDecreaseClick,
            enabled = !clearButtonVisible,
        )

        if (clearButtonVisible) {
            IconControlButton(
                modifier = modifier,
                icon = Icons.Outlined.Clear,
                contentDescription = stringResource(R.string.clear_count),
                onClick = onValueClearClick,
                enabled = false,
            )
        }

        IconControlButton(
            modifier = modifier,
            icon = Icons.Outlined.Add,
            contentDescription = stringResource(R.string.increase_count),
            onClick = onValueIncreaseClick,
            enabled = !clearButtonVisible,
        )
    }
}

@Composable
private fun IconControlButton(
    modifier: Modifier,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    tintColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    clickTintColor: Color = MaterialTheme.colorScheme.outline,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    IconButton(
        modifier = modifier
            .size(48.dp),
        onClick = onClick,
        interactionSource = interactionSource,
        enabled = enabled
    ) {
        Icon(
            modifier = modifier
                .size(28.dp),
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isPressed) clickTintColor else tintColor,
        )
    }
}

@Composable
private fun DraggableThumbButton(
    modifier: Modifier,
    value: String,
    thumbOffsetX: Animatable<Float, AnimationVector1D>,
    thumbOffsetY: Animatable<Float, AnimationVector1D>,
    onClick: () -> Unit,
    onValueDecreaseClick: () -> Unit,
    onValueIncreaseClick: () -> Unit,
    onValueReset: () -> Unit
) {
    val dragLimitHorizontalPx = DRAG_LIMIT_HORIZONTAL_DP.dp.dpToPx()
    val dragLimitVerticalPx = DRAG_LIMIT_VERTICAL_DP.dp.dpToPx()
    val startDragThreshold = START_DRAG_THRESHOLD_DP.dp.dpToPx()

    val scope = rememberCoroutineScope()

    val dragDirection = remember { mutableStateOf(DragDirection.NONE) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .offset {
                IntOffset(
                    thumbOffsetX.value.toInt(),
                    thumbOffsetY.value.toInt(),
                )
            }
            .shadow(8.dp, CircleShape)
            .size(50.dp)
            .clip(CircleShape)
            .clickable {
                if (thumbOffsetX.value.absoluteValue <= startDragThreshold &&
                    thumbOffsetY.value.absoluteValue <= startDragThreshold
                ) {
                    onClick()
                }
            }
            .background(MaterialTheme.colorScheme.primary)
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        awaitFirstDown()

                        dragDirection.value = DragDirection.NONE

                        var counterJob: Job? = null

                        do {
                            val event = awaitPointerEvent()
                            event.changes.forEach { pointerInputChange ->
                                scope.launch {
                                    if ((dragDirection.value == DragDirection.NONE &&
                                                pointerInputChange.positionChange().x.absoluteValue >= startDragThreshold) ||
                                        dragDirection.value == DragDirection.HORIZONTAL
                                    ) {
                                        if (dragDirection.value == DragDirection.NONE) {
                                            counterJob = scope.launch {
                                                delay(COUNTER_DELAY_INITIAL_MS)

                                                var elapsed = COUNTER_DELAY_INITIAL_MS

                                                while (isActive && thumbOffsetX.value.absoluteValue >= (dragLimitHorizontalPx * DRAG_LIMIT_HORIZONTAL_THRESHOLD_FACTOR)) {
                                                    if (thumbOffsetX.value.sign > 0) {
                                                        onValueIncreaseClick()
                                                    } else {
                                                        onValueDecreaseClick()
                                                    }
                                                    delay(COUNTER_DELAY_FAST_MS)
                                                    elapsed += COUNTER_DELAY_FAST_MS
                                                }
                                            }
                                        }

                                        dragDirection.value = DragDirection.HORIZONTAL

                                        val dragFactor =
                                            1 - (thumbOffsetX.value / dragLimitHorizontalPx).absoluteValue

                                        val delta =
                                            pointerInputChange.positionChange().x * dragFactor

                                        val targetValue = thumbOffsetX.value + delta

                                        val targetValueWithinBounds =
                                            targetValue.coerceIn(
                                                -dragLimitHorizontalPx,
                                                dragLimitHorizontalPx
                                            )

                                        thumbOffsetX.snapTo(targetValueWithinBounds)
                                    } else if (dragDirection.value != DragDirection.HORIZONTAL &&
                                        pointerInputChange.positionChange().y >= startDragThreshold
                                    ) {
                                        dragDirection.value = DragDirection.VERTICAL

                                        val dragFactor =
                                            1 - (thumbOffsetY.value / dragLimitVerticalPx).absoluteValue

                                        val delta =
                                            pointerInputChange.positionChange().y * dragFactor

                                        val targetValue = thumbOffsetY.value + delta

                                        val targetValueWithinBounds =
                                            targetValue.coerceIn(
                                                -dragLimitVerticalPx,
                                                dragLimitVerticalPx
                                            )

                                        thumbOffsetY.snapTo(targetValueWithinBounds)
                                    }
                                }
                            }
                        } while (event.changes.any { it.pressed })
                        counterJob?.cancel()
                    }

                    if (thumbOffsetX.value.absoluteValue >= (dragLimitHorizontalPx * DRAG_LIMIT_HORIZONTAL_THRESHOLD_FACTOR)) {
                        if (thumbOffsetX.value.sign > 0) {
                            onValueIncreaseClick()
                        } else {
                            onValueDecreaseClick()
                        }
                    } else if (thumbOffsetY.value.absoluteValue >= (dragLimitVerticalPx * DRAG_LIMIT_VERTICAL_THRESHOLD_FACTOR)) {
                        onValueReset()
                    }

                    scope.launch {
                        if (dragDirection.value == DragDirection.HORIZONTAL &&
                            thumbOffsetX.value != 0f
                        ) {
                            thumbOffsetX.animateTo(
                                targetValue = 0f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        } else if (dragDirection.value == DragDirection.VERTICAL &&
                            thumbOffsetY.value != 0f
                        ) {
                            thumbOffsetY.animateTo(
                                targetValue = 0f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                    }
                }
            }
    ) {
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

private enum class DragDirection {
    NONE, HORIZONTAL, VERTICAL
}

@Composable
private fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }
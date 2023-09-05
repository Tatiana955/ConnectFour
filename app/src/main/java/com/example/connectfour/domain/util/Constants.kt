package com.example.connectfour.domain.util

object Constants {

    object BoardSize {
        const val BOARD_SIZE_MIN = "5 x 4"
        const val BOARD_SIZE_SMALL = "6 x 5"
        const val BOARD_SIZE_NORMAL = "7 x 6"
        const val BOARD_SIZE_MEDIUM = "8 x 7"
        const val BOARD_SIZE_LARGE = "9 x 7"
        const val BOARD_SIZE_EXTRA_LARGE = "8 x 8"
        const val BOARD_SIZE_MAX = "10 x 7"
    }

    object Name {
        const val FIRST_PLAYER = "First player"
        const val SECOND_PLAYER = "Second player"
    }

    object CounterButton {
        const val CONTAINER_OFFSET_FACTOR = 0.1f
        const val DRAG_LIMIT_HORIZONTAL_DP = 72
        const val DRAG_LIMIT_VERTICAL_DP = 64
        const val START_DRAG_THRESHOLD_DP = 2
        const val DRAG_LIMIT_HORIZONTAL_THRESHOLD_FACTOR = 0.9f
        const val DRAG_LIMIT_VERTICAL_THRESHOLD_FACTOR = 0.9f
        const val DRAG_CLEAR_ICON_REVEAL_DP = 2
        const val COUNTER_DELAY_INITIAL_MS = 500L
        const val COUNTER_DELAY_FAST_MS = 100L
    }
}
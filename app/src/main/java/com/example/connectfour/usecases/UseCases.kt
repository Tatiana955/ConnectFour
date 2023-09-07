package com.example.connectfour.usecases

import javax.inject.Inject

data class UseCases @Inject constructor(
    val checkWinUseCase: CheckWinUseCase
)
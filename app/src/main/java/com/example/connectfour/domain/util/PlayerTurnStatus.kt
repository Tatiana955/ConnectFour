package com.example.connectfour.domain.util

enum class PlayerTurnStatus(private val state: String) {
    Success("Successful turn."),
    FullColumn("The column is full. Try again."),
    FullField("The field is full. No winner."),
    WinningRound("%NAME% wins the round."),
    WinningGame("%NAME% wins the game."),
    ItIsDraw("It is draw.");

    override fun toString() = state
    fun getState(name: String) = state.replace("%NAME%", name)
}
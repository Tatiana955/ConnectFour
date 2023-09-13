package com.example.connectfour.domain.util

enum class GameMode(private val mode: String) {
    SINGLE("Single"),
    MULTI("Multiple");

    override fun toString() = mode
}
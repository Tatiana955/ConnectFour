package com.example.connectfour.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Section(
    modifier: Modifier,
    text: String,
    background: Color = MaterialTheme.colorScheme.primaryContainer
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(background),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            modifier = modifier.padding(6.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
    Spacer(modifier = modifier.height(10.dp))
}
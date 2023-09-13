package com.example.connectfour.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.connectfour.R
import com.example.connectfour.navigation.NavGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    viewModel: AppViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        content = {
            NavGraph(
                paddingValues = it,
                viewModel = viewModel
            )
        }
    )
}
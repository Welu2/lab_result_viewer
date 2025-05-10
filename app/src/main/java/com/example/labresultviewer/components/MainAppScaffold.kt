package com.example.labresultviewer.components

import AppBottomBar
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun MainAppScaffold(
    showBottomBar: Boolean,
    navController: NavController,
    userRole: String?,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                when (userRole) {
                    "admin" -> AdminBottomBar(navController)
                    "user" -> AppBottomBar(navController)
                }
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
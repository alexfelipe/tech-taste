package br.com.alura.techtaste.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.alura.techtaste.screens.AssistantScreen
import br.com.alura.techtaste.screens.HomeScreen
import br.com.alura.techtaste.ui.viewmodels.AssistantViewModel
import kotlinx.coroutines.launch

@Composable
fun TechTasteNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen()
        }
        composable("assistant") {
            val viewModel = viewModel<AssistantViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            val scope = rememberCoroutineScope()
            AssistantScreen(
                uiState = uiState,
                onCloseClick = {
                    navController.popBackStack()
                },
                onSendClick = { text ->
                    scope.launch {
                        viewModel.send(text)
                    }
                },
                onRetryMessageClick = {
                    scope.launch {
                        viewModel.retry()
                    }
                },
                onDeleteMessageClick = {
                    scope.launch {
                        viewModel.deleteLast()
                    }
                }
            )
        }
    }
}

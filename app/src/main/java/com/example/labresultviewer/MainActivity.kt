package com.example.labresultviewer

import HomeScreen
import LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.labresultviewer.components.MainAppScaffold
import com.example.labresultviewer.ui.CreateAccountScreen
import com.example.labresultviewer.ui.CreateProfileScreen
import com.example.labresultviewer.ui.WelcomeScreen
import com.example.labresultviewer.ui.theme.LabResultViewerTheme
import com.example.labresultviewer.ui.SuccessScreen
import com.example.labresultviewer.ui.labresults.LabResultsScreen
import com.example.labresultviewer.viewmodel.labresults.LabResultsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabResultViewerTheme(darkTheme = false) {

                AppNavigation()
            }
        }
    }

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onCreateAccountClick = { navController.navigate(Screen.CreateAccount.route) },
                onLoginClick = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.CreateAccount.route) {
            CreateAccountScreen(
                onBackClick = { navController.popBackStack() },
                onCreateClick = { navController.navigate(Screen.Success.route) },
                onLoginClick = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.CreateProfile.route) {
            CreateProfileScreen(
                onBackClick = { navController.popBackStack() },
                onCreateClick = { navController.navigate(Screen.Home.route) }
            )
        }
        composable(Screen.Success.route) {
            SuccessScreen(
                onCompleteClick = { navController.navigate(Screen.CreateProfile.route) }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onLoginClick = { navController.navigate(Screen.Home.route) },
                onForgotPasswordClick = { navController.navigate(Screen.ForgotPassword.route) },
                onCreateAccountClick = { navController.navigate(Screen.CreateAccount.route) }
            )
        }
        composable(Screen.Home.route) {
            MainAppScaffold(showBottomBar = Screen.Home.showBottomBar, navController = navController) { padding ->
                HomeScreen(userName = "Abebech")
            }
        }
        composable(Screen.LabResults.route) {
            MainAppScaffold(showBottomBar = Screen.LabResults.showBottomBar, navController = navController) {padding ->
                val viewModel = hiltViewModel<LabResultsViewModel>()
                LabResultsScreen(viewModel = viewModel)
            }

        }
    }
}
    }



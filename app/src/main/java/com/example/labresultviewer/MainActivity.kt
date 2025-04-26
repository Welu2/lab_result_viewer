package com.example.labresultviewer

import HomeScreen
import LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.labresultviewer.ui.CreateAccountScreen
import com.example.labresultviewer.ui.CreateProfileScreen
import com.example.labresultviewer.ui.WelcomeScreen
import com.example.labresultviewer.ui.theme.LabResultViewerTheme
import com.example.labresultviewer.Screen.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabResultViewerTheme(darkTheme = false) {
                AppNavigation()
            }
            }
        }
    }

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Welcome.route
    ) {
        composable(Welcome.route) {
            WelcomeScreen(
                onCreateAccountClick = { navController.navigate(CreateAccount.route) },
                onLoginClick = { navController.navigate(Login.route) }
            )
        }
        composable(CreateAccount.route) {
            CreateAccountScreen(
                onBackClick = { navController.popBackStack() },
                onCreateClick = { navController.navigate(CreateProfile.route) },
                onLoginClick = { navController.navigate(Login.route) }
            )
        }
        composable(CreateProfile.route) {
            CreateProfileScreen(
                onBackClick = { navController.popBackStack() },
                onCreateClick = { navController.navigate(Home.route) }
            )
        }
        composable(Login.route) {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onLoginClick = { navController.navigate(Home.route) },
                onForgotPasswordClick = { navController.navigate(ForgotPassword.route) },
                onCreateAccountClick = { navController.navigate(CreateAccount.route) }
            )
        }
        composable(Home.route) {
            HomeScreen()
        }
    }
}



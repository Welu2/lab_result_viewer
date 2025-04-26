package com.example.labresultviewer

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object CreateAccount : Screen("create_account")
    object Login : Screen("login")
    object CreateProfile : Screen("create_profile")
    object Home : Screen("home")
    object ForgotPassword : Screen("forgot_password")
    // Add other screens here
}
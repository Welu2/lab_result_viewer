package com.example.labresultviewer

import LoginScreen
import android.os.Bundle
import android.util.Log
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
import com.example.labresultviewer.ui.admin.AppointmentsApprovalScreen
import com.example.labresultviewer.ui.admin.DashboardScreen
import com.example.labresultviewer.ui.admin.LabResultsManagementScreen
import com.example.labresultviewer.ui.admin.PatientsScreen
import com.example.labresultviewer.ui.admin.UploadLabReportScreen
import com.example.labresultviewer.ui.labresults.LabResultsScreen
import com.example.labresultviewer.ui.screens.HomeScreen
import com.example.labresultviewer.viewmodel.labresults.LabResultsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabResultViewerTheme(darkTheme = false) {

                AppNavigation()
            }
        }
    }
     // Composable function responsible for setting up navigation between screens
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
                    onSuccess = {
                        Log.d("Navigation", "Signup successful, navigating to Success screen!")
                        navController.navigate(Screen.Success.route)
                    },
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
                    onLoginSuccess = {
                        navController.navigate(Screen.LabResultsManagement.route)
                        Log.d("Navigation", "Navigating to Home screen after login success!")
                    },
                    onForgotPasswordClick = { navController.navigate(Screen.ForgotPassword.route) },
                    onCreateAccountClick = { navController.navigate(Screen.CreateAccount.route) }
                )
            }
            composable(Screen.Home.route) {
                MainAppScaffold(
                    showBottomBar = Screen.Home.showBottomBar,
                    navController = navController
                ) { padding ->
                    HomeScreen()
                }
            }
            composable(Screen.LabResults.route) {
                MainAppScaffold(
                    showBottomBar = Screen.LabResults.showBottomBar,
                    navController = navController
                ) { padding ->
                    val viewModel = hiltViewModel<LabResultsViewModel>()
                    LabResultsScreen(viewModel = viewModel)
                }

            }
            composable(Screen.AdminDashboard.route) {
                MainAppScaffold(
                    showBottomBar = Screen.AdminDashboard.showBottomBar,
                    navController = navController
                ) { padding ->
                    DashboardScreen()
                }
            }
            composable(Screen.Patients.route) {
                MainAppScaffold(
                    showBottomBar = Screen.Patients.showBottomBar,
                    navController = navController
                ) { padding ->
                    PatientsScreen(
                        onAddPatient = { /* TODO: Handle add patient */ },
                        onEditPatient = { /* TODO: Handle edit patient */ },
                        onViewProfile = { /* TODO: Handle view profile */ },
                        navController = navController
                    )
                }
            }
            composable(Screen.UploadLabReport.route) {
                MainAppScaffold(
                    showBottomBar = Screen.UploadLabReport.showBottomBar,
                    navController = navController
                ) { padding ->
                    val viewModel = hiltViewModel<LabResultsViewModel>()
                    UploadLabReportScreen(viewModel = viewModel)
                }
            }
            composable(Screen.AppointmentsApproval.route) {
                MainAppScaffold(
                    showBottomBar = Screen.AppointmentsApproval.showBottomBar,
                    navController = navController
                ) { padding ->
                    AppointmentsApprovalScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
             // Lab Results Management screen: admin can manage lab results here
            composable(Screen.LabResultsManagement.route){
                MainAppScaffold(
                    showBottomBar = Screen.LabResultsManagement.showBottomBar,
                    navController = navController
                ) { padding ->
                    LabResultsManagementScreen(
                        onUploadReport = { navController.navigate(Screen.UploadLabReport.route) }
                    )
                }
            }
        }
    }
}



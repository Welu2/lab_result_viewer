package com.example.labresultviewer

import LoginScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.labresultviewer.components.MainAppScaffold
import com.example.labresultviewer.data.SessionManager
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
import com.example.labresultviewer.ui.screens.HomeScreen
import com.example.labresultviewer.viewmodel.labresults.LabResultsViewModel
import com.example.labresultviewer.ui.admin.AppointmentScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.labresultviewer.ui.AppointmentsScreen
import com.example.labresultviewer.ui.LabResultsScreen
import com.example.labresultviewer.ui.NotificationListScreen
import com.example.labresultviewer.ui.UserProfileScreen
import com.example.labresultviewer.viewmodel.NotificationViewModel
import com.example.labresultviewer.viewmodel.UserProfileViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.example.labresultviewer.ui.admin.AdminSettingsScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabResultViewerTheme(darkTheme = false) {

                AppNavigation(sessionManager = sessionManager)
            }
        }
    }

    @Composable
    fun AppNavigation(sessionManager: SessionManager) {
        val navController = rememberNavController()
        val userRole by sessionManager.userRole.collectAsState(initial = "user")

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
                    onForgotPasswordClick = { navController.navigate(Screen.ForgotPassword.route) },
                    onCreateAccountClick = { navController.navigate(Screen.CreateAccount.route) },
                    navController = navController
                )
            }
            composable(Screen.Home.route) {
                MainAppScaffold(
                    showBottomBar = Screen.Home.showBottomBar,
                    navController = navController,
                    userRole = userRole
                ) { padding ->
                    HomeScreen(
                        navController = navController
                    )
                }
            }
            composable(Screen.LabResults.route) {
                MainAppScaffold(
                    showBottomBar = Screen.LabResults.showBottomBar,
                    navController = navController,
                    userRole = userRole
                ) { padding ->
                    val viewModel = hiltViewModel<LabResultsViewModel>()
                    LabResultsScreen(viewModel = viewModel)
                }
            }
            composable(Screen.Appointments.route) {
                MainAppScaffold(
                    showBottomBar = Screen.Appointments.showBottomBar,
                    navController = navController,
                    userRole = userRole
                ) { padding ->
                    AppointmentsScreen()
                }
            }
            composable(Screen.AdminDashboard.route) {
                MainAppScaffold(
                    showBottomBar = Screen.AdminDashboard.showBottomBar,
                    navController = navController,
                    userRole = userRole
                ) { padding ->
                    DashboardScreen(navController = navController)
                }
            }
            composable(Screen.Patients.route) {
                MainAppScaffold(
                    showBottomBar = Screen.Patients.showBottomBar,
                    navController = navController,
                    userRole = userRole
                ) { padding ->
                    PatientsScreen(
                        navController = navController
                    )
                }
            }
            composable(Screen.UploadLabReport.route) {
                MainAppScaffold(
                    showBottomBar = Screen.UploadLabReport.showBottomBar,
                    navController = navController,
                    userRole = userRole
                ) { padding ->
                    val viewModel = hiltViewModel<LabResultsViewModel>()
                    UploadLabReportScreen(viewModel = viewModel)
                }
            }
            composable(Screen.AppointmentsApproval.route) {
                MainAppScaffold(
                    showBottomBar = Screen.AppointmentsApproval.showBottomBar,
                    navController = navController,
                    userRole = userRole
                ) { padding ->
                    AppointmentsApprovalScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
            composable(Screen.LabResultsManagement.route){
                MainAppScaffold(
                    showBottomBar = Screen.LabResultsManagement.showBottomBar,
                    navController = navController,
                    userRole = userRole
                ) { padding ->
                    LabResultsManagementScreen(
                        onUploadReport = { navController.navigate(Screen.UploadLabReport.route) }
                    )
                }
            }
            composable(Screen.AppointmentsScreen.route) {
                MainAppScaffold(
                    showBottomBar = Screen.AppointmentsScreen.showBottomBar,
                    navController = navController,
                    userRole = userRole
                ) { padding ->
                    AppointmentScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
            composable("notifications") {
                val notificationViewModel = hiltViewModel<NotificationViewModel>()
                NotificationListScreen(
                    viewModel = notificationViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Profile.route) {
                val coroutineScope = rememberCoroutineScope()
                val userProfileViewModel = hiltViewModel<UserProfileViewModel>()
                val profile by userProfileViewModel.profile.collectAsState()

                // Load profile when entering the screen
                LaunchedEffect(Unit) {
                    userProfileViewModel.loadProfile()
                }

                MainAppScaffold(
                    showBottomBar = Screen.Profile.showBottomBar,
                    navController = navController,
                    userRole = userRole
                ) { padding ->
                    UserProfileScreen(
                        userName = profile?.name ?: "User",
                        dob = profile?.dateOfBirth ?: "",
                        onChangeEmail = { /* navController.navigate("change_email") */ },
                        onNotificationSetting = { /* navController.navigate("notification_settings") */ },
                        onLogout = {
                            userProfileViewModel.logout()
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onDeleteProfile = {
                            coroutineScope.launch {
                                val deleted = userProfileViewModel.deleteProfile()
                                if (deleted) {
                                    userProfileViewModel.logout()
                                    navController.navigate(Screen.Welcome.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            }
                            true
                        },
                        profileImageUrl = null
                    )
                }
            }
            composable(Screen.Settings.route) {
                MainAppScaffold(
                    showBottomBar = Screen.Settings.showBottomBar,
                    navController = navController,
                    userRole = userRole
                ) { padding ->
                    AdminSettingsScreen(
                        onLogout = {
                            sessionManager.clearSession()
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}



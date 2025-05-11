package com.example.labresultviewer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import currentRoute

@Composable
fun AdminBottomBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute(navController) == Screen.AdminDashboard.route,
            onClick = { navController.navigate(Screen.AdminDashboard.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Group, contentDescription = "Patients") },
            label = { Text("Patients") },
            selected = currentRoute(navController) == Screen.Patients.route,
            onClick = { navController.navigate(Screen.Patients.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Add, contentDescription = "Upload") },
            label = { Text("Upload") },
            selected = currentRoute(navController) == Screen.LabResultsManagement.route,
            onClick = { navController.navigate(Screen.LabResultsManagement.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.CalendarMonth, contentDescription = "Appts") },
            label = { Text("Appts") },
            selected = currentRoute(navController) == Screen.AppointmentsScreen.route,
            onClick = { navController.navigate(Screen.AppointmentsScreen.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentRoute(navController) == Screen.Settings.route,
            onClick = { navController.navigate(Screen.Settings.route) }
        )

    } 
}
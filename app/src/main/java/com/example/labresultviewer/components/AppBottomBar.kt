import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomBar(navController: NavController) {
    val currentRoute = currentRoute(navController)

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == Screen.Home.route,
            onClick = { navController.navigate(Screen.Home.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Assignment, contentDescription = "Lab Results") },
            label = { Text("Lab Results") },
            selected = currentRoute == Screen.LabResults.route,
            onClick = { navController.navigate(Screen.LabResults.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.CalendarMonth, contentDescription = "Appts") },
            label = { Text("Appointments") },
            selected = currentRoute == Screen.Appointments.route,
            onClick = { navController.navigate(Screen.Appointments.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Lab Results") },
            label = { Text("Profile") },
            selected = currentRoute == Screen.Profile.route,
            onClick = { navController.navigate(Screen.Profile.route)}
        )
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
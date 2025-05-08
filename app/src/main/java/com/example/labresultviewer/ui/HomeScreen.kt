import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// For Modifier and its extensions
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape

// For Card styling
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen(userName: String = "Abebech") {
    var search by remember { mutableStateOf("") }
    // Placeholder values for now, to be replaced with backend data
    var totalTests by remember { mutableStateOf(2) }
    var abnormalResults by remember { mutableStateOf(1) }
    var currentTab by remember { mutableStateOf("home") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true,
                    onClick = { /* TODO */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.CalendarToday, contentDescription = "Appointments") },
                    label = { Text("Appointments") },
                    selected = currentTab == "appointments",
                    onClick = {
                        currentTab = "appointments"
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Profile", ) },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = { /* TODO */ }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(18.dp)
                .fillMaxSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box (Modifier.size(48.dp).border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = CircleShape).clip(
                    CircleShape), contentAlignment = Alignment.Center){
                    Text(text = userName.take(1).uppercase())
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Hello, $userName", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "Notifications", Modifier.size(30.dp))

            }

            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Search") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                trailingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search", modifier = Modifier.size(40.dp).padding(end = 10.dp))
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text("Services", color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listOf("Ultrasound", "CT Scan", "MRI", "Blood Work")) { service ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.primary)

                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarMonth,
                                contentDescription = "Calendar",
                                modifier = Modifier.size(48.dp).padding(start = 20.dp),
                                tint = Color.White
                            )
                            Text(service, modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                            Text("Book now!", modifier = Modifier.padding(start = 16.dp), color = Color.White)
                        }
                    }

                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                   Icon(imageVector = Icons.Outlined.MonitorHeart, contentDescription = "Heart", modifier = Modifier.size(48.dp).padding(start = 20.dp, top = 16.dp), tint = Color.Green)
                    Text(
                        "Health Summary",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )}
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
                        Column {
                        Text("Total Tests", fontSize = 16.sp)
                            Text("$totalTests", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Column { Text("Abnormal Results", fontSize = 16.sp)
                            Text("$abnormalResults", fontWeight = FontWeight.Bold, fontSize = 16.sp)
}
                    }
                }
            }
        }
    }
}

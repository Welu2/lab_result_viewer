import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.labresultviewer.viewmodel.AuthViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onCreateAccountClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onBackClick: () -> Unit,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        Log.d("LoginScreen", "LaunchedEffect triggered: $loginState")

        when (loginState) {
            is AuthViewModel.AuthResult.Success -> {
                Log.d("LoginScreen", "Login successful. Navigating based on role.")
                if (rememberMe) {
                    // Save credentials
                }
                val role = (loginState as AuthViewModel.AuthResult.Success).userWithToken.role
                when (role.lowercase()) {
                    "admin" -> {
                        Log.d("LoginScreen", "Admin user, navigating to AdminDashboard")
                        navController.navigate(Screen.AdminDashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                    else -> {
                        Log.d("LoginScreen", "Regular user, navigating to Home")
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            }
            is AuthViewModel.AuthResult.Error -> {
                errorMessage = (loginState as AuthViewModel.AuthResult.Error).message
                Log.e("LoginScreen", "Login error: $errorMessage")
            }
            is AuthViewModel.AuthResult.Loading -> {
                errorMessage = null
            }
            else -> {
                errorMessage = null
                Log.d("LoginScreen", "Login state: $loginState")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top,
            ) {
                Text("Log in", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("Welcome Back!", fontSize = 12.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = it.isBlank()
                        errorMessage = null
                    },
                    label = { Text("Email") },
                    shape = RoundedCornerShape(18.dp),
                    isError = emailError,
                    modifier = Modifier.fillMaxWidth()
                )

                if (emailError) {
                    Text("Please enter a valid email", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = it.isBlank()
                        errorMessage = null
                    },
                    label = { Text("Password") },
                    shape = RoundedCornerShape(18.dp),
                    isError = passwordError,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (passwordError) {
                    Text("Password is required", color = Color.Red, fontSize = 12.sp)
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onForgotPasswordClick) {
                        Text("Forgot Password?", fontSize = 12.sp)
                    }
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it }
                    )
                    Text("Remember Me", fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = {
                        emailError = email.isBlank()
                        passwordError = password.isBlank()
                        if (!emailError && !passwordError) {
                            viewModel.login(email, password)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    enabled = loginState !is AuthViewModel.AuthResult.Loading
                ) {
                    if (loginState is AuthViewModel.AuthResult.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Log in")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Are you new?", color = Color.Gray, fontSize = 13.sp)
                    TextButton(onClick = onCreateAccountClick) {
                        Text("Create an account", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

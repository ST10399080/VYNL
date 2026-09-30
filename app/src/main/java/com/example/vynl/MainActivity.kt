package com.example.vynl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vynl.ui.auth.RegisterScreen
import com.example.vynl.ui.screen.auth.LoginScreen
import com.example.vynl.ui.theme.VYNLTheme
import com.example.vynl.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VYNLTheme {
                VYNLApp()
            }
        }
    }
}

@Composable
fun VYNLApp(
    authViewModel: AuthViewModel = viewModel()
) {
    var showSplash by remember { mutableStateOf(true) }
    var showRegister by remember { mutableStateOf(false) }

    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        delay(1500)
        showSplash = false
    }

    AnimatedVisibility(
        visible = showSplash,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        SplashScreen()
    }

    if (!showSplash) {
        when {
            currentUser != null -> {
                HomePlaceholder()
            }

            showRegister -> {
                RegisterScreen(
                    authViewModel = authViewModel,
                    onRegisterSuccess = {
                        // Firebase authentication state updates automatically.
                    },
                    onLoginClick = {
                        showRegister = false
                        authViewModel.clearError()
                    }
                )
            }

            else -> {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = {
                        // Firebase authentication state updates automatically.
                    },
                    onRegisterClick = {
                        showRegister = true
                        authViewModel.clearError()
                    }
                )
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF090611),
                        Color(0xFF120A22),
                        Color(0xFF090611)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "VYNL",
                color = Color(0xFFF5F3FF),
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 6.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Discover. Rate. Remember.",
                color = Color(0xFFB8AEC8),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = "VYNL",
            color = Color(0xFF6C3FBF),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Composable
fun HomePlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Welcome to VYNL"
        )
    }
}
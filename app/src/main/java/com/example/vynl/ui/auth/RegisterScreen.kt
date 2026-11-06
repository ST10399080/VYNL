package com.example.vynl.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynl.ui.viewmodel.AuthViewModel

private val VynlPurple = Color(0xFF6C3FBF)
private val VynlCyan = Color(0xFF06B6D4)
private val VynlPink = Color(0xFFEC4899)
private val VynlBackground = Color(0xFF090611)
private val VynlSurface = Color(0xFF171020)
private val VynlText = Color(0xFFF5F3FF)
private val VynlSecondaryText = Color(0xFFB8AEC8)

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    val currentUser by authViewModel.currentUser.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        VynlBackground,
                        Color(0xFF120A22),
                        VynlBackground
                    )
                )
            )
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "VYNL",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = VynlText,
            letterSpacing = 4.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Discover. Rate. Remember.",
            fontSize = 14.sp,
            color = VynlSecondaryText
        )

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Create your account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = VynlText
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Start your music journey with VYNL.",
            fontSize = 14.sp,
            color = VynlSecondaryText
        )

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                validationError = null
                authViewModel.clearError()
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Email")
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VynlCyan,
                unfocusedBorderColor = VynlSurface,
                focusedLabelColor = VynlCyan,
                unfocusedLabelColor = VynlSecondaryText,
                focusedTextColor = VynlText,
                unfocusedTextColor = VynlText,
                cursorColor = VynlCyan
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                validationError = null
                authViewModel.clearError()
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Password")
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VynlPink,
                unfocusedBorderColor = VynlSurface,
                focusedLabelColor = VynlPink,
                unfocusedLabelColor = VynlSecondaryText,
                focusedTextColor = VynlText,
                unfocusedTextColor = VynlText,
                cursorColor = VynlPink
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                validationError = null
                authViewModel.clearError()
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Confirm Password")
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VynlPurple,
                unfocusedBorderColor = VynlSurface,
                focusedLabelColor = VynlPurple,
                unfocusedLabelColor = VynlSecondaryText,
                focusedTextColor = VynlText,
                unfocusedTextColor = VynlText,
                cursorColor = VynlPurple
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        val displayedError = validationError ?: errorMessage

        if (displayedError != null) {
            Text(
                text = displayedError,
                color = Color(0xFFFF6B6B),
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {
                when {
                    email.isBlank() -> {
                        validationError = "Please enter your email address."
                    }

                    password.isBlank() -> {
                        validationError = "Please enter a password."
                    }

                    password.length < 6 -> {
                        validationError =
                            "Password must be at least 6 characters."
                    }

                    confirmPassword.isBlank() -> {
                        validationError =
                            "Please confirm your password."
                    }

                    password != confirmPassword -> {
                        validationError =
                            "Passwords do not match."
                    }

                    else -> {
                        validationError = null

                        authViewModel.register(
                            email = email.trim(),
                            password = password
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            enabled = !isLoading,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VynlPurple,
                contentColor = Color.White
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Create Account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        TextButton(
            onClick = onLoginClick,
            enabled = !isLoading
        ) {
            Text(
                text = "Already have an account? Login",
                color = VynlCyan
            )
        }
    }
}
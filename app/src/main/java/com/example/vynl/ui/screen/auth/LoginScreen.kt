package com.example.vynl.ui.screen.auth

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val currentUser by authViewModel.currentUser.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()

    /*
     * Only trigger the success callback when Firebase
     * reports that a user has successfully authenticated.
     */
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VynlBackground)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "VYNL",
            color = VynlText,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Discover. Rate. Remember.",
            color = VynlSecondaryText,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Welcome back",
            color = VynlText,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sign in to continue your music journey.",
            color = VynlSecondaryText,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                authViewModel.clearError()
            },
            label = {
                Text("Email")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
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
                authViewModel.clearError()
            },
            label = {
                Text("Password")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            visualTransformation = PasswordVisualTransformation(),
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

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
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
                authViewModel.login(
                    email = email.trim(),
                    password = password
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading &&
                    email.isNotBlank() &&
                    password.isNotBlank(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VynlPurple,
                contentColor = VynlText
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = VynlText,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Login",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Don't have an account?",
            color = VynlSecondaryText,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = onRegisterClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = VynlCyan
            )
        ) {
            Text(
                text = "Create an account",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
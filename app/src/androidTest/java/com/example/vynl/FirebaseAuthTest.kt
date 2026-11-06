package com.example.vynl

import com.example.vynl.data.remote.auth.FirebaseAuthService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FirebaseAuthTest {

    @Test
    fun testFirebaseRegistrationLoginAndLogout() {
        runBlocking {

            println("===== VYNL FIREBASE AUTH TEST =====")
            println()

            val authService = FirebaseAuthService()

            val email = "vynl.test.2026@example.com"
            val password = "VynlTest123!2026"

            println("----- REGISTER / LOGIN -----")

            try {

                val registeredUser = authService.register(
                    email = email,
                    password = password
                )

                println("New Firebase account created.")
                println("Firebase UID: ${registeredUser?.uid}")

            } catch (exception: Exception) {

                println("Account may already exist.")
                println("Attempting login instead.")

                val loggedInUser = authService.login(
                    email = email,
                    password = password
                )

                println("Login successful.")
                println("Firebase UID: ${loggedInUser?.uid}")
            }

            println()

            println("----- CURRENT USER -----")

            val currentUser = authService.getCurrentUser()

            println("Current Firebase UID: ${currentUser?.uid}")
            println("Current Firebase email: ${currentUser?.email}")

            println()

            println("----- LOGOUT -----")

            authService.logout()

            println("Logout completed.")
            println(
                "Current user after logout: " +
                        authService.getCurrentUser()
            )

            println()

            println("===== FIREBASE AUTH TEST COMPLETE =====")
        }
    }
}
package com.example.vynl.data.remote.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthService {

    private val firebaseAuth: FirebaseAuth =
        FirebaseAuth.getInstance()

    suspend fun register(
        email: String,
        password: String
    ): FirebaseUser? {

        val result = firebaseAuth
            .createUserWithEmailAndPassword(
                email,
                password
            )
            .await()

        return result.user
    }

    suspend fun login(
        email: String,
        password: String
    ): FirebaseUser? {

        val result = firebaseAuth
            .signInWithEmailAndPassword(
                email,
                password
            )
            .await()

        return result.user
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}
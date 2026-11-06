package com.example.vynl_app.data.account

import kotlinx.coroutines.flow.StateFlow

interface AccountRepository {
    val account: StateFlow<UserAccount>

    // Fails with AccountException when the input is invalid.
    suspend fun updateAccount(displayName: String, email: String): Result<Unit>
}
